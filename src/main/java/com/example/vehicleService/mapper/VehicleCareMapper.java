package com.example.vehicleService.mapper;

import com.example.vehicleService.dto.VehicleCareDTO;
import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.entity.VehicleCare;
import com.example.vehicleService.repository.ShopRepository;
import com.example.vehicleService.repository.VehicleCareRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ShopMapper.class})
public interface VehicleCareMapper extends EntityMapper<VehicleCareDTO, VehicleCare>{
    @Mapping(source = "shopId", target = "shop")
    VehicleCare toEntity(VehicleCareDTO dto, @Context ShopRepository shopRepository);

    @Mapping(source = "shop.id", target = "shopId")
    VehicleCareDTO toDto(VehicleCare entity);


}
