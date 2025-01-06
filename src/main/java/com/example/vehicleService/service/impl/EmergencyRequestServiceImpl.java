package com.example.vehicleService.service.impl;

import com.example.vehicleService.dto.EmergencyRequestDTO;
import com.example.vehicleService.entity.EmergencyRequest;
import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.mapper.EmergencyRequestMapper;
import com.example.vehicleService.repository.CustomerRepository;
import com.example.vehicleService.repository.EmergencyRequestRepository;
import com.example.vehicleService.repository.ProposalRepository;
import com.example.vehicleService.repository.ShopRepository;
import com.example.vehicleService.service.CloudinaryService;
import com.example.vehicleService.service.EmergencyRequestService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class EmergencyRequestServiceImpl implements EmergencyRequestService {
    private final EmergencyRequestRepository emergencyRequestRepository;
    private final EmergencyRequestMapper emergencyRequestMapper;
    private final ProposalRepository proposalRepository;
    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;
    private final CloudinaryService cloudinaryService;

    public EmergencyRequestServiceImpl(EmergencyRequestRepository emergencyRequestRepository, EmergencyRequestMapper emergencyRequestMapper, ProposalRepository proposalRepository, CustomerRepository customerRepository, ShopRepository shopRepository, CloudinaryService cloudinaryService) {
        this.emergencyRequestRepository = emergencyRequestRepository;
        this.emergencyRequestMapper = emergencyRequestMapper;
        this.proposalRepository = proposalRepository;
        this.customerRepository = customerRepository;
        this.shopRepository = shopRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public EmergencyRequest getById(Integer id) {
        return emergencyRequestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Emergency Request!")
        );
    }

    @Override
    public Page<EmergencyRequest> getAllPagination(Pageable pageable) {
        return emergencyRequestRepository.findAll(pageable);
    }

    @Override
    public EmergencyRequest save(MultipartFile image1, MultipartFile image2, MultipartFile image3, EmergencyRequestDTO emergencyRequestDTO) {
        EmergencyRequest emergencyRequest = emergencyRequestMapper.toEntity(emergencyRequestDTO, proposalRepository, customerRepository);
        emergencyRequest = emergencyRequestRepository.save(emergencyRequest);
        String all = "";
        Map i1 = cloudinaryService.upload(image1, "emergency-request/" + emergencyRequest.getId());
        all += i1.get("secure_url") + " - ";

        if (image2 != null){
            Map i2 = cloudinaryService.upload(image2, "emergency-request/" + emergencyRequest.getId());
            all += i2.get("secure_url") + " - ";
        }
        if (image3 != null){
            Map i3 = cloudinaryService.upload(image3, "emergency-request/" + emergencyRequest.getId());
            all += i3.get("secure_url") + " - ";
        }
        all = all.substring(0, all.length() - 2);
        emergencyRequest.setImageDetail(all);
        return emergencyRequestRepository.save(emergencyRequest);
    }

    @Override
    public void deleteById(Integer id) {
        EmergencyRequest emergencyRequest = emergencyRequestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Emergency Request!")
        );
        emergencyRequestRepository.delete(emergencyRequest);
    }

    @Override
    public void updateStatus(Status status, Integer id) {
        EmergencyRequest emergencyRequest = emergencyRequestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Emergency request!")
        );
        emergencyRequest.setRequestStatus(status);
        emergencyRequestRepository.save(emergencyRequest);
    }

    @Override
    public Integer countByDate(LocalDate date) {
        return emergencyRequestRepository.countByDate(date);
    }

    @Override
    public Integer countByDateAndCurrentShop(LocalDate date) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Shop shop = shopRepository.findByUserUsername(username);
        return emergencyRequestRepository.countByDateAndCurrentShop(date, shop.getId());
    }

    @Override
    public List<EmergencyRequest> getByCustomer() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return emergencyRequestRepository.findByCustomerUserUsername(username);
    }

    @Override
    public long count() {
        return emergencyRequestRepository.count();
    }

    @Override
    public long countByCurrentShop() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Shop shop = shopRepository.findByUserUsername(username);
        return emergencyRequestRepository.countByCurrentShop(shop.getId());
    }

    @Override
    public Page<EmergencyRequest> searchEmergencyRequests(String searchTerm, Pageable pageable) {
        return emergencyRequestRepository.searchEmergencyRequests(searchTerm, pageable);
    }
}
