package com.aniket.pms.service;

import com.aniket.pms.entities.ParkingSlot;
import com.aniket.pms.enums.VehicleType;
import com.aniket.pms.model.ParkingSlotDto;
import com.aniket.pms.model.ParkingSlotResponseDto;
import com.aniket.pms.repository.ParkingSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingSlotServiceTest {

    @Mock
    private ParkingSlotRepository slotRepository;

    @InjectMocks
    private ParkingSlotService parkingSlotService;

    private ParkingSlotDto slotDto;
    private ParkingSlot slot;

    @BeforeEach
    void setUp() {
        slotDto = new ParkingSlotDto("A1", VehicleType.CAR);
        slot = new ParkingSlot(UUID.randomUUID(), "A1", VehicleType.CAR, true, null);
    }

    @Test
    void createSlot_Success() {
        when(slotRepository.save(any(ParkingSlot.class))).thenReturn(slot);

        ParkingSlotResponseDto result = parkingSlotService.createSlot(slotDto);

        assertNotNull(result);
        assertEquals("A1", result.getSlotNumber());
        assertEquals(VehicleType.CAR, result.getSlotType());
        verify(slotRepository).save(any(ParkingSlot.class));
    }

    @Test
    void listSlots_WithFilters() {
        List<ParkingSlot> expectedSlots = Arrays.asList(slot);
        when(slotRepository.findByIsAvailableAndSlotType(true, VehicleType.CAR))
                .thenReturn(expectedSlots);

        List<ParkingSlotResponseDto> result = parkingSlotService.listSlots(true, VehicleType.CAR);

        assertEquals(1, result.size());
        assertEquals("A1", result.get(0).getSlotNumber());
        verify(slotRepository).findByIsAvailableAndSlotType(true, VehicleType.CAR);
    }

    @Test
    void listSlots_WithoutFilters() {
        List<ParkingSlot> allSlots = Arrays.asList(slot, new ParkingSlot());
        when(slotRepository.findAll()).thenReturn(allSlots);

        List<ParkingSlotResponseDto> result = parkingSlotService.listSlots(null, null);

        assertEquals(2, result.size());
        verify(slotRepository).findAll();
    }

    @Test
    void listSlots_WithOnlyAvailabilityFilter() {
        List<ParkingSlot> allSlots = Arrays.asList(slot);
        when(slotRepository.findAll()).thenReturn(allSlots);

        List<ParkingSlotResponseDto> result = parkingSlotService.listSlots(true, null);

        assertEquals(1, result.size());
        verify(slotRepository).findAll();
    }
}