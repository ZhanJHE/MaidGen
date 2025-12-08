package com.Github.ZhanJHE.MaidGen.repository;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 仓库类，负责将生成的密码列表保存到文件中。
 */
@Repository
public class PasswordRepository {

    /**
     * 将密码列表保存到指定的文件路径。如果提供的路径无效或不可写，它将尝试保存到带有时间戳的默认文件名。
     *
     * @param passwords 要保存的密码列表。
     * @param filePath 保存密码文件的目标文件路径。
     */
    public void savePasswords(List<String> passwords, String filePath) {
        try {
            Path path = Paths.get(filePath);
            // 确保父目录存在。这可以防止一些IOExceptions。
            Path parentDir = path.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }
            Files.write(path, passwords);
        } catch (IOException | InvalidPathException e) {
            System.err.println("警告: 指定的路径 '" + filePath + "' 不可用。原因: " + e.getMessage());
            System.out.println("正在尝试保存到默认路径...");

            // 回退逻辑
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String defaultFileName = "Passwords" + passwords.size() + "_" + timeStamp + ".txt";
                
                // 保存在当前工作目录中
                Path defaultPath = Paths.get(defaultFileName);

                Files.write(defaultPath, passwords);
                System.out.println("密码已成功保存到默认文件: " + defaultPath.toAbsolutePath());

            } catch (IOException ex) {
                System.err.println("错误: 无法保存到默认路径。原因: " + ex.getMessage());
            }
        }
    }
}