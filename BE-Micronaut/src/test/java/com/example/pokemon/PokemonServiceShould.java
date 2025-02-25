package com.example.pokemon;

import com.example.exception.PokemonValidationException;
import com.example.power.Power;
import com.example.power.PowerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonServiceShould {

    @Mock
    private PokemonRepository pokemonRepository;

    @Mock
    private PowerService powerService;

    @InjectMocks
    private PokemonService pokemonService;

    private Power firePower;
    private Pokemon pikachu;
    private Pokemon bulbasaur;
    private PokemonCreationForm creationForm;
    private PokemonUpdationForm updationForm;

    @BeforeEach
    void setUp() {
        firePower = new Power("Fire");
        pikachu = new Pokemon(1, "Pikachu", firePower, "pikachu.png");
        bulbasaur = new Pokemon(2, "Bulbasaur", firePower, "bulbasaur.png");

        creationForm = new PokemonCreationForm("Pikachu", firePower.getName(), "pikachu.png");
        updationForm = new PokemonUpdationForm(2, "Bulbasaur", firePower.getName(), "bulbasaur.png");
    }

    @Test
    void get_all_pokemons() {
        when(pokemonRepository.findAll()).thenReturn(List.of(pikachu, bulbasaur));

        List<Pokemon> returnedPokemons = pokemonService.getAll();

        assertEquals(2, returnedPokemons.size());
        verify(pokemonRepository).findAll();
    }

    @Test
    void create_pokemon_successfully() {
        when(powerService.getOrCreate("Fire")).thenReturn(firePower);
        when(pokemonRepository.existsByNameIgnoreCase("Pikachu")).thenReturn(false);
        when(pokemonRepository.save(any())).thenReturn(pikachu);

        Pokemon createdPokemon = pokemonService.create(creationForm);

        assertEquals("Pikachu", createdPokemon.getName());
        verify(pokemonRepository).save(any());
    }

    @Test
    void throw_exception_when_creating_duplicate_pokemon() {
        when(pokemonRepository.existsByNameIgnoreCase("Pikachu")).thenReturn(true);

        PokemonValidationException exception = assertThrows(
                PokemonValidationException.class, () -> pokemonService.create(creationForm)
        );

        assertEquals("Pokemon With name: Pikachu Already Exist", exception.getMessage());
        verify(pokemonRepository, never()).save(any());
    }

    @Test
    void get_pokemon_by_id_successfully() {
        when(pokemonRepository.findById(1)).thenReturn(Optional.of(pikachu));

        Pokemon foundPokemon = pokemonService.getById(1);

        assertEquals("Pikachu", foundPokemon.getName());
        verify(pokemonRepository).findById(1);
    }

    @Test
    void throw_exception_when_getting_non_existent_pokemon_by_id() {
        when(pokemonRepository.findById(3)).thenReturn(Optional.empty());

        PokemonValidationException exception = assertThrows(
                PokemonValidationException.class, () -> pokemonService.getById(3)
        );

        assertEquals("Pokemon with id: 3 Not found", exception.getMessage());
        verify(pokemonRepository).findById(3);
    }

    @Test
    void update_pokemon_successfully() {
        when(pokemonRepository.findById(2)).thenReturn(Optional.of(bulbasaur));
        when(powerService.getOrCreate("Fire")).thenReturn(firePower);
        when(pokemonRepository.update(any())).thenReturn(bulbasaur);

        Pokemon updatedPokemon = pokemonService.update(updationForm);

        assertEquals("Bulbasaur", updatedPokemon.getName());
        verify(pokemonRepository).update(any());
    }

    @Test
    void throw_exception_when_updating_non_existent_pokemon() {
        when(pokemonRepository.findById(5)).thenReturn(Optional.empty());

        PokemonValidationException exception = assertThrows(
                PokemonValidationException.class, () -> pokemonService.update(new PokemonUpdationForm(5, "Raichu", "Electric", "raichu.png"))
        );

        assertEquals("Pokemon With id: 5 does not exist", exception.getMessage());
        verify(pokemonRepository, never()).update(any());
    }

    @Test
    void delete_pokemon_successfully() {
        when(pokemonRepository.existsById(1)).thenReturn(true);

        pokemonService.delete(1);

        verify(pokemonRepository).deleteById(1);
    }

    @Test
    void throw_exception_when_deleting_non_existent_pokemon() {
        when(pokemonRepository.existsById(10)).thenReturn(false);

        PokemonValidationException exception = assertThrows(
                PokemonValidationException.class, () -> pokemonService.delete(10)
        );

        assertEquals("Pokemon with id 10 Not Found", exception.getMessage());
        verify(pokemonRepository, never()).deleteById(any());
    }

    @Test
    void delete_all_pokemons() {
        pokemonService.deleteAll();

        verify(pokemonRepository).deleteAll();
    }

}
