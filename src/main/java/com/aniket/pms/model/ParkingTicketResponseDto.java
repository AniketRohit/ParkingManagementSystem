package com.aniket.pms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingTicketResponseDto {
    private UUID id;
    private VehicleResponseDto vehicle;
    private ParkingSlotResponseDto slot;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
}