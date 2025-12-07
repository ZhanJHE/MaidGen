package com.Github.ZhanJHE.MaidGen.controller;

import com.Github.ZhanJHE.MaidGen.model.PasswordOptions;
import com.Github.ZhanJHE.MaidGen.service.PasswordGeneratorService;
import com.Github.ZhanJHE.MaidGen.view.PasswordGeneratorUI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Scanner;

@Component
public class PasswordController implements CommandLineRunner {

    private final PasswordGeneratorService passwordGeneratorService;

    public PasswordController(PasswordGeneratorService passwordGeneratorService) {
        this.passwordGeneratorService = passwordGeneratorService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("欢迎使用 MaidGen 密码生成器!");
        System.out.print("您希望使用图形用户界面 (GUI) 吗? (y/n): ");

        try (Scanner scanner = new Scanner(System.in)) {
            String choice = scanner.nextLine().trim().toLowerCase();

            if ("y".equals(choice)) {
                // Run the GUI on the Event Dispatch Thread
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