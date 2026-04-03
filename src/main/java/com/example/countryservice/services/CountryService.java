package com.example.countryservice.services;

import com.example.countryservice.DTO.CountryDTO;
import com.example.countryservice.exceptions.ResourceNotFoundException;
import com.example.countryservice.models.Country;
import com.example.countryservice.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public Country getCountryById(int id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country with id " + id + " not found"));
    }

    public Country getCountryByName(String name) {
        Country country = countryRepository.findByName(name);
        if (country == null) {
            throw new ResourceNotFoundException("Country with name '" + name + "' not found");
        }
        return country;
    }

    public Country addCountry(CountryDTO dto) {
        Country country = new Country();
        country.setName(dto.getName());
        country.setCapital(dto.getCapital());
        return countryRepository.save(country);
    }

    public Country updateCountry(int id, CountryDTO dto) {
        Country existingCountry = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country with id " + id + " not found"));
        existingCountry.setName(dto.getName());
        existingCountry.setCapital(dto.getCapital());
        return countryRepository.save(existingCountry);
    }

    public void deleteCountry(int id) {
        if (!countryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Country with id " + id + " not found");
        }
        countryRepository.deleteById(id);
    }
}
