package com.aniket.pms.controller;

import com.aniket.pms.model.ParkingTicketResponseDto;
import com.aniket.pms.service.ParkingTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ParkingTicketController {

    private final ParkingTicketService ticketService;

    @PostMapping("/park")
    public ResponseEntity<ParkingTicketResponseDto> park(@RequestParam UUID vehicleId){
        log.info("Parking vehicle with ID: {}", vehicleId);
        return ResponseEntity.ok(ticketService.parkVehicle(vehicleId));
    }

    @PostMapping("/unpark/{ticketId}")
    public ResponseEntity<ParkingTicketResponseDto> unpark(@PathVariable UUID ticketId){
        log.info("Unparking vehicle with ticket ID: {}", ticketId);
        return ResponseEntity.ok(ticketService.unparkVehicle(ticketId));
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<ParkingTicketResponseDto> get(@PathVariable UUID id){
        log.info("Fetching ticket with ID: {}", id);
        return ResponseEntity.ok(ticketService.getTicket(id));
    }
}