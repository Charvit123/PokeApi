package com.example.pokemon;

import com.example.exception.PokemonValidationException;
import com.example.power.Power;
import com.example.power.PowerRepository;
import com.example.power.PowerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonServiceShould {

  @Mock
  PokemonRepository pokemonRepository;
  @Mock
  PowerRepository powerRepository;
  @Mock
  PowerService powerService;
  @InjectMocks
  PokemonService pokemonService;
  Power power;
  Pokemon pikachuPokemon;
  Pokemon bulbasaurPokemon;
  PokemonUpdationForm pokemonUpdationForm;

  List<Pokemon> pokemons;

  PokemonCreationForm pokemonCreationForm;

  @BeforeEach
  void setUp() {

    power = new Power(1L, "fire");

    pikachuPokemon = new Pokemon(1, "Pikachu", power, "Pikachu.png");

    bulbasaurPokemon = new Pokemon(2, "Bulbasaur", power, "Bulbasaur.png");

    pokemons = List.of(pikachuPokemon, bulbasaurPokemon);

    pokemonCreationForm =
        new PokemonCreationForm(pikachuPokemon.getName(), pikachuPokemon.getPower().getName());

    pokemonUpdationForm = new PokemonUpdationForm(bulbasaurPokemon.getId(), bulbasaurPokemon.getName(), "Fire", bulbasaurPokemon.getImageUrl());

    powerService = new PowerService(powerRepository);

    pokemonService = new PokemonService(pokemonRepository, powerService);
  }

  @Test
  @DisplayName("Test Get All Pokemons")
  void get() {

    when(pokemonRepository.findAll()).thenReturn(pokemons);

    List<Pokemon> retunedPokemons = pokemonService.get();

    verify(pokemonRepository).findAll();

    assertThat(retunedPokemons).isEqualTo(pokemons);
  }

  @Test
  @DisplayName("Test Create Pokemon")
  void create() {

    when(powerRepository.findByName(anyString())).thenReturn(power);

    when(pokemonRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);

    when(pokemonRepository.save(any())).thenReturn(pikachuPokemon);

    Pokemon returnedPokemon = pokemonService.create(pokemonCreationForm);

    verify(pokemonRepository).save(any());
    verify(powerRepository).findByName(anyString());
    verify(pokemonRepository).existsByNameIgnoreCase(anyString());

    assertThat(returnedPokemon).isEqualTo(pikachuPokemon);
  }

  @Test
  @DisplayName("Test Get Pokemon By Id")
  void getById() {
    when(pokemonRepository.findById(anyInt())).thenReturn(Optional.ofNullable(pikachuPokemon));

    Pokemon returnedPokemon = pokemonService.getById(1);

    verify(pokemonRepository).findById(anyInt());

    assertThat(returnedPokemon).isEqualTo(pikachuPokemon);
  }

  @Test
  @DisplayName("Test Update Pokemon")
  void update() {

    when(pokemonRepository.findById(anyInt())).thenReturn(Optional.ofNullable(bulbasaurPokemon));
    when(powerRepository.findByName(anyString())).thenReturn(power);


    when(pokemonRepository.findByNameIgnoreCase(anyString()))
        .thenReturn(Optional.ofNullable(bulbasaurPokemon));

    when(pokemonRepository.update(any())).thenReturn(bulbasaurPokemon);

    Pokemon updatedPokemon = pokemonService.update(pokemonUpdationForm);

    verify(pokemonRepository).update(any());
    verify(pokemonRepository).findById(anyInt());
    verify(pokemonRepository).findByNameIgnoreCase(anyString());
    verify(powerRepository).findByName(anyString());


    assertThat(updatedPokemon).isEqualTo(bulbasaurPokemon);
  }

  @Test
  @DisplayName("Test Delete Pokemon")
  void delete() {

    when(pokemonRepository.existsById(anyInt())).thenReturn(true);

    pokemonService.delete(anyInt());

    verify(pokemonRepository).deleteById(anyInt());
  }

  @Test
  @DisplayName("Test Pokemon-name Already exists")
  void pokemonWithNameExistsException() {
    when(pokemonRepository.existsByNameIgnoreCase(anyString())).thenReturn(true);

    assertThatThrownBy(() -> pokemonService.create(pokemonCreationForm));
    catchThrowableOfType(
        () -> pokemonService.create(pokemonCreationForm), PokemonValidationException.class);
  }

  @Test
  @DisplayName("Test Pokemon-Id could not find in update")
  void pokemonIdNotFoundException() {

    when(pokemonRepository.findById(anyInt())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> pokemonService.update(pokemonUpdationForm));

    catchThrowableOfType(
        () -> pokemonService.update(pokemonUpdationForm), PokemonValidationException.class);
  }

  @Test
  @DisplayName("Test Pokemon-name Already found")
  void pokemonNameAlreadyFound() {
    when(pokemonRepository.findById(anyInt())).thenReturn(Optional.ofNullable(bulbasaurPokemon));

    when(pokemonRepository.findByNameIgnoreCase(anyString()))
        .thenReturn(Optional.ofNullable(pikachuPokemon));

    assertThatThrownBy(() -> pokemonService.update(pokemonUpdationForm));

    catchThrowableOfType(
        () -> pokemonService.update(pokemonUpdationForm), PokemonValidationException.class);
  }

  @Test
  @DisplayName("Test Pokemon-Id does not exist ")
  void pokemonIdNotExists() {
    when(pokemonRepository.existsById(anyInt())).thenReturn(false);

    assertThatThrownBy(() -> pokemonService.delete(anyInt()));

    catchThrowableOfType(
        () -> pokemonService.delete(anyInt()), PokemonValidationException.class);
  }

  @Test
  @DisplayName("Test Update Pokemon-power ")
  void pokemonPowerUpdate() {
    when(pokemonRepository.findById(anyInt())).thenReturn(Optional.ofNullable(bulbasaurPokemon));

    when(pokemonRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

    when(pokemonRepository.update(any())).thenReturn(bulbasaurPokemon);

    Pokemon updatedPokemon = pokemonService.update(pokemonUpdationForm);

    verify(pokemonRepository).update(any());
    verify(pokemonRepository).findById(anyInt());
    verify(pokemonRepository).findByNameIgnoreCase(anyString());

    assertThat(updatedPokemon).isEqualTo(bulbasaurPokemon);
  }

  @Test
  @DisplayName("Test Pokemon-Id does not found in getById")
  void pokemonIdNotFound() {
    when(pokemonRepository.findById(anyInt()))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> pokemonService.getById(anyInt()));

    catchThrowableOfType(
        () -> pokemonService.getById(anyInt()), PokemonValidationException.class);
  }
}
