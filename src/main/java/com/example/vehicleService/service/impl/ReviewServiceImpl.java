package com.example.vehicleService.service.impl;

import com.example.vehicleService.dto.ReviewDTO;
import com.example.vehicleService.entity.Review;
import com.example.vehicleService.mapper.ReviewMapper;
import com.example.vehicleService.repository.CustomerRepository;
import com.example.vehicleService.repository.ReviewRepository;
import com.example.vehicleService.repository.ShopRepository;
import com.example.vehicleService.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;
    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, CustomerRepository customerRepository, ShopRepository shopRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
        this.shopRepository = shopRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public Review getById(Long id) {
        return reviewRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Review!")
        );
    }

    @Override
    public Page<Review> getAllPagination(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    @Override
    public Review save(ReviewDTO reviewDTO) {
        Review review = reviewMapper.toEntity(reviewDTO, shopRepository, customerRepository);
        return reviewRepository.save(review);
    }

    @Override
    public void delete(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Review!")
        );
        reviewRepository.delete(review);
    }

    @Override
    public List<Review> findTop8ByOrderByCreatedAtDesc() {
        return reviewRepository.findTop8ByOrderByCreatedAtDesc();
    }
}
