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

@Repository
public class PasswordRepository {
    public void savePasswords(List<String> passwords, String filePath) {
        try {
            Path path = Paths.get(filePath);
            // Ensure parent directories exist. This can prevent some IOExceptions.
            Path parentDir = path.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }
            Files.write(path, passwords);
        } catch (IOException | InvalidPathException e) {
            System.err.println("警告: 指定的路径 '" + filePath + "' 不可用。原因: " + e.getMessage());
            System.out.println("正在尝试保存到默认路径...");

            // Fallback logic
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String defaultFileName = "Passwords" + passwords.size() + "_" + timeStamp + ".txt";
                
                // Save in the current working directory
                Path defaultPath = Paths.get(defaultFileName);

                Files.write(defaultPath, passwords);
                System.out.println("密码已成功保存到默认文件: " + defaultPath.toAbsolutePath());

            } catch (IOException ex) {
                System.err.println("错误: 无法保存到默认路径。原因: " + ex.getMessage());
            }
        }
    }
}