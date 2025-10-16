package com.aniket.pms.service;

import com.aniket.pms.entities.ParkingSlot;
import com.aniket.pms.enums.VehicleType;
import com.aniket.pms.model.ParkingSlotDto;
import com.aniket.pms.model.ParkingSlotResponseDto;
import com.aniket.pms.repository.ParkingSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingSlotService {
    private final ParkingSlotRepository slotRepository;

    public ParkingSlotResponseDto createSlot(ParkingSlotDto dto) {
        log.debug("Creating parking slot: {}", dto.getSlotNumber());
        
        slotRepository.findBySlotNumber(dto.getSlotNumber())
                .ifPresent(s -> {
                    log.error("Parking slot with number {} already exists", dto.getSlotNumber());
                    throw new RuntimeException("Parking slot with number '" + dto.getSlotNumber() + "' already exists");
                });
        
        ParkingSlot slot = new ParkingSlot();
        BeanUtils.copyProperties(dto, slot);
        ParkingSlot saved = slotRepository.save(slot);
        
        log.info("Parking slot created successfully with ID: {}", saved.getId());
        return mapToResponseDto(saved);
    }

    public List<ParkingSlotResponseDto> listSlots(Boolean isAvailable, VehicleType type) {
        log.debug("Listing slots with filters - available: {}, type: {}", isAvailable, type);
        
        List<ParkingSlot> slots;
        if(isAvailable != null && type != null) {
            slots = slotRepository.findByIsAvailableAndSlotType(isAvailable, type);
        } else {
            slots = slotRepository.findAll();
        }
        
        log.info("Found {} parking slots", slots.size());
        return slots.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    private ParkingSlotResponseDto mapToResponseDto(ParkingSlot slot) {
        ParkingSlotResponseDto dto = new ParkingSlotResponseDto();
        BeanUtils.copyProperties(slot, dto);
        return dto;
    }
}