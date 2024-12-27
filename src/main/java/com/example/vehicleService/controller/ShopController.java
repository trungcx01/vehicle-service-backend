package com.example.vehicleService.controller;

import com.example.vehicleService.dto.ResponseMessage;
import com.example.vehicleService.dto.ShopDTO;
import com.example.vehicleService.entity.Proposal;
import com.example.vehicleService.service.ProposalService;
import com.example.vehicleService.service.ShopService;
import com.example.vehicleService.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/shops")
public class ShopController {
    private final ShopService shopService;
    private final UserService userService;
    private final ProposalService proposalService;

    public ShopController(ShopService shopService, UserService userService, ProposalService proposalService) {
        this.shopService = shopService;
        this.userService = userService;
        this.proposalService = proposalService;
    }

    @GetMapping
    public ResponseEntity<?> getAllShops(Pageable pageable){
        return ResponseEntity.ok(shopService.getAllPagination(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getShopById(@PathVariable Long id){
        return ResponseEntity.ok(shopService.getById(id));
    }

    @PostMapping
    public ResponseEntity<?> addShop(@RequestBody ShopDTO shopDTO){
        return ResponseEntity.ok(shopService.save(shopDTO));
    }

    @PutMapping
    public ResponseEntity<?> updateShop(@RequestBody ShopDTO shopDTO){
        return ResponseEntity.ok(shopService.save(shopDTO));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteShop(@PathVariable Long id){
        shopService.delete(id);
        return ResponseEntity.ok(new ResponseMessage("Delete shop successfully!", LocalDateTime.now()));
    }

    @GetMapping("/top6-rating")
    public ResponseEntity<?> top6Rating(){
        return ResponseEntity.ok(shopService.findTop6ByOrderByRatingDesc());
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentShop(){
        return ResponseEntity.ok(shopService.getCurrentShop());
    }

    @PutMapping("/update-info")
    @Transactional
    public ResponseEntity<?> updateInfo(@RequestParam("avatar")MultipartFile avatar, @RequestBody ShopDTO shopDTO){
        userService.updateAvatar(avatar, null);
        shopService.save(shopDTO);
        return ResponseEntity.ok(new ResponseMessage("Cập nhật thông tin thành công", LocalDateTime.now()));
    }

    @GetMapping("check-send-proposal/{erId}")
    public ResponseEntity<?> checkSendProposal(@PathVariable Long erId){
        Proposal proposal = proposalService.checkSendProposal(erId);
        return ResponseEntity.ok(proposal);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByName(@RequestParam("name") String name, @RequestParam("district") String district){
        return ResponseEntity.ok(shopService.search(name, district));
    }
}
