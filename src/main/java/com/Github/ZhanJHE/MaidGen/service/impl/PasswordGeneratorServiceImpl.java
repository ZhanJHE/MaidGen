package com.Github.ZhanJHE.MaidGen.service.impl;

import com.Github.ZhanJHE.MaidGen.model.PasswordOptions;
import com.Github.ZhanJHE.MaidGen.service.PasswordGeneratorService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * 密码生成器服务实现类
 */
@Service
public class PasswordGeneratorServiceImpl implements PasswordGeneratorService {

    // 定义大写字母、小写字母、数字和特殊字符的字符集
    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+<>?";

    /**
     * 生成密码
     *
     * @param options 密码生成选项
     * @return 生成的密码
     */
    @Override
    public String generatePassword(PasswordOptions options) {
        StringBuilder password = new StringBuilder();
        String availableChars = "";

        // 根据用户选择，构建可用字符集
        if (options.isUseUpperCase()) {
            availableChars += UPPER_CASE;
        }
        if (options.isUseLowerCase()) {
            availableChars += LOWER_CASE;
        }
        if (options.isUseNumbers()) {
            availableChars += NUMBERS;
        }
        if (options.isUseSpecialCharacters()) {
            availableChars += SPECIAL_CHARACTERS;
        }

        // 如果没有选择任何字符类型，则抛出异常
        if (availableChars.isEmpty()) {
            throw new IllegalArgumentException("必须至少选择一种字符类型。");
        }

        // 使用 SecureRandom 生成随机密码
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < options.getLength(); i++) {
            int randomIndex = random.nextInt(availableChars.length());
            password.append(availableChars.charAt(randomIndex));
        }

        return password.toString();
    }
}