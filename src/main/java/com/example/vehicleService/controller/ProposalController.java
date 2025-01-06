package com.example.vehicleService.controller;

import com.example.vehicleService.dto.ProposalDTO;
import com.example.vehicleService.dto.ResponseMessage;
import com.example.vehicleService.entity.Proposal;
import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.service.LiveTrackingSubcriber;
import com.example.vehicleService.service.NotificationService;
import com.example.vehicleService.service.ProposalService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/proposals")
public class ProposalController {
    private final ProposalService proposalService;
    private final NotificationService notificationService;
    private final LiveTrackingSubcriber liveTrackingSubcriber;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ProposalController(ProposalService proposalService, NotificationService notificationService, LiveTrackingSubcriber liveTrackingSubcriber, SimpMessagingTemplate simpMessagingTemplate) {
        this.proposalService = proposalService;
        this.notificationService = notificationService;
        this.liveTrackingSubcriber = liveTrackingSubcriber;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        return ResponseEntity.ok(proposalService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllPagination(Pageable pageable){
        return ResponseEntity.ok(proposalService.getAllPagination(pageable));
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody ProposalDTO proposalDTO){
        Proposal proposal = proposalService.save(proposalDTO);
        simpMessagingTemplate.convertAndSendToUser(proposal.getEmergencyRequest().getCustomer().getUser().getUsername(), "/queue/proposal", "PROPOSAL: " + proposal.getId() + " FROM SHOP: " + proposal.getShop().getName());
        System.out.println("ỉuhfoidjfhueiide");
        return ResponseEntity.ok(proposal);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody ProposalDTO proposalDTO){
        return ResponseEntity.ok(proposalService.save(proposalDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> acceptProposal(@PathVariable Integer id){
        Proposal proposal = proposalService.acceptProposal(id);
//        simpMessagingTemplate.convertAndSendToUser(proposal.getShop().getUser().getUsername(), "/queue/proposal",
//                "ACCEPTED_PROPOSAL: " + proposal.getId() + " FOR EMERGENCY_REQUEST: " + proposal.getEmergencyRequest().getId());
        return ResponseEntity.ok(new ResponseMessage("Accept proposal successfully!", LocalDateTime.now()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        proposalService.deleteById(id);
        return ResponseEntity.ok(new ResponseMessage("Delete Proposal successfully!", LocalDateTime.now()));
    }

    @GetMapping("emergency-request-{erId}")
    public ResponseEntity<?> getByEmergencyRequest(@PathVariable Integer erId){
        return ResponseEntity.ok(proposalService.getByEmergencyRequest(erId));
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestParam("status") Status status){
        proposalService.updateStatus(status, id);
        return ResponseEntity.ok(new ResponseMessage("Cập nhật thành công", LocalDateTime.now()));
    }
}
