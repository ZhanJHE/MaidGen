package com.Github.ZhanJHE.MaidGen.model;

/**
 * 密码生成选项
 */
public class PasswordOptions {

    private int length;
    private boolean useUpperCase;
    private boolean useLowerCase;
    private boolean useNumbers;
    private boolean useSpecialCharacters;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isUseUpperCase() {
        return useUpperCase;
    }

    public void setUseUpperCase(boolean useUpperCase) {
        this.useUpperCase = useUpperCase;
    }

    public boolean isUseLowerCase() {
        return useLowerCase;
    }

    public void setUseLowerCase(boolean useLowerCase) {
        this.useLowerCase = useLowerCase;
    }

    public boolean isUseNumbers() {
        return useNumbers;
    }

    public void setUseNumbers(boolean useNumbers) {
        this.useNumbers = useNumbers;
    }

    public boolean isUseSpecialCharacters() {
        return useSpecialCharacters;
    }

    public void setUseSpecialCharacters(boolean useSpecialCharacters) {
        this.useSpecialCharacters = useSpecialCharacters;
    }
}