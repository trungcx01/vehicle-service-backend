package com.example.vehicleService.controller;

import com.example.vehicleService.dto.*;
import com.example.vehicleService.entity.User;
import com.example.vehicleService.exception.BlogAPIException;
import com.example.vehicleService.service.AuthService;
import com.example.vehicleService.service.MailService;
import com.example.vehicleService.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    private final MailService mailService;
    private final NotificationService notificationService;

    public AuthController(AuthService authService, MailService mailService, NotificationService notificationService) {
        this.authService = authService;
        this.mailService = mailService;
        this.notificationService = notificationService;
    }

    @PostMapping("auth/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO){
        String token = authService.login(loginDTO);
//        notificationService.sendNotification("/shop/login", token);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    @PostMapping("auth/signup")
    public ResponseEntity<?> signUp(@RequestBody UserRegisterDTO userRegisterDTO){
        User user = authService.signUp(userRegisterDTO);
        mailService.sendTextMail(new EmailDetail(user.getEmail(),
                "Activate your account!", "Your activate code: " + user.getActivationKey()));
        return ResponseEntity.ok(new ResponseMessage("Sign up user successfully!", LocalDateTime.now()));
    }

    @PostMapping("auth/activated")
    public ResponseEntity<?> activatedUser(@RequestParam("key") String key){
        User user = authService.activateUser(key);
        if (user == null){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Fail to activate your account!");
        }
        return ResponseEntity.ok(new ResponseMessage("Activate your account successfully!", LocalDateTime.now()));
    }

    @PostMapping("request-activation")
    public ResponseEntity<?> requestActivation(){
        User user = authService.resetActivationKey();
        mailService.sendTextMail(new EmailDetail(user.getEmail(),
                "Activate your account!", "Your activate code: " + user.getActivationKey()));
        return ResponseEntity.ok(new ResponseMessage("Sent activation key to" + user.getEmail() + " successfully!", LocalDateTime.now()));
    }

    @PostMapping("request-reset-password")
    public ResponseEntity<?> requestResetPassword(@RequestParam String email){
        User user = authService.sendResetKey(email);
        mailService.sendTextMail(new EmailDetail(user.getEmail(),
                "Reset password for your account!", "Your reset code: " + user.getResetKey()));
        return ResponseEntity.ok(new ResponseMessage("Sent reset key to" + user.getEmail() + " successfully!", LocalDateTime.now()));
    }

    @PostMapping("reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO){
        User user = authService.resetPassword(forgotPasswordDTO);
        if (user == null){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Fail to reset password!");
        }
        return ResponseEntity.ok(new ResponseMessage("Reset password successfully!", LocalDateTime.now()));
    }

    @PostMapping("change-password")
    public ResponseEntity<?> changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword){
        authService.changePassword(oldPassword, newPassword);
        return ResponseEntity.ok(new ResponseMessage("Change password successfully!", LocalDateTime.now()));
    }

    @GetMapping("get-user")
    public ResponseEntity<?> getUser(){
        return ResponseEntity.ok(authService.getCurrentUser());
    }
}
