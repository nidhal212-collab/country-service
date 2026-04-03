package com.example.countryservice.controllers;

import com.example.countryservice.DTO.CountryDTO;
import com.example.countryservice.models.Country;
import com.example.countryservice.services.CountryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping("/getcountries")
    public ResponseEntity<List<Country>> getAllCountries() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    @GetMapping("/getcountries/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable int id) {
        return ResponseEntity.ok(countryService.getCountryById(id));
    }

    @GetMapping("/getcountries/countryname")
    public ResponseEntity<Country> getCountryByName(@RequestParam String name) {
        return ResponseEntity.ok(countryService.getCountryByName(name));
    }

    @PostMapping("/addcountry")
    public ResponseEntity<Country> addCountry(@Valid @RequestBody CountryDTO countryDTO) {
        Country saved = countryService.addCountry(countryDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/updatecountry/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable int id, @Valid @RequestBody CountryDTO countryDTO) {
        Country updated = countryService.updateCountry(id, countryDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/deletecountry/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable int id) {
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }
}
