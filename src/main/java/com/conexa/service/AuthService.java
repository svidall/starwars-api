package com.conexa.service;

public interface AuthService {
    boolean login(String username, String password);
    void register(String username, String password);
}
