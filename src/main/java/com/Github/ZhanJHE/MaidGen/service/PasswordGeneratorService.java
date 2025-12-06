package com.Github.ZhanJHE.MaidGen.service;

import com.Github.ZhanJHE.MaidGen.model.PasswordOptions;
import com.Github.ZhanJHE.MaidGen.model.PasswordSegmentOptions;
import com.Github.ZhanJHE.MaidGen.model.PasswordStorage;
import com.Github.ZhanJHE.MaidGen.model.enums.DataSource;
import com.Github.ZhanJHE.MaidGen.model.enums.WordCase;
import com.Github.ZhanJHE.MaidGen.repository.PasswordRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

/**
 * 密码生成器服务类，负责根据用户定义的选项生成和管理密码。
 * 该服务可以从多种数据源（如外部文件、JSON单词列表、随机标点或数字）生成密码段，
 * 并将它们组合成最终的密码。此外，它还支持将生成的密码保存到文件中。
 */
@Service
public class PasswordGeneratorService {

    private final PasswordRepository passwordRepository;
    /**
     * 用于生成随机标点符号的字符集。
     */
    private static final String PUNCTUATION = "!@#$%?&*_+-=,";

    /**
     * 构造函数，用于注入密码仓库的依赖。
     *
     * @param passwordRepository 密码仓库，用于保存生成的密码。
     */
    public PasswordGeneratorService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    /**
     * 主入口方法，用于根据提供的选项生成指定数量的密码，并将其显示和保存。
     *
     * @param options 包含所有密码生成规则的配置对象。
     */
    public void generatePasswords(PasswordOptions options) {
        PasswordStorage passwordStorage = new PasswordStorage();
        for (int p = 1; p <= options.getNumberOfPasswords(); p++) {
            System.out.println("\n--- 正在生成第 " + p + " 个密码 ---");
            try {
                String finalPassword = generateFullPassword(options);
                System.out.println("\n--- 最终生成的第 " + p + " 个密码 ---");
                System.out.println(finalPassword);
                passwordStorage.addPassword(finalPassword);
            } catch (IllegalArgumentException e) {
                System.err.println("生成密码时出错: " + e.getMessage());
            }
        }

        if (options.isSaveToFile()) {
            passwordRepository.savePasswords(passwordStorage.getPasswords(), options.getSavePath());
            System.out.println("\n密码已保存到 " + options.getSavePath());
        }
    }

    /**
     * 根据密码选项生成一个完整的密码。
     * 该方法会遍历所有密码段选项，并依次生成每个段，最后拼接成一个完整的密码。
     *
     * @param options 包含所有密码生成规则的配置对象。
     * @return 生成的完整密码字符串。
     */
    public String generateFullPassword(PasswordOptions options) {
        StringBuilder fullPassword = new StringBuilder();
        for (PasswordSegmentOptions segmentOptions : options.getPasswordSegments()) {
            String passwordSegment = generatePasswordSegment(segmentOptions);
            fullPassword.append(passwordSegment);
        }
        return fullPassword.toString();
    }

    /**
     * 根据单个密码段的选项生成密码段。
     *
     * @param options 单个密码段的配置选项。
     * @return 生成的密码段字符串。
     */
    public String generatePasswordSegment(PasswordSegmentOptions options) {
        String segment = "";
        switch (options.getDataSource()) {
            case RANDOM_PUNCTUATION:
                segment = generateRandomPunctuation(options.getLength());
                break;
            case RANDOM_NUMBERS:
                segment = generateRandomNumbers(options.getLength());
                break;
            case EXTERNAL_FILE:
            case WORD_LIST_JSON:
                List<String> words = getWords(options);
                if (words.isEmpty()) {
                    throw new IllegalArgumentException("单词列表为空。 ");
                }
                Random random = new Random();
                segment = words.get(random.nextInt(words.size()));
                segment = applyWordCase(segment, options.getWordCase());
                break;
        }
        return segment;
    }

    /**
     * 根据密码段选项从指定的数据源获取单词列表。
     *
     * @param options 单个密码段的配置选项。
     * @return 从文件或JSON资源中读取的单词列表。
     * @throws RuntimeException 如果加载或解析单词列表时发生IO异常。
     */
    private List<String> getWords(PasswordSegmentOptions options) {
        try {
            if (options.getDataSource() == DataSource.EXTERNAL_FILE) {
                return Files.readAllLines(Paths.get(options.getFilePath()));
            } else { // WORD_LIST_JSON
                String resourcePath = "src/main/resources/words_" + options.getLength() + ".json";
                InputStream inputStream = getClass().getResourceAsStream(resourcePath);
                if (inputStream == null) {
                    throw new IllegalArgumentException("找不到资源文件： " + resourcePath);
                }
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(inputStream, new TypeReference<List<String>>() {});
            }
        } catch (IOException e) {
            throw new RuntimeException("加载或解析单词列表时出错", e);
        }
    /**
     * 生成一个指定长度的随机标点符号字符串。
     *
     * @param length 要生成的字符串的长度。
     * @return 生成的随机标点符号字符串。
     */
    private String generateRandomPunctuation(int length) {
        StringBuilder punctuation = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            punctuation.append(PUNCTUATION.charAt(random.nextInt(PUNCTUATION.length())));
        }
        return punctuation.toString();
    }

    /**
     * 生成一个指定长度的随机数字字符串。
     *
     * @param length 要生成的字符串的长度。
     * @return 生成的随机数字字符串。
     */
    private String generateRandomNumbers(int length) {
        StringBuilder numbers = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            numbers.append(random.nextInt(10));
        }
        return numbers.toString();
    }

    /**
     * 根据指定的单词大小写规则格式化单词。
     *
     * @param word     要格式化的单词。
     * @param wordCase 单词大小写规则（全大写、首字母大写、全小写）。
     * @return 格式化后的单词。
     */
    private String applyWordCase(String word, WordCase wordCase) {
        if (wordCase == null) return word;
        switch (wordCase) {
            case UPPERCASE:
                return word.toUpperCase();
            case CAPITALIZE:
                return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            case LOWERCASE:
                return word.toLowerCase();
            default:
                return word;
        }
    }
}