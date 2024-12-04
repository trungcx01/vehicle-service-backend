package com.example.vehicleService.controller;

import com.example.vehicleService.dto.EmergencyRequestDTO;
import com.example.vehicleService.dto.ResponseMessage;
import com.example.vehicleService.entity.EmergencyRequest;
import com.example.vehicleService.service.EmergencyRequestService;
import com.example.vehicleService.service.NotificationService;
import com.example.vehicleService.service.RedisPubSubService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/emergency-requests")
public class EmergencyRequestController {
    private final EmergencyRequestService emergencyRequestService;
    private final RedisPubSubService redisPubSubService;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public EmergencyRequestController(EmergencyRequestService emergencyRequestService, RedisPubSubService redisPubSubService, ObjectMapper objectMapper, SimpMessagingTemplate simpMessagingTemplate) {
        this.emergencyRequestService = emergencyRequestService;
        this.redisPubSubService = redisPubSubService;
        this.objectMapper = objectMapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping
    public ResponseEntity<?> getAllPagination(Pageable pageable){
        return ResponseEntity.ok(emergencyRequestService.getAllPagination(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return ResponseEntity.ok(emergencyRequestService.getById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> add(@RequestPart(value = "image1") MultipartFile image1, @RequestPart(value = "image2", required = false) MultipartFile image2, @RequestPart(value = "image3", required = false) MultipartFile image3, @RequestPart EmergencyRequestDTO emergencyRequestDTO) {
        EmergencyRequest request = emergencyRequestService.save(image1, image2, image3, emergencyRequestDTO);
//        redisPubSubService.publishMessage("emergency-request",  "EMERGENCY_REQUEST: " + request.getId());
        simpMessagingTemplate.convertAndSend("/topic/emergency-request", "EMERGENCY_REQUEST: " + request.getId());
        return ResponseEntity.ok(request);
    }

//    @PutMapping
//    public ResponseEntity<?> update(@Valid @RequestBody EmergencyRequestDTO emergencyRequestDTO){
//        return ResponseEntity.ok(emergencyRequestService.save(emergencyRequestDTO));
//    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        emergencyRequestService.deleteById(id);
        return ResponseEntity.ok(new ResponseMessage("Delete Emergency Request successfully!", LocalDateTime.now()));
    }
}
