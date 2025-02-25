package com.example.power;

import com.example.exception.PokemonValidationException;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class PowerService {

    private final PowerRepository powerRepository;


    public PowerService(PowerRepository powerRepository) {
        this.powerRepository = powerRepository;
    }

    public List<Power> getAll() {
        return powerRepository.findAll();
    }

    public Power getOrCreate(String name) {
        Optional<Power> existingPower = powerRepository.findByName(name);
        return existingPower.orElseGet(() -> powerRepository.save(new Power(name)));
    }

    public void delete(Integer id) {
        boolean isPowerExist = powerRepository.existsById(id);
        if (!isPowerExist) {
            throw new PokemonValidationException("Power with id " + id + " Not Found");
        }
        powerRepository.deleteById(id);
    }

    public void deleteAll() {
        powerRepository.deleteAll();
    }
}
