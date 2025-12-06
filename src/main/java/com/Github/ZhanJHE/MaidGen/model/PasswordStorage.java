package com.Github.ZhanJHE.MaidGen.model;

import java.util.LinkedList;
import java.util.List;

public class PasswordStorage {

    private final List<String> passwords = new LinkedList<>();

    public void addPassword(String password) {
        passwords.add(password);
    }

    public List<String> getPasswords() {
        return passwords;
    }
}