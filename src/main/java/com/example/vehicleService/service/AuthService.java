package com.example.vehicleService.service;

import com.example.vehicleService.dto.EmailDetail;
import com.example.vehicleService.dto.LoginDTO;
import com.example.vehicleService.dto.UserRegisterDTO;
import com.example.vehicleService.entity.User;

import java.util.Optional;

public interface AuthService {
    String login(LoginDTO loginDTO);
    User signUp(UserRegisterDTO userRegisterDTO);
    User resetActivationKey();
    User activateUser(String key);
    User resetResetKey(String email);
    User resetPassword(String key);
    User getCurrentUser();

    void changePassword(String oldPassword, String newPassword);
}
