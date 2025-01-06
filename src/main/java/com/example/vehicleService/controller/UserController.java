 package com.example.vehicleService.controller;

 import com.example.vehicleService.dto.ResponseMessage;
 import com.example.vehicleService.service.UserService;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;
 import org.springframework.web.multipart.MultipartFile;

 import java.time.LocalDate;
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

     @GetMapping("/get-by-date/{date}")
     public ResponseEntity<?> getByMonth(@PathVariable String date){
         LocalDate localDate = LocalDate.parse(date);
         return ResponseEntity.ok(userService.getUserByDate(localDate));
     }


     @PutMapping("/lock/{id}")
     public ResponseEntity<?> lockUser(@PathVariable Integer id){
         userService.lockAccount(id);
         return ResponseEntity.ok(new ResponseMessage("Khóa tài khoản thành công", LocalDateTime.now()));
     }

     @PutMapping("/unlock/{id}")
     public ResponseEntity<?> unlockUser(@PathVariable Integer id){
         userService.unlockAccount(id);
         return ResponseEntity.ok(new ResponseMessage("Mở khóa tài khoản thành công", LocalDateTime.now()));
     }

     @GetMapping("/count")
     public ResponseEntity<?> count(){
         return ResponseEntity.ok(userService.count());
     }
 }
