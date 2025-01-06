package com.example.vehicleService.controller;

import com.example.vehicleService.dto.EmergencyRequestDTO;
import com.example.vehicleService.dto.ResponseMessage;
import com.example.vehicleService.entity.EmergencyRequest;
import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.repository.ShopRepository;
import com.example.vehicleService.service.EmergencyRequestService;
import com.example.vehicleService.service.ShopService;
import com.example.vehicleService.service.impl.SmsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/emergency-requests")
public class EmergencyRequestController {
    private final EmergencyRequestService emergencyRequestService;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SmsService smsService;
    private final ShopRepository shopRepository;

    public EmergencyRequestController(EmergencyRequestService emergencyRequestService, ObjectMapper objectMapper, SimpMessagingTemplate simpMessagingTemplate, SmsService smsService, ShopRepository shopRepository) {
        this.emergencyRequestService = emergencyRequestService;
        this.objectMapper = objectMapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.smsService = smsService;
        this.shopRepository = shopRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllPagination(Pageable pageable){
        return ResponseEntity.ok(emergencyRequestService.getAllPagination(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        return ResponseEntity.ok(emergencyRequestService.getById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> add(@RequestPart(value = "image1") MultipartFile image1, @RequestPart(value = "image2", required = false) MultipartFile image2, @RequestPart(value = "image3", required = false) MultipartFile image3, @RequestPart EmergencyRequestDTO emergencyRequestDTO) {
        EmergencyRequest request = emergencyRequestService.save(image1, image2, image3, emergencyRequestDTO);
//        redisPubSubService.publishMessage("emergency-request",  "EMERGENCY_REQUEST: " + request.getId());
//        List<Shop> shopList = shopRepository.findAll();
//        String[] phoneList =  new String[shopList.size()];
//        int i = 0;
//        for (Shop x : shopList){
//            phoneList[i++] = x.getPhoneNumber();
//        }
//        smsService.sendBulkSms(phoneList, "Có cứu trợ khẩn cấp mới. Vào hệ thống để xem ngay!");
        simpMessagingTemplate.convertAndSend("/topic/emergency-request", "EMERGENCY_REQUEST: " + request.getId());
        return ResponseEntity.ok(request);
    }

//    @PutMapping
//    public ResponseEntity<?> update(@Valid @RequestBody EmergencyRequestDTO emergencyRequestDTO){
//        return ResponseEntity.ok(emergencyRequestService.save(emergencyRequestDTO));
//    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        emergencyRequestService.deleteById(id);
        return ResponseEntity.ok(new ResponseMessage("Delete Emergency Request successfully!", LocalDateTime.now()));
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestParam("status") Status status){
        emergencyRequestService.updateStatus(status, id);
        return ResponseEntity.ok(new ResponseMessage("Cập nhật thành công", LocalDateTime.now()));
    }

    @GetMapping("count-by-shop/{date}")
    public ResponseEntity<?> countByDateAndCurrentShop(@PathVariable String date){
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(emergencyRequestService.countByDateAndCurrentShop(localDate));
    }

    @GetMapping("count/{date}")
    public ResponseEntity<?> countByDate(@PathVariable String date){
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(emergencyRequestService.countByDate(localDate));
    }

    @GetMapping("/current-customer")
    public ResponseEntity<?> getByCurrentCustomer(){
        return ResponseEntity.ok(emergencyRequestService.getByCustomer());
    }
    @GetMapping("count")
    public ResponseEntity<?> count(){
        return ResponseEntity.ok(emergencyRequestService.count());
    }
    @GetMapping("count-by-shop")
    public ResponseEntity<?> countByCurrentShop(){
        return ResponseEntity.ok(emergencyRequestService.countByCurrentShop());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EmergencyRequest>> searchEmergencyRequests(
            @RequestParam(value = "searchTerm") String searchTerm,
            Pageable pageable) {

        Page<EmergencyRequest> emergencyRequestPage = emergencyRequestService.searchEmergencyRequests(searchTerm, pageable);
        return ResponseEntity.ok(emergencyRequestPage);
    }
}
