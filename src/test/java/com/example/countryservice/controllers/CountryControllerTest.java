package com.example.countryservice.controllers;

import com.example.countryservice.DTO.CountryDTO;
import com.example.countryservice.exceptions.ResourceNotFoundException;
import com.example.countryservice.models.Country;
import com.example.countryservice.services.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryController.class)
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CountryService countryService;

    @Autowired
    private ObjectMapper objectMapper;

    private Country country1;
    private Country country2;

    @BeforeEach
    void setUp() {
        country1 = new Country(1, "Tunisia", "Tunis");
        country2 = new Country(2, "France", "Paris");
    }

    // ========== GET /api/getcountries ==========

    @Test
    @DisplayName("GET /api/getcountries - should return 200 with list of countries")
    void getAllCountries_200() throws Exception {
        when(countryService.getAllCountries()).thenReturn(Arrays.asList(country1, country2));

        mockMvc.perform(get("/api/getcountries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Tunisia")))
                .andExpect(jsonPath("$[1].name", is("France")));

        verify(countryService, times(1)).getAllCountries();
    }

    // ========== GET /api/getcountries/{id} ==========

    @Test
    @DisplayName("GET /api/getcountries/{id} - should return 200 when country found")
    void getCountryById_200() throws Exception {
        when(countryService.getCountryById(1)).thenReturn(country1);

        mockMvc.perform(get("/api/getcountries/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Tunisia")))
                .andExpect(jsonPath("$.capital", is("Tunis")));

        verify(countryService, times(1)).getCountryById(1);
    }

    @Test
    @DisplayName("GET /api/getcountries/{id} - should return 404 when country not found")
    void getCountryById_404() throws Exception {
        when(countryService.getCountryById(99))
                .thenThrow(new ResourceNotFoundException("Country with id 99 not found"));

        mockMvc.perform(get("/api/getcountries/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("99")));

        verify(countryService, times(1)).getCountryById(99);
    }

    // ========== GET /api/getcountries/countryname ==========

    @Test
    @DisplayName("GET /api/getcountries/countryname?name=Tunisia - should return 200")
    void getCountryByName_200() throws Exception {
        when(countryService.getCountryByName("Tunisia")).thenReturn(country1);

        mockMvc.perform(get("/api/getcountries/countryname").param("name", "Tunisia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Tunisia")))
                .andExpect(jsonPath("$.capital", is("Tunis")));

        verify(countryService, times(1)).getCountryByName("Tunisia");
    }

    // ========== POST /api/addcountry ==========

    @Test
    @DisplayName("POST /api/addcountry - should return 201 with valid body")
    void addCountry_201() throws Exception {
        CountryDTO dto = new CountryDTO(null, "Tunisia", "Tunis");
        when(countryService.addCountry(any(CountryDTO.class))).thenReturn(country1);

        mockMvc.perform(post("/api/addcountry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Tunisia")))
                .andExpect(jsonPath("$.capital", is("Tunis")));

        verify(countryService, times(1)).addCountry(any(CountryDTO.class));
    }

    @Test
    @DisplayName("POST /api/addcountry - should return 400 with invalid body (blank name)")
    void addCountry_400() throws Exception {
        CountryDTO invalidDTO = new CountryDTO(null, "", "Tunis");

        mockMvc.perform(post("/api/addcountry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(countryService, never()).addCountry(any(CountryDTO.class));
    }

    // ========== PUT /api/updatecountry/{id} ==========

    @Test
    @DisplayName("PUT /api/updatecountry/{id} - should return 200 on success")
    void updateCountry_200() throws Exception {
        CountryDTO dto = new CountryDTO(null, "Tunisie", "Tunis City");
        Country updated = new Country(1, "Tunisie", "Tunis City");
        when(countryService.updateCountry(eq(1), any(CountryDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/updatecountry/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Tunisie")))
                .andExpect(jsonPath("$.capital", is("Tunis City")));

        verify(countryService, times(1)).updateCountry(eq(1), any(CountryDTO.class));
    }

    // ========== DELETE /api/deletecountry/{id} ==========

    @Test
    @DisplayName("DELETE /api/deletecountry/{id} - should return 204 on success")
    void deleteCountry_204() throws Exception {
        doNothing().when(countryService).deleteCountry(1);

        mockMvc.perform(delete("/api/deletecountry/1"))
                .andExpect(status().isNoContent());

        verify(countryService, times(1)).deleteCountry(1);
    }
}
