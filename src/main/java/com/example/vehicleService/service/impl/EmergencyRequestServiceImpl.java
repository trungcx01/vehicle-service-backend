package com.example.vehicleService.service.impl;

import com.example.vehicleService.dto.EmergencyRequestDTO;
import com.example.vehicleService.entity.EmergencyRequest;
import com.example.vehicleService.entity.Notification;
import com.example.vehicleService.entity.Proposal;
import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.mapper.EmergencyRequestMapper;
import com.example.vehicleService.repository.*;
import com.example.vehicleService.service.CloudinaryService;
import com.example.vehicleService.service.EmergencyRequestService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class EmergencyRequestServiceImpl implements EmergencyRequestService {
    private final EmergencyRequestRepository emergencyRequestRepository;
    private final EmergencyRequestMapper emergencyRequestMapper;
    private final ProposalRepository proposalRepository;
    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;
    private final CloudinaryService cloudinaryService;
    private  final TaskScheduler taskScheduler;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationRepository notificationRepository;

    public EmergencyRequestServiceImpl(EmergencyRequestRepository emergencyRequestRepository, EmergencyRequestMapper emergencyRequestMapper, ProposalRepository proposalRepository, CustomerRepository customerRepository, ShopRepository shopRepository, CloudinaryService cloudinaryService, TaskScheduler taskScheduler, SimpMessagingTemplate simpMessagingTemplate, NotificationRepository notificationRepository) {
        this.emergencyRequestRepository = emergencyRequestRepository;
        this.emergencyRequestMapper = emergencyRequestMapper;
        this.proposalRepository = proposalRepository;
        this.customerRepository = customerRepository;
        this.shopRepository = shopRepository;
        this.cloudinaryService = cloudinaryService;
        this.taskScheduler = taskScheduler;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.notificationRepository = notificationRepository;
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
    @Transactional
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
        emergencyRequest = emergencyRequestRepository.save(emergencyRequest);
        scheduleRequest(emergencyRequest);
        return emergencyRequest;
    }

    public void scheduleRequest(EmergencyRequest request) {
        Instant cancelTime = Instant.now().plusSeconds(15 * 60);
        taskScheduler.schedule(() -> cancelRequest(request.getId()), Date.from(cancelTime));

        Instant finishTime = Instant.now().plusSeconds(4 * 60 * 60);
        taskScheduler.schedule(() -> finishRequest(request.getId()), Date.from(finishTime));
    }

    private void cancelRequest(Integer requestId) {
        EmergencyRequest request = emergencyRequestRepository.findById(requestId).orElse(null);
        if (request != null &&  request.getRequestStatus().equals(Status.PENDING)) {
            request.setRequestStatus(Status.CANCELED);
            List<Proposal> proposals = proposalRepository.findByEmergencyRequestId(request.getId());
            for (Proposal p : proposals){
                p.setStatus(Status.DECLINED);
            }
            proposalRepository.saveAll(proposals);
            emergencyRequestRepository.save(request);
            String message = "Yêu cầu cứu trợ bị hủy vì đã vượt quá 15 phút, bạn có thể tạo yêu cầu mới";
            simpMessagingTemplate.convertAndSendToUser(request.getCustomer().getUser().getUsername(), "queue/notifications", "CANCELED_EMERGENCY_REQUEST: " + message);
            System.out.println("Request ID " + request + " has been canceled.");
        } else {
            System.out.println("Request ID " + request + " was already handled or not found.");
        }
    }

    private void finishRequest(Integer requestId) {
        EmergencyRequest request = emergencyRequestRepository.findById(requestId).orElse(null);
        if (request != null &&  request.getRequestStatus().equals(Status.ARRIVED)) {
            request.setRequestStatus(Status.FINISHED);
            emergencyRequestRepository.save(request);
            String message = "Hệ thống tự động xác nhận hoàn thành yêu cầu cứu trợ khẩn cấp của bạn với ID: " + request.getId();
            Notification notification = new Notification();
            notification.setUsers(Set.of(request.getCustomer().getUser()));
            notification.setMessage(message);
            notificationRepository.save(notification);
        } else {
            System.out.println("Request ID " + request + " was already handled or not found.");
        }
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
