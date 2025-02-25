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
        pokemonController.getAll();

        verify(pokemonService).getAll();
    }

    @Test
    void get_pokemon_by_id_found() {
        Power power = new Power("fire");
        Pokemon mockPokemon = new Pokemon(1, "Pikachu", power, "Pikachu.png");
        when(pokemonService.getById(1)).thenReturn(mockPokemon);

        HttpResponse<Pokemon> response = pokemonController.getById(1);

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("Pikachu", Objects.requireNonNull(response.body()).getName());
        verify(pokemonService).getById(1);
    }

    @Test
    void create_pokemon() {
        Power power = new Power("fire");
        PokemonCreationForm form = new PokemonCreationForm("Bulbasaur", power.getName(), "imgUrl");
        Pokemon createdPokemon = new Pokemon(3, "Bulbasaur", power, "image.url");
        when(pokemonService.create(form)).thenReturn(createdPokemon);

        HttpResponse<Pokemon> response = pokemonController.create(form);

        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("Bulbasaur", Objects.requireNonNull(response.body()).getName());
        verify(pokemonService).create(form);
    }

    @Test
    void update_pokemon() {
        Power power = new Power("fire");
        PokemonUpdationForm form = new PokemonUpdationForm(1, "Raichu", power.getName(), "image.url");
        Pokemon updatedPokemon = new Pokemon(1, "Raichu", power, "pokemon.url");
        when(pokemonService.update(form)).thenReturn(updatedPokemon);

        HttpResponse<Pokemon> response = pokemonController.update(form);

        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("Raichu", Objects.requireNonNull(response.body()).getName());
        verify(pokemonService).update(form);
    }

    @Test
    void delete_pokemon() {
        HttpResponse<Void> response = pokemonController.delete(1);

        assertEquals(HttpStatus.OK, response.getStatus());
        verify(pokemonService).delete(1);
    }
    @Test
    void delete_all_pokemon() {
        HttpResponse<Void> response = pokemonController.deleteAll();

        assertEquals(HttpStatus.OK, response.getStatus());
        verify(pokemonService).deleteAll();
    }

}
