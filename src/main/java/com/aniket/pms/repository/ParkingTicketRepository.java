package com.aniket.pms.repository;

import com.aniket.pms.entities.ParkingTicket;
import com.aniket.pms.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, UUID> {
    Optional<ParkingTicket> findByVehicleAndExitTimeIsNull(Vehicle vehicle);
}
