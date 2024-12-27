 package com.example.vehicleService.controller;

 import com.example.vehicleService.dto.ResponseMessage;
 import com.example.vehicleService.service.UserService;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;
 import org.springframework.web.multipart.MultipartFile;

 import java.time.LocalDateTime;

 @RestController
 @RequestMapping("api/users")
 public class UserController {
     private final UserService userService;

     public UserController(UserService userService) {
         this.userService = userService;
     }

     @PutMapping("/update-avatar")
     public ResponseEntity<?> updateAvatar(@RequestParam("avatar") MultipartFile avatar){
         return ResponseEntity.ok(userService.updateAvatar(avatar, null));
     }

     @GetMapping("/get-by-month/{month}")
     public ResponseEntity<?> getByMonth(@PathVariable String month){
         String[] split= month.split("-");
         return ResponseEntity.ok(userService.getUserByMonth(Long.valueOf(split[0]), Long.valueOf(split[1])));
     }


     @PutMapping("/lock/{id}")
     public ResponseEntity<?> lockUser(@PathVariable Long id){
         userService.lockAccount(id);
         return ResponseEntity.ok(new ResponseMessage("Khóa tài khoản thành công", LocalDateTime.now()));
     }

     @PutMapping("/unlock/{id}")
     public ResponseEntity<?> unlockUser(@PathVariable Long id){
         userService.unlockAccount(id);
         return ResponseEntity.ok(new ResponseMessage("Mở khóa tài khoản thành công", LocalDateTime.now()));
     }
 }
