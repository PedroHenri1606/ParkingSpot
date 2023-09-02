package com.api.parkingcontrol.service;

import com.api.parkingcontrol.dto.ParkingSpotDTO;
import com.api.parkingcontrol.model.ParkingSpotModel;
import com.api.parkingcontrol.repository.ParkingSpotRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingSpotService {

    @Autowired
    private ParkingSpotRepository repository;

    @Transactional
    public ParkingSpotDTO save(ParkingSpotDTO parkingSpotDTO) {

        ParkingSpotModel parkingSpotModel = new ParkingSpotModel();

        BeanUtils.copyProperties(parkingSpotDTO, parkingSpotModel);

            repository.save(parkingSpotModel);
            return parkingSpotDTO;
    }

    public Page<ParkingSpotModel> findAll(Pageable pageable){
        Page<ParkingSpotModel> parkingSpotModel = repository.findAll(pageable);

        if(parkingSpotModel.isEmpty()){
            throw new RuntimeException("It was not possible to locate any registered car spot.");
        } else {
            return parkingSpotModel;
        }
    }

    public ParkingSpotModel findById(UUID id){
        Optional<ParkingSpotModel> parkingSpotModelOptional = repository.findById(id);
        if(!parkingSpotModelOptional.isPresent()){

            throw new RuntimeException("Parking Spot not found."); // -> Não é necessário
        }
        return parkingSpotModelOptional.get();
    }

    @Transactional
    public void delete(UUID id){
        Optional<ParkingSpotModel> parkingSpotModel = repository.findById(id);

        repository.delete(parkingSpotModel.get());
    }

    @Transactional
    public ParkingSpotModel updateParkingSpot(UUID id, ParkingSpotDTO parkingSpotUpdated){
        Optional<ParkingSpotModel> parkingSpotModelOptional = repository.findById(id);

        ParkingSpotModel parkingSpotModel = new ParkingSpotModel();

        BeanUtils.copyProperties(parkingSpotUpdated,parkingSpotModel);
        parkingSpotModel.setId(parkingSpotModelOptional.get().getId());

        return repository.save(parkingSpotModel);
    }

    public Boolean existsByLicensePlateCar(String licensePlateCar){
        return repository.existsByLicensePlateCar(licensePlateCar);
    }

    public Boolean existsByParkingSpotNumber(String parkingSpotNumber){
        return repository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public Boolean existsByApartmentAndBlock(String apartment,String block){
        return repository.existsByApartmentAndBlock(apartment,block);
    }


}
