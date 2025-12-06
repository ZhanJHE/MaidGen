package com.Github.ZhanJHE.MaidGen.repository.impl;

import com.Github.ZhanJHE.MaidGen.repository.PasswordRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Repository
public class PasswordRepositoryImpl implements PasswordRepository {

    @Override
    public void savePasswords(List<String> passwords, String filePath) {
        try {
            Files.write(Paths.get(filePath), passwords);
        } catch (IOException e) {
            System.err.println("保存密码到文件时出错: " + e.getMessage());
        }
    }
}