package com.Github.ZhanJHE.MaidGen.controller;

import com.Github.ZhanJHE.MaidGen.model.PasswordOptions;
import com.Github.ZhanJHE.MaidGen.service.PasswordGeneratorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class PasswordController implements CommandLineRunner {

    private final PasswordGeneratorService passwordGeneratorService;

    public PasswordController(PasswordGeneratorService passwordGeneratorService) {
        this.passwordGeneratorService = passwordGeneratorService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);// 用于读取用户输入
        PasswordOptions options = new PasswordOptions(scanner); // 获取用户输入的密码选项
        scanner.close(); // 关闭 Scanner
        passwordGeneratorService.generatePasswords(options); // 进入service层生成密码

    }
}