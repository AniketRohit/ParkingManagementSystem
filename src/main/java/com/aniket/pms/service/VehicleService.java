package com.aniket.pms.service;

import com.aniket.pms.entities.Vehicle;
import com.aniket.pms.exceptions.ResourceNotFoundException;
import com.aniket.pms.model.VehicleDto;
import com.aniket.pms.model.VehicleResponseDto;
import com.aniket.pms.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleResponseDto registerVehicle(VehicleDto dto) {
        log.debug("Registering vehicle with license plate: {}", dto.getLicensePlate());
        
        vehicleRepository.findByLicensePlate(dto.getLicensePlate())
                .ifPresent(v -> {
                    log.error("Vehicle with license plate {} already exists", dto.getLicensePlate());
                    throw new RuntimeException("Vehicle with license plate '" + dto.getLicensePlate() + "' already exists");
                });

        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(dto, vehicle);
        Vehicle saved = vehicleRepository.save(vehicle);
        
        log.info("Vehicle registered successfully with ID: {}", saved.getId());
        return mapToResponseDto(saved);
    }

    public VehicleResponseDto getVehicle(UUID id) {
        log.debug("Fetching vehicle with ID: {}", id);
        
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Vehicle not found with ID: {}", id);
                    return new ResourceNotFoundException("Vehicle not found with ID: " + id);
                });
        
        log.debug("Vehicle found: {}", vehicle.getLicensePlate());
        return mapToResponseDto(vehicle);
    }

    public List<VehicleResponseDto> getAllVehicles() {
        log.debug("Fetching all vehicles");
        
        List<Vehicle> vehicles = vehicleRepository.findAll();
        log.info("Found {} vehicles", vehicles.size());
        
        return vehicles.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    private VehicleResponseDto mapToResponseDto(Vehicle vehicle) {
        VehicleResponseDto dto = new VehicleResponseDto();
        BeanUtils.copyProperties(vehicle, dto);
        return dto;
    }
}