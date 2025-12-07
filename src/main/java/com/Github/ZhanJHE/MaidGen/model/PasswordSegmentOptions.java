package com.Github.ZhanJHE.MaidGen.model;

import com.Github.ZhanJHE.MaidGen.model.enums.DataSource;
import com.Github.ZhanJHE.MaidGen.model.enums.WordCase;
import com.Github.ZhanJHE.MaidGen.util.UserInputValidator;

import java.util.Scanner;

public class PasswordSegmentOptions {

    private DataSource dataSource;
    private String filePath; // Optional: Path to external file
    private String customField; // Optional: Custom user-defined string
    private WordCase wordCase;
    private int length;

    public PasswordSegmentOptions() {
    }

    public PasswordSegmentOptions(Scanner scanner) {
        // 数据来源
        int dataSourceChoice = UserInputValidator.getInt(scanner, "请选择数据类型 (1: 来源于自定义文件, 2: 随机单词（内置）, 3: 随机标点符号, 4: 随机数字串, 5: 指定字符串): ", 1, 5);
        this.dataSource = DataSource.values()[dataSourceChoice - 1];

        // 根据数据来源获取其他选项
        if (this.dataSource == DataSource.EXTERNAL_FILE) {
            this.filePath = UserInputValidator.getString(scanner, "请输入自定义文件的路径: ");
        } else if (this.dataSource == DataSource.CUSTOM_FIELD) {
            this.customField = UserInputValidator.getString(scanner, "请输入指定字符串: ");
        }

        if (this.dataSource == DataSource.EXTERNAL_FILE || this.dataSource == DataSource.WORD_LIST_JSON) {
            int wordCaseChoice = UserInputValidator.getInt(scanner, "请选择英文单词的表现形式 (1: 全大写, 2: 首字母大写, 3: 全小写): ", 1, 3);
            this.wordCase = WordCase.values()[wordCaseChoice - 1];
        }

        if (this.dataSource == DataSource.CUSTOM_FIELD) {
            this.length = this.customField.length();
        } else if (this.dataSource == DataSource.WORD_LIST_JSON) {
            this.length = UserInputValidator.getInt(scanner, "请输入密码段长度 (3-9): ", 3, 9);
        } else {
            this.length = UserInputValidator.getInt(scanner, "请输入密码段长度: ", 1, 50);
        }

        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- 密码段配置概要 ---\n");

        String dataSourceStr = "";
        switch (dataSource) {
            case EXTERNAL_FILE: dataSourceStr = "来源于自定义文件"; break;
            case WORD_LIST_JSON: dataSourceStr = "随机单词（内置）"; break;
            case RANDOM_PUNCTUATION: dataSourceStr = "随机标点符号"; break;
            case RANDOM_NUMBERS: dataSourceStr = "随机数字串"; break;
            case CUSTOM_FIELD: dataSourceStr = "指定字符串"; break;
        }
        sb.append("  数据类型: ").append(dataSourceStr).append("\n");

        if (dataSource == DataSource.EXTERNAL_FILE) {
            sb.append("  文件路径: ").append(filePath).append("\n");
        }
        if (dataSource == DataSource.CUSTOM_FIELD) {
            sb.append("  指定字符串: '").append(customField).append("'\n");
        }

        if (dataSource == DataSource.EXTERNAL_FILE || dataSource == DataSource.WORD_LIST_JSON) {
            String wordCaseStr = "";
            if (wordCase != null) {
                switch (wordCase) {
                    case UPPERCASE: wordCaseStr = "全大写"; break;
                    case CAPITALIZE: wordCaseStr = "首字母大写"; break;
                    case LOWERCASE: wordCaseStr = "全小写"; break;
                }
                sb.append("  单词形式: ").append(wordCaseStr).append("\n");
            }
        }

        if (dataSource != DataSource.CUSTOM_FIELD) {
             sb.append("  密码段长度: ").append(length).append("\n");
        }

        sb.append("----------------------\n");
        return sb.toString();
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

    public String getCustomField() {
        return customField;
    }

    public void setCustomField(String customField) {
        this.customField = customField;
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