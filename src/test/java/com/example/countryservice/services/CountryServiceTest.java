package com.example.countryservice.services;

import com.example.countryservice.DTO.CountryDTO;
import com.example.countryservice.exceptions.ResourceNotFoundException;
import com.example.countryservice.models.Country;
import com.example.countryservice.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    private Country country1;
    private Country country2;
    private CountryDTO countryDTO;

    @BeforeEach
    void setUp() {
        country1 = new Country(1, "Tunisia", "Tunis");
        country2 = new Country(2, "France", "Paris");
        countryDTO = new CountryDTO(null, "Tunisia", "Tunis");
    }

    // ========== getAllCountries ==========

    @Test
    @DisplayName("getAllCountries - should return list of all countries")
    void getAllCountries_returnsList() {
        when(countryRepository.findAll()).thenReturn(Arrays.asList(country1, country2));

        List<Country> result = countryService.getAllCountries();

        assertEquals(2, result.size());
        assertEquals("Tunisia", result.get(0).getName());
        assertEquals("France", result.get(1).getName());
        verify(countryRepository, times(1)).findAll();
    }

    // ========== getCountryById ==========

    @Test
    @DisplayName("getCountryById - should return country when ID exists")
    void getCountryById_found() {
        when(countryRepository.findById(1)).thenReturn(Optional.of(country1));

        Country result = countryService.getCountryById(1);

        assertNotNull(result);
        assertEquals("Tunisia", result.getName());
        assertEquals("Tunis", result.getCapital());
        verify(countryRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("getCountryById - should throw ResourceNotFoundException when ID does not exist")
    void getCountryById_notFound() {
        when(countryRepository.findById(99)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> countryService.getCountryById(99)
        );

        assertTrue(exception.getMessage().contains("99"));
        verify(countryRepository, times(1)).findById(99);
    }

    // ========== getCountryByName ==========

    @Test
    @DisplayName("getCountryByName - should return country when name exists")
    void getCountryByName_found() {
        when(countryRepository.findByName("Tunisia")).thenReturn(country1);

        Country result = countryService.getCountryByName("Tunisia");

        assertNotNull(result);
        assertEquals("Tunis", result.getCapital());
        verify(countryRepository, times(1)).findByName("Tunisia");
    }

    @Test
    @DisplayName("getCountryByName - should throw ResourceNotFoundException when name does not exist")
    void getCountryByName_notFound() {
        when(countryRepository.findByName("Unknown")).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> countryService.getCountryByName("Unknown")
        );

        assertTrue(exception.getMessage().contains("Unknown"));
        verify(countryRepository, times(1)).findByName("Unknown");
    }

    // ========== addCountry ==========

    @Test
    @DisplayName("addCountry - should save and return new country")
    void addCountry_success() {
        when(countryRepository.save(any(Country.class))).thenReturn(country1);

        Country result = countryService.addCountry(countryDTO);

        assertNotNull(result);
        assertEquals("Tunisia", result.getName());
        assertEquals("Tunis", result.getCapital());
        verify(countryRepository, times(1)).save(any(Country.class));
    }

    // ========== updateCountry ==========

    @Test
    @DisplayName("updateCountry - should update and return country when ID exists")
    void updateCountry_success() {
        CountryDTO updateDTO = new CountryDTO(null, "Tunisie", "Tunis City");
        Country updatedCountry = new Country(1, "Tunisie", "Tunis City");

        when(countryRepository.findById(1)).thenReturn(Optional.of(country1));
        when(countryRepository.save(any(Country.class))).thenReturn(updatedCountry);

        Country result = countryService.updateCountry(1, updateDTO);

        assertNotNull(result);
        assertEquals("Tunisie", result.getName());
        assertEquals("Tunis City", result.getCapital());
        verify(countryRepository, times(1)).findById(1);
        verify(countryRepository, times(1)).save(any(Country.class));
    }

    @Test
    @DisplayName("updateCountry - should throw ResourceNotFoundException when ID does not exist")
    void updateCountry_notFound() {
        when(countryRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> countryService.updateCountry(99, countryDTO)
        );

        verify(countryRepository, times(1)).findById(99);
        verify(countryRepository, never()).save(any(Country.class));
    }

    // ========== deleteCountry ==========

    @Test
    @DisplayName("deleteCountry - should delete when ID exists")
    void deleteCountry_success() {
        when(countryRepository.existsById(1)).thenReturn(true);
        doNothing().when(countryRepository).deleteById(1);

        assertDoesNotThrow(() -> countryService.deleteCountry(1));

        verify(countryRepository, times(1)).existsById(1);
        verify(countryRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("deleteCountry - should throw ResourceNotFoundException when ID does not exist")
    void deleteCountry_notFound() {
        when(countryRepository.existsById(99)).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> countryService.deleteCountry(99)
        );

        verify(countryRepository, times(1)).existsById(99);
        verify(countryRepository, never()).deleteById(anyInt());
    }
}
