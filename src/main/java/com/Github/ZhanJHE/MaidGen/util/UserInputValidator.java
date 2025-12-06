package com.Github.ZhanJHE.MaidGen.util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInputValidator {

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

    public static String getString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.next();
    }
}