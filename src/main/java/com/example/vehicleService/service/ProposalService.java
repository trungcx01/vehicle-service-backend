package com.example.vehicleService.service;

import com.example.vehicleService.dto.ProposalDTO;
import com.example.vehicleService.entity.Proposal;
import com.example.vehicleService.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProposalService {
    Proposal getById(Integer id);
    Page<Proposal> getAllPagination(Pageable pageable);
    Proposal save(ProposalDTO proposalDTO);
    void deleteById(Integer id);
    List<Proposal> getByEmergencyRequest(Integer emergencyRequestId);
    Proposal acceptProposal(Integer proposalId);
    Proposal checkSendProposal(Integer emergencyRequestId);
    void updateStatus(Status status, Integer id);
}
