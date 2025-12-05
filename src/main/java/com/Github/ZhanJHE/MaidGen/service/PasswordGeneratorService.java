package com.Github.ZhanJHE.MaidGen.service;

import com.Github.ZhanJHE.MaidGen.model.PasswordOptions;

/**
 * 密码生成器服务接口
 */
public interface PasswordGeneratorService {

    /**
     * 生成密码
     *
     * @param options 密码生成选项
     * @return 生成的密码
     */
    String generatePassword(PasswordOptions options);
}