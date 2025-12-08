package com.Github.ZhanJHE.MaidGen.util;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 提供静态方法来验证和获取用户输入。
 */
public class UserInputValidator {

    /**
     * 提示用户输入一个在指定范围内的整数。
     *
     * @param scanner 用于读取用户输入的扫描仪。
     * @param prompt  显示给用户的提示信息。
     * @param min     允许的最小值（含）。
     * @param max     允许的最大值（含）。
     * @return 验证后的整数。
     */
    public static int getInt(Scanner scanner, String prompt, int min, int max) {
        int value = 0;
        while (true) {
            try {
                System.out.print(prompt);
                value = scanner.nextInt();
                if (value >= min && value <= max) {
                    break;
                } else {
                    System.out.println("输入无效，请输入 " + min + " 和 " + max + " 之间的一个数字。");
                }
            } catch (InputMismatchException e) {
                System.out.println("输入无效，请输入一个整数。");
                scanner.next(); // 清除无效输入
            }
        }
        return value;
    }

    /**
     * 提示用户输入“y”或“n”以获得布尔值响应。
     *
     * @param scanner 用于读取用户输入的扫描仪。
     * @param prompt  显示给用户的提示信息。
     * @return 如果用户输入“y”则为 true，如果输入“n”则为 false。
     */
    public static boolean getYesNo(Scanner scanner, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.next().trim().toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                System.out.println("输入无效，请输入 'y' 或 'n'。");
            }
        }
    }

    /**
     * 提示用户输入一个字符串。
     *
     * @param scanner 用于读取用户输入的扫描仪。
     * @param prompt  显示给用户的提示信息。
     * @return 用户输入的字符串。
     */
    public static String getString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.next();
    }
}