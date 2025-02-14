package com.example.pokemon;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.micronaut.http.HttpRequest.*;
import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class PokemonControllerTest {
    @Client("/pokemon")
    @Inject
    HttpClient httpClient;

    Pokemon pokemonCreated;

    @BeforeEach
    void setUp() {
        PokemonCreationForm pokemon = new PokemonCreationForm("Ivyasaur", "Fire", "imgUrl");
        pokemonCreated = httpClient.toBlocking().exchange(POST("/", pokemon), Pokemon.class).body();
    }

    @AfterEach
    void setup() {
        httpClient.toBlocking().exchange(DELETE("/delete-all"));
    }

    @Test
    void get_all_pokemons() {
        HttpResponse<List<Pokemon>> response = httpClient.toBlocking().exchange(
                GET(""),
                Argument.listOf(Pokemon.class)
        );

        assertThat(response.getStatus().getCode()).isEqualTo(200);
        List<Pokemon> pokemonList = response.body();

        assertThat(pokemonList).isNotNull();
        assertThat(pokemonList.size()).isGreaterThanOrEqualTo(1);
    }


    @Test
    void create_pokemon() {
        PokemonCreationForm pokemon = new PokemonCreationForm("BulbaSour" + Math.random(), "Fire", "imgUrl");
        HttpResponse<Pokemon> pokemonResponse =
                httpClient.toBlocking().exchange(POST("", pokemon), Pokemon.class);
        assertThat(pokemonResponse.getStatus().getCode()).isEqualTo(201);
        Pokemon createdPokemon = pokemonResponse.body();
        assertThat(createdPokemon).isNotNull();
        assertThat(createdPokemon.getId()).isNotNull();
        assertThat(createdPokemon.getName()).isEqualTo(pokemon.getName());
        assertThat(createdPokemon.getPower().getName()).isEqualTo(pokemon.getPower());
        assertThat(createdPokemon.getImageUrl()).isEqualTo(pokemon.getImageUrl());
    }

    @Test
    void update_pokemon() {
        PokemonUpdationForm pokemonToUpdate = new PokemonUpdationForm(pokemonCreated.getId(), pokemonCreated.getName(), "Water", "abc.png");

        HttpResponse<Pokemon> response = httpClient.toBlocking().exchange(
                PUT("", pokemonToUpdate),
                Pokemon.class
        );

        assertThat(response.getStatus().getCode()).isEqualTo(201);
        Pokemon updatedPokemon = response.body();

        assertThat(updatedPokemon).isNotNull();
        assertThat(updatedPokemon.getName()).isEqualTo(pokemonToUpdate.getName());
        assertThat(updatedPokemon.getPower().getName()).isEqualTo(pokemonToUpdate.getPower());
        assertThat(updatedPokemon.getImageUrl()).isEqualTo(pokemonToUpdate.getImageUrl());
    }


    @Test
    void getPokemonById() {
        HttpResponse<Pokemon> response = httpClient.toBlocking().exchange(GET("" + pokemonCreated.getId()), Pokemon.class);

        assertThat(response.getStatus().getCode()).isEqualTo(200);

        Pokemon pokemon = response.body();
        assertThat(pokemon).isNotNull();
        assertThat(pokemon.getId()).isEqualTo(pokemonCreated.getId());
        assertThat(pokemon.getName()).isEqualTo("Ivyasaur");
        assertThat(pokemon.getImageUrl())
                .isEqualTo("imgUrl");
        assertThat(pokemon.getPower().getName()).isEqualTo("Fire");
    }

    @Test
    void deletePokemonById() {
        HttpResponse<?> response = httpClient.toBlocking().exchange(DELETE("" + pokemonCreated.getId()));
        assertThat(response.getStatus().getCode()).isEqualTo(200);
    }
}
