package com.example.power;

import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class PowerService {

    private final PowerRepository powerRepository;


    public PowerService(PowerRepository powerRepository) {
        this.powerRepository = powerRepository;
    }

    public Power get(String name) {
        return powerRepository.findByName(name);
    }

    public List<Power> getAll() {
        return powerRepository.findAll();
    }
}
