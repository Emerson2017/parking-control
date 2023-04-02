package com.api.parkingcontrol.services;

import com.api.parkingcontrol.dto.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpot;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingSpotService   {

    final ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    public Optional<ParkingSpot> getParkingSpot(UUID id) {
        return parkingSpotRepository.findById(id);
    }

    public Page<ParkingSpot> findAll(Pageable pageable) {
        return parkingSpotRepository.findAll(pageable);
    }

    @Transactional
    public void deleteParkingSpot(ParkingSpot parkingSpot) {
        parkingSpotRepository.delete(parkingSpot);
    }

    public ParkingSpot save(ParkingSpot parkingSpotModel) {
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return parkingSpotRepository.save(parkingSpotModel);
    }

    public void validRequest(ParkingSpotDto parkingSpotDto) throws Exception {
        if (parkingSpotRepository.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
            throw new Exception("License plate car is already in use");
        }

        if (parkingSpotRepository.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
            throw new Exception("Parking Spot is already in use");
        }

        if (parkingSpotRepository.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())) {
            throw new Exception("Parking Spot already registered");
        }
    }
}
