package com.example.vehicleService.service;

import com.example.vehicleService.dto.PaymentDTO;
import com.example.vehicleService.dto.ProposalDTO;
import com.example.vehicleService.entity.Payment;
import com.example.vehicleService.entity.Proposal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProposalService {
    Proposal getById(Long id);
    Page<Proposal> getAllPagination(Pageable pageable);
    Proposal save(ProposalDTO proposalDTO);
    void deleteById(Long id);
    List<Proposal> getByEmergencyRequest(Long emergencyRequestId);
    Proposal acceptProposal(Long proposalId);
    Proposal checkSendProposal(Long emergencyRequestId);
}
