package com.Github.ZhanJHE.MaidGen.repository;

import java.util.List;

public interface PasswordRepository {
    void savePasswords(List<String> passwords, String filePath);
}