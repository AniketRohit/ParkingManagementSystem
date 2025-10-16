package com.aniket.pms.controller;

import com.aniket.pms.enums.VehicleType;
import com.aniket.pms.model.ParkingSlotDto;
import com.aniket.pms.model.ParkingSlotResponseDto;
import com.aniket.pms.service.ParkingSlotService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
@Slf4j
public class ParkingSlotController {
    private final ParkingSlotService slotService;

    @PostMapping
    public ResponseEntity<ParkingSlotResponseDto> create(@Valid @RequestBody ParkingSlotDto dto){
        log.info("Creating parking slot: {}", dto.getSlotNumber());
        return ResponseEntity.ok(slotService.createSlot(dto));
    }

    @GetMapping
    public ResponseEntity<List<ParkingSlotResponseDto>> list(
            @RequestParam(required = false) Boolean isAvailable,
            @RequestParam(required = false) VehicleType type){
        log.info("Listing parking slots with filters - available: {}, type: {}", isAvailable, type);
        return ResponseEntity.ok(slotService.listSlots(isAvailable, type));
    }
}