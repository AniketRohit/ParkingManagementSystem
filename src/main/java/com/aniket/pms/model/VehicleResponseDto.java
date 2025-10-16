package com.aniket.pms.model;

import com.aniket.pms.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponseDto {
    private UUID id;
    private String licensePlate;
    private String ownerName;
    private VehicleType vehicleType;
}