package com.aniket.pms.repository;

import com.aniket.pms.entities.ParkingSlot;
import com.aniket.pms.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, UUID> {
    List<ParkingSlot> findByIsAvailableAndSlotType(boolean isAvailable, VehicleType slotType);
    Optional<ParkingSlot> findBySlotNumber(String slotNumber);
}
