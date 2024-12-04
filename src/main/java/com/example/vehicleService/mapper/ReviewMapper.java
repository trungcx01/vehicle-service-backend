package com.example.vehicleService.mapper;

import com.example.vehicleService.dto.ReviewDTO;
import com.example.vehicleService.entity.Review;
import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.repository.CustomerRepository;
import com.example.vehicleService.repository.ShopRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ShopMapper.class, CustomerMapper.class})
public interface ReviewMapper extends EntityMapper<ReviewDTO, Review>{
    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "shopId", target = "shop")
    Review toEntity(ReviewDTO dto, @Context ShopRepository shopRepository, @Context CustomerRepository customerRepository);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "shop.id", target = "shopId")
    ReviewDTO toDto(Review entity);
}
