package com.example.vehicleService.service.impl;

import com.example.vehicleService.dto.ProposalDTO;
import com.example.vehicleService.entity.EmergencyRequest;
import com.example.vehicleService.entity.Proposal;
import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.exception.BlogAPIException;
import com.example.vehicleService.mapper.ProposalMapper;
import com.example.vehicleService.repository.EmergencyRequestRepository;
import com.example.vehicleService.repository.ProposalRepository;
import com.example.vehicleService.repository.ShopRepository;
import com.example.vehicleService.service.ProposalService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProposalServiceImpl implements ProposalService {
    private final ProposalRepository proposalRepository;
    private final ProposalMapper proposalMapper;
    private final ShopRepository shopRepository;
    private final EmergencyRequestRepository emergencyRequestRepository;

    public ProposalServiceImpl(ProposalRepository proposalRepository, ProposalMapper proposalMapper, ShopRepository shopRepository, EmergencyRequestRepository emergencyRequestRepository) {
        this.proposalRepository = proposalRepository;
        this.proposalMapper = proposalMapper;
        this.shopRepository = shopRepository;
        this.emergencyRequestRepository = emergencyRequestRepository;
    }

    @Override
    public Proposal getById(Long id) {
        return proposalRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Proposal!")
        );
    }

    @Override
    public Page<Proposal> getAllPagination(Pageable pageable) {
        return proposalRepository.findAll(pageable);
    }

    @Override
    public Proposal save(ProposalDTO proposalDTO) {
        Proposal proposal = proposalMapper.toEntity(proposalDTO, shopRepository, emergencyRequestRepository);
        return proposalRepository.save(proposal);
    }

    @Override
    public void deleteById(Long id) {
        Proposal proposal = proposalRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Proposal!")
        );
        proposalRepository.delete(proposal);
    }

    @Override
    public List<Proposal> getByEmergencyRequest(Long emergencyRequestId) {
        return proposalRepository.findByEmergencyRequestId(emergencyRequestId);
    }

    @Override
    @Transactional
    public Proposal acceptProposal(Long proposalId) {
        Proposal proposal = proposalRepository.findById(proposalId).orElseThrow(
                () -> new EntityNotFoundException("Not Found Proposal!")
        );

        List<Proposal> allProposalsOfRequest = proposalRepository.findByEmergencyRequestId(proposal.getEmergencyRequest().getId());

        for (Proposal x : allProposalsOfRequest){
            if (x.getStatus() != Status.ACCEPTED) {
                x.setStatus(Status.DECLINED);
            }
        }

        proposalRepository.saveAll(allProposalsOfRequest);

        proposalRepository.flush();

        proposal.setStatus(Status.ACCEPTED);
        proposal = proposalRepository.save(proposal);

        EmergencyRequest emergencyRequest = proposal.getEmergencyRequest();
        emergencyRequest.setRequestStatus(Status.ACCEPTED);
        emergencyRequestRepository.save(emergencyRequest);

        emergencyRequestRepository.flush();

        return proposal;
    }


    @Override
    public Proposal checkSendProposal(Long emergencyRequestId) {
        EmergencyRequest emergencyRequest = emergencyRequestRepository.findById(emergencyRequestId).orElseThrow(
                () -> new EntityNotFoundException("Not found ER")
        );
        Shop shop = shopRepository.findByUserUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Proposal proposal = proposalRepository.findByShopAndEmergencyRequest(shop, emergencyRequest);
        if (proposal == null){
            throw new BlogAPIException(HttpStatus.NOT_FOUND, "Không tìm thấy proposal nào!");
        }
        return proposal;
    }

    @Override
    public void updateStatus(Status status, Long id) {
        Proposal proposal = proposalRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found proposal!")
        );
        proposal.setStatus(status);
        proposalRepository.save(proposal);
    }
}
