package com.api.parkingcontrol.repository;

import com.api.parkingcontrol.model.ParkingSpotModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpotModel, UUID> {

     Boolean existsByLicensePlateCar(String licensePlateCar);
     Boolean existsByParkingSpotNumber(String parkingSpotNumber);
     Boolean existsByApartmentAndBlock(String apartment, String block);
}
