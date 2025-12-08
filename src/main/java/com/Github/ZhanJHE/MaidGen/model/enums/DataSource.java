package com.Github.ZhanJHE.MaidGen.model.enums;

/**
 * 定义密码段的可用数据源类型。
 */
public enum DataSource {
    /**
     * 数据来源于用户提供的外部文本文件。
     */
    EXTERNAL_FILE,
    /**
     * 数据来源于内置的JSON格式的单词列表。
     */
    WORD_LIST_JSON,
    /**
     * 数据为随机生成的标点符号。
     */
    RANDOM_PUNCTUATION,
    /**
     * 数据为随机生成的数字字符串。
     */
    RANDOM_NUMBERS,
    /**
     * 数据来源于用户直接输入的自定义字符串。
     */
    CUSTOM_FIELD
}