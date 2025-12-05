package com.Github.ZhanJHE.MaidGen.controller;

import com.Github.ZhanJHE.MaidGen.model.PasswordOptions;
import com.Github.ZhanJHE.MaidGen.service.PasswordGeneratorService;
import com.Github.ZhanJHE.MaidGen.util.UserInputValidator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * 密码生成器命令行控制器
 */
@Component
public class PasswordController implements CommandLineRunner {

    private final PasswordGeneratorService passwordGeneratorService;

    /**
     * 构造函数
     *
     * @param passwordGeneratorService 密码生成器服务
     */
    public PasswordController(PasswordGeneratorService passwordGeneratorService) {
        this.passwordGeneratorService = passwordGeneratorService;
    }

    /**
     * 命令行运行方法
     *
     * @param args 命令行参数
     * @throws Exception 异常
     */
    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        StringBuilder finalPassword = new StringBuilder();

        int numberOfSegments = UserInputValidator.getNumberOfSegments(scanner);

        for (int i = 1; i <= numberOfSegments; i++) {
            System.out.println("\n--- 第 " + i + " 段密码选项 ---");
            PasswordOptions options = new PasswordOptions();

            // 获取用户输入的密码长度
            System.out.print("请输入第 " + i + " 段的单词数量: ");
            options.setLength(scanner.nextInt());

            try {
                // 生成密码段并追加
                String passwordSegment = passwordGeneratorService.generatePassword(options);
                finalPassword.append(passwordSegment);
                System.out.println("生成的第 " + i + " 段密码: " + passwordSegment);
            } catch (IllegalArgumentException e) {
                // 捕获并打印异常信息
                System.err.println("生成第 " + i + " 段密码时出错: " + e.getMessage());
            }
        }

        System.out.println("\n--- 最终生成的密码 ---");
        System.out.println(finalPassword.toString());
    }
}