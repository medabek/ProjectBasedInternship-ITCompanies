package com.ourFinalProject.turizm.service;

import com.ourFinalProject.turizm.model.User;

public interface UserService {
    public User findUserByEmail(String email);
    public void saveUser(User user);
}