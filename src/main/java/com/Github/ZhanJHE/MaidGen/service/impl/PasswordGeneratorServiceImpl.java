package com.Github.ZhanJHE.MaidGen.service.impl;

import com.Github.ZhanJHE.MaidGen.model.PasswordOptions;
import com.Github.ZhanJHE.MaidGen.service.PasswordGeneratorService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * 密码生成器服务实现类
 */
@Service
public class PasswordGeneratorServiceImpl implements PasswordGeneratorService {

    private static final Random RANDOM = new SecureRandom();
    private List<String> words;

    public PasswordGeneratorServiceImpl() {
        try {
            // 从JSON文件中加载单词列表
            InputStream inputStream = TypeReference.class.getResourceAsStream("/words.json");
            words = new ObjectMapper().readValue(inputStream, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("无法加载单词列表", e);
        }
    }

    /**
     * 生成密码
     *
     * @param options 密码选项
     * @return 生成的密码
     */
    @Override
    public String generatePassword(PasswordOptions options) {
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException("单词列表为空，无法生成密码");
        }

        StringBuilder password = new StringBuilder();
        for (int i = 0; i < options.getLength(); i++) {
            password.append(words.get(RANDOM.nextInt(words.size())));
        }

        return password.toString();
    }
}