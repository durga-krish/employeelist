package com.durga.employeelist.controller;

import com.durga.employeelist.model.Location;
import com.durga.employeelist.repository.LocationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping
public class LocationController {

    private final LocationRepository locationRepository;

    public LocationController(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @GetMapping
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @GetMapping("/{id}")
    public Location getLocationById(@PathVariable int id) {
        return locationRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody Location location) throws URISyntaxException {
        Location savedLocation = locationRepository.save(location);
        return ResponseEntity.created(new URI("/locations/" + savedLocation.getId())).body(savedLocation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@RequestBody Location location, @PathVariable int id) {
        Location currentLocation = locationRepository.findById(id).orElseThrow(RuntimeException::new);
        currentLocation.setName(location.getName());
        currentLocation.setAddress(location.getAddress());
        currentLocation.setZipcode(location.getZipcode());
        currentLocation.setLat(location.getLat());
        currentLocation.setLng(location.getLng());
        Location updatedLocation = locationRepository.save(currentLocation);
        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable int id) {
        locationRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
