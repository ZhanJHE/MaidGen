package com.Github.ZhanJHE.MaidGen.model;

import com.Github.ZhanJHE.MaidGen.util.UserInputValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 存储密码生成选项，包括要生成的密码总数、密码段数以及每个密码段的配置。
 */
public class PasswordOptions {

    private int numberOfPasswords; // 要生成的密码总数
    private int numberOfSegments; // 密码中的段数
    private List<PasswordSegmentOptions> passwordSegments; // 每个密码段的配置列表
    private boolean saveToFile; // 是否将生成的密码保存到文件
    private String savePath; // 密码文件的保存路径

    /**
     * 默认构造函数，初始化一个空的密码段列表。
     */
    public PasswordOptions() {
        this.passwordSegments = new ArrayList<>();
    }

    /**
     * 从用户输入（通过扫描仪）构造 PasswordOptions，用于命令行界面。
     *
     * @param scanner 用于读取用户输入的扫描仪。
     */
    public PasswordOptions(Scanner scanner) {
        this.numberOfPasswords = UserInputValidator.getInt(scanner, "请输入要生成的密码总数：", 1, 100);
        this.numberOfSegments = UserInputValidator.getInt(scanner, "请输入密码段数：", 1, 10);

        this.passwordSegments = new ArrayList<>();
        for (int i = 0; i < numberOfSegments; i++) {
            System.out.println("\n--- 配置第 " + (i + 1) + " 个密码段 ---");
            this.passwordSegments.add(new PasswordSegmentOptions(scanner));
        }

        this.saveToFile = UserInputValidator.getYesNo(scanner, "\n是否需要保存生成的密码到 TXT 文件？ (y/n): ");
        if (this.saveToFile) {
            this.savePath = UserInputValidator.getString(scanner, "请输入保存路径 (可选): ");
        }
    }

    // Getters and Setters

    public int getNumberOfPasswords() {
        return numberOfPasswords;
    }

    public void setNumberOfPasswords(int numberOfPasswords) {
        this.numberOfPasswords = numberOfPasswords;
    }

    public int getNumberOfSegments() {
        return numberOfSegments;
    }

    public void setNumberOfSegments(int numberOfSegments) {
        this.numberOfSegments = numberOfSegments;
    }

    public List<PasswordSegmentOptions> getPasswordSegments() {
        return passwordSegments;
    }

    public void setPasswordSegments(List<PasswordSegmentOptions> passwordSegments) {
        this.passwordSegments = passwordSegments;
    }

    public boolean isSaveToFile() {
        return saveToFile;
    }

    public void setSaveToFile(boolean saveToFile) {
        this.saveToFile = saveToFile;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
}