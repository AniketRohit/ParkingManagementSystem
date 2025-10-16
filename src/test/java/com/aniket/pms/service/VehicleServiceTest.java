package com.aniket.pms.service;

import com.aniket.pms.entities.Vehicle;
import com.aniket.pms.enums.VehicleType;
import com.aniket.pms.exceptions.ResourceNotFoundException;
import com.aniket.pms.model.VehicleDto;
import com.aniket.pms.model.VehicleResponseDto;
import com.aniket.pms.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private VehicleDto vehicleDto;
    private Vehicle vehicle;
    private UUID vehicleId;

    @BeforeEach
    void setUp() {
        vehicleId = UUID.randomUUID();
        vehicleDto = new VehicleDto("ABC123", "aniket", VehicleType.CAR);
        vehicle = new Vehicle(vehicleId, "ABC123", "aniket", VehicleType.CAR, null);
    }

    @Test
    void registerVehicle_Success() {
        when(vehicleRepository.findByLicensePlate("ABC123")).thenReturn(Optional.empty());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        VehicleResponseDto result = vehicleService.registerVehicle(vehicleDto);

        assertNotNull(result);
        assertEquals("ABC123", result.getLicensePlate());
        assertEquals("aniket", result.getOwnerName());
        assertEquals(VehicleType.CAR, result.getVehicleType());
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void registerVehicle_ThrowsException_WhenVehicleExists() {
        when(vehicleRepository.findByLicensePlate("ABC123")).thenReturn(Optional.of(vehicle));

        assertThrows(RuntimeException.class, () -> vehicleService.registerVehicle(vehicleDto));
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void getVehicle_Success() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        VehicleResponseDto result = vehicleService.getVehicle(vehicleId);

        assertNotNull(result);
        assertEquals(vehicleId, result.getId());
        assertEquals("ABC123", result.getLicensePlate());
    }

    @Test
    void getVehicle_ThrowsException_WhenNotFound() {
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.getVehicle(vehicleId));
    }

    @Test
    void getAllVehicles_Success() {
        List<Vehicle> vehicles = Arrays.asList(vehicle, new Vehicle());
        when(vehicleRepository.findAll()).thenReturn(vehicles);

        List<VehicleResponseDto> result = vehicleService.getAllVehicles();

        assertEquals(2, result.size());
        verify(vehicleRepository).findAll();
    }
}