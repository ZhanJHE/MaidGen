package com.Github.ZhanJHE.MaidGen.util;

import java.util.Scanner;

/**
 * 用户输入校验器
 */
public class UserInputValidator {

    /**
     * 获取并验证密码段数
     *
     * @param scanner Scanner 对象
     * @return 合法的密码段数
     */
    public static int getNumberOfSegments(Scanner scanner) {
        int numberOfSegments;
        while (true) {
            System.out.print("请输入密码段数 (1-9): ");
            if (scanner.hasNextInt()) {
                numberOfSegments = scanner.nextInt();
                if (numberOfSegments >= 1 && numberOfSegments <= 9) {
                    return numberOfSegments;
                } else {
                    System.out.println("密码段数必须在 1 到 9 之间，请重新输入。");
                }
            } else {
                System.out.println("无效输入，请输入一个整数。");
                scanner.next(); // 消费掉无效的输入
            }
        }
    }
}