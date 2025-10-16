package com.aniket.pms.service;

import com.aniket.pms.entities.ParkingSlot;
import com.aniket.pms.entities.ParkingTicket;
import com.aniket.pms.entities.Vehicle;
import com.aniket.pms.enums.VehicleType;
import com.aniket.pms.exceptions.ResourceNotFoundException;
import com.aniket.pms.model.ParkingTicketResponseDto;
import com.aniket.pms.repository.ParkingSlotRepository;
import com.aniket.pms.repository.ParkingTicketRepository;
import com.aniket.pms.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingTicketServiceTest {

    @Mock
    private ParkingTicketRepository ticketRepository;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private ParkingSlotRepository slotRepository;

    @InjectMocks
    private ParkingTicketService parkingTicketService;

    private UUID vehicleId;
    private UUID ticketId;
    private Vehicle vehicle;
    private ParkingSlot slot;
    private ParkingTicket ticket;

    @BeforeEach
    void setUp() {
        vehicleId = UUID.randomUUID();
        ticketId = UUID.randomUUID();
        vehicle = new Vehicle(vehicleId, "ABC123", "aniket", VehicleType.CAR, null);
        slot = new ParkingSlot(UUID.randomUUID(), "A1", VehicleType.CAR, true, null);
        ticket = new ParkingTicket(ticketId, vehicle, slot, LocalDateTime.now(), null);
    }

    @Test
    void parkVehicle_Success() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(ticketRepository.findByVehicleAndExitTimeIsNull(vehicle)).thenReturn(Optional.empty());
        when(slotRepository.findByIsAvailableAndSlotType(true, VehicleType.CAR))
                .thenReturn(Arrays.asList(slot));
        when(ticketRepository.save(any(ParkingTicket.class))).thenReturn(ticket);

        ParkingTicketResponseDto result = parkingTicketService.parkVehicle(vehicleId);

        assertNotNull(result);
        assertEquals(vehicle.getId(), result.getVehicle().getId());
        assertEquals(slot.getId(), result.getSlot().getId());
        verify(slotRepository).save(slot);
        assertFalse(slot.isAvailable());
    }

    @Test
    void parkVehicle_ThrowsException_WhenVehicleNotFound() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
                () -> parkingTicketService.parkVehicle(vehicleId));
    }

    @Test
    void parkVehicle_ThrowsException_WhenVehicleAlreadyParked() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(ticketRepository.findByVehicleAndExitTimeIsNull(vehicle)).thenReturn(Optional.of(ticket));

        assertThrows(RuntimeException.class, 
                () -> parkingTicketService.parkVehicle(vehicleId));
    }

    @Test
    void parkVehicle_ThrowsException_WhenNoAvailableSlots() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(ticketRepository.findByVehicleAndExitTimeIsNull(vehicle)).thenReturn(Optional.empty());
        when(slotRepository.findByIsAvailableAndSlotType(true, VehicleType.CAR))
                .thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, 
                () -> parkingTicketService.parkVehicle(vehicleId));
    }

    @Test
    void unparkVehicle_Success() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(ParkingTicket.class))).thenReturn(ticket);

        ParkingTicketResponseDto result = parkingTicketService.unparkVehicle(ticketId);

        assertNotNull(result);
        assertNotNull(result.getExitTime());
        assertTrue(slot.isAvailable());
        verify(slotRepository).save(slot);
    }

    @Test
    void unparkVehicle_ThrowsException_WhenTicketNotFound() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
                () -> parkingTicketService.unparkVehicle(ticketId));
    }

    @Test
    void unparkVehicle_ThrowsException_WhenAlreadyUnparked() {
        ticket.setExitTime(LocalDateTime.now());
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        assertThrows(RuntimeException.class, 
                () -> parkingTicketService.unparkVehicle(ticketId));
    }

    @Test
    void getTicket_Success() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        ParkingTicketResponseDto result = parkingTicketService.getTicket(ticketId);

        assertNotNull(result);
        assertEquals(ticketId, result.getId());
    }

    @Test
    void getTicket_ThrowsException_WhenNotFound() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
                () -> parkingTicketService.getTicket(ticketId));
    }
}