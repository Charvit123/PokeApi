package com.example.pokemon;

import com.example.exception.PokemonValidationException;
import com.example.power.Power;
import com.example.power.PowerService;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Singleton
public class PokemonService {

    private static final Logger log = LoggerFactory.getLogger(Pokemon.class);

    private final PokemonRepository pokemonRepository;

    private final PowerService powerService;

    public PokemonService(PokemonRepository pokemonRepository, PowerService powerService) {

        this.pokemonRepository = pokemonRepository;
        this.powerService = powerService;
    }

    public List<Pokemon> getAll() {
        return (List<Pokemon>) pokemonRepository.findAll();
    }

    @Transactional
    public Pokemon create(PokemonCreationForm pokemonForm) {
        boolean isPokemonExist = pokemonRepository.existsByNameIgnoreCase(pokemonForm.getName());
        if (isPokemonExist) {
            throw new PokemonValidationException(
                    "Pokemon With name: " + pokemonForm.getName() + " Already Exist");
        }

        Power power = powerService.getOrCreate(pokemonForm.getPower());
        Pokemon pokemon = new Pokemon();
        pokemon.setName(pokemonForm.getName());
        pokemon.setPower(power);
        pokemon.setImageUrl(pokemonForm.getImageUrl());
        return pokemonRepository.save(pokemon);
    }

    public Pokemon getById(Integer id) {
        return pokemonRepository
                .findById(id)
                .orElseThrow(() -> new PokemonValidationException("Pokemon with id: " + id + " Not found"));
    }

    @Transactional
    public Pokemon update(PokemonUpdationForm pokemonForm) {
        Pokemon pokemonWithId = pokemonRepository
                .findById(pokemonForm.getId())
                .orElseThrow(() -> new PokemonValidationException(
                        "Pokemon With id: " + pokemonForm.getId() + " does not exist"));

        Pokemon pokemonWithNameExist = pokemonRepository
                .findByNameIgnoreCase(pokemonForm.getName())
                .orElse(null);

        if (pokemonWithNameExist != null && !Objects.equals(pokemonWithNameExist.getId(), pokemonWithId.getId())) {
            throw new PokemonValidationException(
                    "Pokemon with name: " + pokemonForm.getName() + " exists more than once");
        }

        pokemonWithId.setName(pokemonForm.getName());
        pokemonWithId.setPower(powerService.getOrCreate(pokemonForm.getPower()));
        pokemonWithId.setImageUrl(pokemonForm.getImageUrl());

        return pokemonRepository.update(pokemonWithId);
    }


    public void delete(Integer id) {
        boolean isPokemonExist = pokemonRepository.existsById(id);
        if (!isPokemonExist) {
            throw new PokemonValidationException("Pokemon with id " + id + " Not Found");
        }
        pokemonRepository.deleteById(id);
    }

    public void deleteAll() {
        pokemonRepository.deleteAll();
    }
}
