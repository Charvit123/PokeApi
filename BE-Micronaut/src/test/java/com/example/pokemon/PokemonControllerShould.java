package com.example.pokemon;

import com.example.power.Power;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PokemonControllerShould {

    @Mock
    PokemonService pokemonService;

    @InjectMocks
    PokemonController pokemonController;


    @Test
    void get_all_pokemon() {
        // Arrange
        pokemonController.getAll();

        verify(pokemonService).get();
    }

    @Test
    void get_pokemon_by_id_found() {
        // Arrange
        Power power = new Power(1L, "fire");
        Pokemon mockPokemon = new Pokemon(1, "Pikachu", power, "Pikachu.png");
        when(pokemonService.getById(1)).thenReturn(mockPokemon);

        // Act
        HttpResponse<Pokemon> response = pokemonController.getById(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Pikachu", Objects.requireNonNull(response.body()).getName());
        verify(pokemonService).getById(1);
    }

    @Test
    void create_pokemon() {
        // Arrange
        Power power = new Power(1L, "fire");
        PokemonCreationForm form = new PokemonCreationForm("Bulbasaur", power.getName());
        Pokemon createdPokemon = new Pokemon(3, "Bulbasaur", power, "image.url");
        when(pokemonService.create(form)).thenReturn(createdPokemon);

        // Act
        HttpResponse<Pokemon> response = pokemonController.create(form);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("Bulbasaur", Objects.requireNonNull(response.body()).getName());
        verify(pokemonService).create(form);
    }

    @Test
    void update_pokemon() {
        // Arrange
        Power power = new Power(1L, "fire");
        PokemonUpdationForm form = new PokemonUpdationForm(1, "Raichu", power.getName(), "image.url");
        Pokemon updatedPokemon = new Pokemon(1, "Raichu", power, "pokemon.url");
        when(pokemonService.update(form)).thenReturn(updatedPokemon);

        // Act
        HttpResponse<Pokemon> response = pokemonController.update(form);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("Raichu", Objects.requireNonNull(response.body()).getName());
        verify(pokemonService).update(form);
    }

    @Test
    void delete_pokemon() {
        // Act
        HttpResponse<Pokemon> response = pokemonController.delete(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatus());
        verify(pokemonService).delete(1);
    }

}
