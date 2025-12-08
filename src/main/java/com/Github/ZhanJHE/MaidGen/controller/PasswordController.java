package com.Github.ZhanJHE.MaidGen.controller;

import com.Github.ZhanJHE.MaidGen.model.PasswordOptions;
import com.Github.ZhanJHE.MaidGen.service.PasswordGeneratorService;
import com.Github.ZhanJHE.MaidGen.view.PasswordGeneratorUI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Scanner;

/**
 * 控制器类，用于处理用户交互并根据用户的选择（GUI或命令行）启动密码生成过程。
 */
@Component
public class PasswordController implements CommandLineRunner {

    private final PasswordGeneratorService passwordGeneratorService;

    /**
     * 构造一个带有密码生成器服务的 PasswordController。
     *
     * @param passwordGeneratorService 用于生成密码的服务。
     */
    public PasswordController(PasswordGeneratorService passwordGeneratorService) {
        this.passwordGeneratorService = passwordGeneratorService;
    }

    /**
     * 应用程序启动时执行，提示用户选择GUI或命令行模式。
     *
     * @param args 命令行参数（未使用）。
     * @throws Exception 如果在处理过程中发生错误。
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("欢迎使用 MaidGen 密码生成器!");
        System.out.print("您希望使用图形用户界面 (GUI) 吗? (y/n): ");

        try (Scanner scanner = new Scanner(System.in)) {
            String choice = scanner.nextLine().trim().toLowerCase();

            if ("y".equals(choice)) {
                // 在事件调度线程上运行GUI
                SwingUtilities.invokeLater(() -> {
                    PasswordGeneratorUI ui = new PasswordGeneratorUI(passwordGeneratorService);
                    ui.setVisible(true);
                });
            } else {
                System.out.println("\n--- 命令行模式 ---");
                PasswordOptions options = new PasswordOptions(scanner);
                passwordGeneratorService.generatePasswords(options);
                System.out.println("\n程序执行完毕。");
            }
        } catch (Exception e) {
            System.err.println("发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}