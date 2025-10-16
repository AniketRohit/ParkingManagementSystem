package com.aniket.pms.model;

import com.aniket.pms.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    @NotBlank(message = "License plate is required")
    private String licensePlate;
    
    @NotBlank(message = "Owner name is required")
    private String ownerName;
    
    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;
}
