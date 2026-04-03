package com.example.countryservice.repository;

import com.example.countryservice.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    Country findByName(String name);
}
