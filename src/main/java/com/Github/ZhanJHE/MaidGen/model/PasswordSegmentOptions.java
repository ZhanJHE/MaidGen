package com.Github.ZhanJHE.MaidGen.model;

import com.Github.ZhanJHE.MaidGen.model.enums.DataSource;
import com.Github.ZhanJHE.MaidGen.model.enums.WordCase;
import com.Github.ZhanJHE.MaidGen.util.UserInputValidator;

import java.util.Scanner;

public class PasswordSegmentOptions {

    private DataSource dataSource;
    private String filePath; // Optional: Path to external file
    private WordCase wordCase;
    private int length;

    public PasswordSegmentOptions() {
    }

    public PasswordSegmentOptions(Scanner scanner) {
        // 数据来源
        int dataSourceChoice = UserInputValidator.getInt(scanner, "请选择数据来源 (1: 外部文件, 2: 单词表JSON, 3: 随机标点符号, 4: 一串数字): ", 1, 4);
        this.dataSource = DataSource.values()[dataSourceChoice - 1];

        // 根据数据来源获取其他选项
        if (this.dataSource == DataSource.EXTERNAL_FILE) {
            this.filePath = UserInputValidator.getString(scanner, "请输入外部文件的路径: ");
        }

        if (this.dataSource == DataSource.EXTERNAL_FILE || this.dataSource == DataSource.WORD_LIST_JSON) {
            int wordCaseChoice = UserInputValidator.getInt(scanner, "请选择英文单词的表现形式 (1: 全大写, 2: 首字母大写, 3: 全小写): ", 1, 3);
            this.wordCase = WordCase.values()[wordCaseChoice - 1];
        }

        this.length = UserInputValidator.getInt(scanner, "请输入密码段长度: ", 1, 50);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public WordCase getWordCase() {
        return wordCase;
    }

    public void setWordCase(WordCase wordCase) {
        this.wordCase = wordCase;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}