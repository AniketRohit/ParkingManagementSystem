package com.aniket.pms.service;

import com.aniket.pms.entities.ParkingSlot;
import com.aniket.pms.entities.ParkingTicket;
import com.aniket.pms.entities.Vehicle;
import com.aniket.pms.exceptions.ResourceNotFoundException;
import com.aniket.pms.model.ParkingSlotResponseDto;
import com.aniket.pms.model.ParkingTicketResponseDto;
import com.aniket.pms.model.VehicleResponseDto;
import com.aniket.pms.repository.ParkingSlotRepository;
import com.aniket.pms.repository.ParkingTicketRepository;
import com.aniket.pms.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingTicketService {
    private final ParkingTicketRepository ticketRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSlotRepository slotRepository;

    @Transactional
    public ParkingTicketResponseDto parkVehicle(UUID vehicleId) {
        log.debug("Parking vehicle with ID: {}", vehicleId);
        
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> {
                    log.error("Vehicle not found with ID: {}", vehicleId);
                    return new ResourceNotFoundException("Vehicle not found with ID: " + vehicleId);
                });

        if(ticketRepository.findByVehicleAndExitTimeIsNull(vehicle).isPresent()) {
            log.error("Vehicle {} is already parked", vehicle.getLicensePlate());
            throw new RuntimeException("Vehicle with license plate '" + vehicle.getLicensePlate() + "' is already parked");
        }

        List<ParkingSlot> availableSlots = slotRepository
                .findByIsAvailableAndSlotType(true, vehicle.getVehicleType());

        if(availableSlots.isEmpty()) {
            log.error("No available slots for vehicle type: {}", vehicle.getVehicleType());
            throw new RuntimeException("No available parking slots for vehicle type: " + vehicle.getVehicleType());
        }

        ParkingSlot slot = availableSlots.get(0);
        slot.setAvailable(false);
        slotRepository.save(slot);

        ParkingTicket ticket = new ParkingTicket();
        ticket.setVehicle(vehicle);
        ticket.setSlot(slot);
        ParkingTicket saved = ticketRepository.save(ticket);
        
        log.info("Vehicle {} parked successfully in slot {}", vehicle.getLicensePlate(), slot.getSlotNumber());
        return mapToResponseDto(saved);
    }

    @Transactional
    public ParkingTicketResponseDto unparkVehicle(UUID ticketId) {
        log.debug("Unparking vehicle with ticket ID: {}", ticketId);
        
        ParkingTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> {
                    log.error("Ticket not found with ID: {}", ticketId);
                    return new ResourceNotFoundException("Parking ticket not found with ID: " + ticketId);
                });

        if(ticket.getExitTime() != null) {
            log.error("Vehicle with ticket {} is already unparked", ticketId);
            throw new RuntimeException("Vehicle is already unparked. Ticket ID: " + ticketId);
        }

        ticket.setExitTime(LocalDateTime.now());
        ParkingSlot slot = ticket.getSlot();
        slot.setAvailable(true);
        slotRepository.save(slot);

        ParkingTicket saved = ticketRepository.save(ticket);
        log.info("Vehicle {} unparked successfully from slot {}", 
                ticket.getVehicle().getLicensePlate(), slot.getSlotNumber());
        
        return mapToResponseDto(saved);
    }

    public ParkingTicketResponseDto getTicket(UUID id) {
        log.debug("Fetching ticket with ID: {}", id);
        
        ParkingTicket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Ticket not found with ID: {}", id);
                    return new ResourceNotFoundException("Parking ticket not found with ID: " + id);
                });
        
        log.debug("Ticket found for vehicle: {}", ticket.getVehicle().getLicensePlate());
        return mapToResponseDto(ticket);
    }
    
    private ParkingTicketResponseDto mapToResponseDto(ParkingTicket ticket) {
        ParkingTicketResponseDto dto = new ParkingTicketResponseDto();
        dto.setId(ticket.getId());
        dto.setEntryTime(ticket.getEntryTime());
        dto.setExitTime(ticket.getExitTime());
        
        VehicleResponseDto vehicleDto = new VehicleResponseDto();
        BeanUtils.copyProperties(ticket.getVehicle(), vehicleDto);
        dto.setVehicle(vehicleDto);
        
        ParkingSlotResponseDto slotDto = new ParkingSlotResponseDto();
        BeanUtils.copyProperties(ticket.getSlot(), slotDto);
        dto.setSlot(slotDto);
        
        return dto;
    }
}