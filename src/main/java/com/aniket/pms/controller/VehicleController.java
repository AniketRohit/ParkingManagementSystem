package com.aniket.pms.controller;

import com.aniket.pms.model.VehicleDto;
import com.aniket.pms.model.VehicleResponseDto;
import com.aniket.pms.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Slf4j
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping("/create")
    public ResponseEntity<VehicleResponseDto> create(@Valid @RequestBody VehicleDto dto){
        log.info("Creating vehicle with license plate: {}", dto.getLicensePlate());
        return ResponseEntity.ok(vehicleService.registerVehicle(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDto> get(@PathVariable UUID id){
        log.info("Fetching vehicle with ID: {}", id);
        return ResponseEntity.ok(vehicleService.getVehicle(id));
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponseDto>> list(){
        log.info("Fetching all vehicles");
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }
}