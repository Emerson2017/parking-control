package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dto.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpot;
import com.api.parkingcontrol.services.ParkingSpotService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-control")
public class ParkingSpotController {

    final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto) throws Exception {
        parkingSpotService.validRequest(parkingSpotDto);
        var parkingSpotModel = new ParkingSpot();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpot>> getAllParkingSpots(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getParkingSpot(@PathVariable(value = "id") UUID id) {
        Optional<ParkingSpot> parkingSpot = parkingSpotService.getParkingSpot(id);
        if(!parkingSpot.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpot.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id) {
        Optional<ParkingSpot> parkingSpot = parkingSpotService.getParkingSpot(id);
        if(!parkingSpot.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found");
        }
        parkingSpotService.deleteParkingSpot(parkingSpot.get());
        return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid ParkingSpotDto parkingSpotDto) {
        Optional<ParkingSpot> parkingSpotOpt = parkingSpotService.getParkingSpot(id);
        if(!parkingSpotOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found");
        }
        ParkingSpot parkingSpot = new ParkingSpot();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpot);
        parkingSpot.setId(parkingSpotOpt.get().getId());
        parkingSpot.setRegistrationDate(parkingSpotOpt.get().getRegistrationDate());
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpot));
    }

}
