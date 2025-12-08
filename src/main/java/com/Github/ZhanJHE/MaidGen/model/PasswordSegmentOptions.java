package com.Github.ZhanJHE.MaidGen.model;

import com.Github.ZhanJHE.MaidGen.model.enums.DataSource;
import com.Github.ZhanJHE.MaidGen.model.enums.WordCase;
import com.Github.ZhanJHE.MaidGen.util.UserInputValidator;

import java.util.Scanner;

/**
 * 表示密码中单个段的配置选项。
 * 这包括数据源、大小写转换、长度以及任何特定于数据源的设置（例如文件路径或自定义字符串）。
 */
public class PasswordSegmentOptions {

    private DataSource dataSource; // 该段的数据源（例如，文件、随机单词）
    private String filePath; // 可选：外部文件的路径
    private String customField; // 可选：用户定义的自定义字符串
    private WordCase wordCase; // 单词的大小写格式（例如，大写、小写）
    private int length; // 该段的长度

    /**
     * 默认构造函数。
     */
    public PasswordSegmentOptions() {
    }

    /**
     * 从用户输入（通过扫描仪）构造 PasswordSegmentOptions，用于命令行界面。
     *
     * @param scanner 用于读取用户输入的扫描仪。
     */
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

    /**
     * 返回此密码段配置的字符串表示形式。
     *
     * @return 包含此段配置摘要的字符串。
     */
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