package com.example.vehicleService.mapper;

import com.example.vehicleService.dto.ProposalDTO;
import com.example.vehicleService.entity.Proposal;
import com.example.vehicleService.exception.BlogAPIException;
import com.example.vehicleService.repository.EmergencyRequestRepository;
import com.example.vehicleService.repository.ProposalRepository;
import com.example.vehicleService.repository.ShopRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;

@Mapper(componentModel = "spring", uses = {ShopMapper.class, EmergencyRequestMapper.class})
public interface ProposalMapper extends EntityMapper<ProposalDTO, Proposal> {
    @Mapping(source = "shopId", target = "shop")
    @Mapping(source = "emergencyRequestId", target = "emergencyRequest")
    Proposal toEntity(ProposalDTO dto, @Context ShopRepository shopRepository, @Context EmergencyRequestRepository emergencyRequestRepository);

    @Mapping(source = "shop.id", target = "shopId")
    @Mapping(source = "emergencyRequest.id", target = "emergencyRequestId")
    ProposalDTO toDto(Proposal entity);

    default Proposal fromId(Long id, @Context ProposalRepository proposalRepository){
        if (id == null){
            return null;
//            throw new BlogAPIException(HttpStatus.NOT_FOUND, "Please fill in proposal id!");
        }
        Proposal proposal = proposalRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Proposal!")
        );
        return proposal;
    }


}
