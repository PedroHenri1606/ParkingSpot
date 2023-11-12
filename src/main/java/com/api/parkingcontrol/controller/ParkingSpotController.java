package com.api.parkingcontrol.controller;

import com.api.parkingcontrol.dto.ParkingSpotDTO;
import com.api.parkingcontrol.model.ParkingSpotModel;
import com.api.parkingcontrol.service.ParkingSpotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

/*

    {
        "parkingSpotNumber": "205B",
        "licensePlateCar": "RRS8562",
        "brandCar": "audi",
        "modelCar": "q5",
        "colorCar": "black",
        "registrationDate": "2023-09-02T00:42:20.5471879",
        "responsibleName": "Pedro Henrique",
        "apartment": "205",
        "block": "b"
    }

 */

@RestController
@RequestMapping(value = "/parking")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ParkingSpotController {

    @Autowired
    private ParkingSpotService service;

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDTO parkingSpotDTO) {
        try {

            if (service.existsByLicensePlateCar(parkingSpotDTO.getLicensePlateCar())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");

            }  if (service.existsByParkingSpotNumber(parkingSpotDTO.getParkingSpotNumber())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");

            }  if (service.existsByApartmentAndBlock(parkingSpotDTO.getApartment(), parkingSpotDTO.getBlock())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registered for this apartment/block!");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(parkingSpotDTO));

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error, " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpots(@PageableDefault(page = 0,size = 10,sort = "id",direction = Sort.Direction.ASC)Pageable pageable){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findAll(pageable));

        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "It was not possible to locate a registered car spot.");
        }
    }

    @GetMapping(value = "/find")
    public ResponseEntity<Object> getOneParkingSpot(@RequestParam("id") final UUID id){
         try {
             return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
         } catch (Exception e){
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found");
         }
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Object> updateParkingSpot(@RequestParam("id") final UUID id, @Valid @RequestBody final ParkingSpotDTO parkingSpotUpdated){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.updateParkingSpot(id,parkingSpotUpdated));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found");
        }
    }

    @DeleteMapping(value = "/deleteBy")
    public ResponseEntity<Object> deleteParkingSpot(@RequestParam("id") final UUID id){
        try {
            service.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
    }
}
