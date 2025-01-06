package com.example.vehicleService.service.impl;

import com.example.vehicleService.dto.ReviewDTO;
import com.example.vehicleService.entity.*;
import com.example.vehicleService.entity.enums.ServiceType;
import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.exception.BlogAPIException;
import com.example.vehicleService.mapper.ReviewMapper;
import com.example.vehicleService.repository.*;
import com.example.vehicleService.service.CloudinaryService;
import com.example.vehicleService.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;
    private final AppointmentRepository appointmentRepository;
    private final EmergencyRequestRepository emergencyRequestRepository;
    private final ProposalRepository proposalRepository;
    private final CloudinaryService cloudinaryService;
    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, CustomerRepository customerRepository, ShopRepository shopRepository, AppointmentRepository appointmentRepository, EmergencyRequestRepository emergencyRequestRepository, ProposalRepository proposalRepository, CloudinaryService cloudinaryService, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
        this.shopRepository = shopRepository;
        this.appointmentRepository = appointmentRepository;
        this.emergencyRequestRepository = emergencyRequestRepository;
        this.proposalRepository = proposalRepository;
        this.cloudinaryService = cloudinaryService;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public Review getById(Integer id) {
        return reviewRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Review!")
        );
    }

    @Override
    public Page<Review> getAllPagination(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    @Override
    public Review save(ReviewDTO reviewDTO, MultipartFile image) {
        Review review = reviewMapper.toEntity(reviewDTO, customerRepository);
        Double currentRate = 0d;
        Shop shop = null;

        if (reviewDTO.getAppointmentId() == null) {
            Proposal proposal = proposalRepository.findById(reviewDTO.getProposalId()).orElseThrow(
                    () -> new EntityNotFoundException("Not found Proposal")
            );
            review.setBaseService(proposal);
            shop = proposal.getShop();
            currentRate = shop.getRating();
            review.setServiceType(ServiceType.EMERGENCY_REQUEST);
        } else {
            Appointment appointment = appointmentRepository.findById(reviewDTO.getAppointmentId()).orElseThrow(
                    () -> new EntityNotFoundException("Not found Appointment")
            );
            review.setBaseService(appointment);
            List<VehicleCare> vehicleCares = appointment.getVehicleCares().stream().collect(Collectors.toList());
            shop = vehicleCares.get(0).getShop();
            currentRate = shop.getRating();
            review.setServiceType(ServiceType.APPOINTMENT);
        }

        long cntReview = reviewRepository.findByShopId(shop.getId()).size();
        Double newRating = (currentRate * cntReview + review.getRate()) / (cntReview + 1);
        shop.setRating(newRating);
        shopRepository.save(shop);

        if (image != null) {
            Map<String, Object> imageUrl = cloudinaryService.upload(image, "review");
            String url = imageUrl.get("secure_url").toString();
            review.setImageUrl(url);
        }
        return reviewRepository.save(review);
    }


    @Override
    public void delete(Integer id) {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Review!")
        );
        review.setDeleted(true);
        reviewRepository.save(review);
    }

    @Override
    public List<Review> findTop8ByOrderByCreatedAtDesc() {
        return reviewRepository.findTop8ByOrderByCreatedAtDesc();
    }

    @Override
    public Review findByAppointmentId(Integer id) {
        return reviewRepository.findByBaseServiceId(id);
    }

    @Override
    public Review findByProposalId(Integer id) {
        return reviewRepository.findByBaseServiceId(id);
    }

    @Override
    public List<Review> findByShop(Integer shopId) {
        return reviewRepository.findByShopId(shopId);
    }

    @Override
    public Page<Review> searchReviews(String searchTerm, Pageable pageable) {
        return reviewRepository.searchReviews(searchTerm, pageable);
    }

    @Override
    public Page<Review> findByCurrentShop(Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Shop shop = shopRepository.findByUserUsername(username);
        return reviewRepository.findByShopId(shop.getId(), pageable);
    }

    @Override
    public Page<Review> searchReviewsInCurrentShop(String searchTerm, Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Shop shop = shopRepository.findByUserUsername(username);
        return reviewRepository.searchReviewsInShop(searchTerm, shop.getId(), pageable);
    }
}
