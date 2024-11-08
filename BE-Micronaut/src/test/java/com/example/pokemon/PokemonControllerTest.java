package com.example.pokemon;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class PokemonControllerTest {

  @Test
  void getAll(@NotNull RequestSpecification requestSpecification) {
    List<Pokemon> pokemons =
        List.of(
            RestAssured.given(requestSpecification)
                .when()
                .get("/pokemon")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(Pokemon[].class));
    assertThat(pokemons.size()).isNotNegative();
  }

  @Test
  void createPokemon(@NotNull RequestSpecification requestSpecification) {
    PokemonCreationForm pokemon = new PokemonCreationForm("Ivyasaur6", "Fire");
    Pokemon createdPokemon =
        RestAssured.given(
                requestSpecification.header("Content-type", "application/json").body(pokemon))
            .when()
            .post("/pokemon")
            .then()
            .assertThat()
            .statusCode(201)
            .extract()
            .as(Pokemon.class);

    assertThat(createdPokemon.getName()).isEqualTo(pokemon.getName());
    assertThat(createdPokemon.getPower().getName()).isEqualTo(pokemon.getPower());
    assertThat(createdPokemon.getId()).isNotNull();
    assertThat(createdPokemon.getImageUrl()).isNotNull();
  }

  @Test
  void updatePokemon(@NotNull RequestSpecification requestSpecification) {

    PokemonUpdationForm pokemonToUpdate = new PokemonUpdationForm(18, "Bulbasaur4", "Water", "abc.png");
    Pokemon updatedPokemon =
        RestAssured.given(
                requestSpecification
                    .header("Content-type", "application/json")
                    .body(pokemonToUpdate))
            .when()
            .put("/pokemon")
            .then()
            .extract()
            .as(Pokemon.class);

    assertThat(updatedPokemon.getName()).isEqualTo(pokemonToUpdate.getName());
    assertThat(updatedPokemon.getPower().getName())
        .isEqualTo(pokemonToUpdate.getPower());
    assertThat(updatedPokemon.getId()).isNotNull();
    assertThat(updatedPokemon.getImageUrl()).isEqualTo(pokemonToUpdate.getImageUrl());
  }

  @Test
  void getPokemonById(@NotNull RequestSpecification requestSpecification) {
    Pokemon pokemon =
        RestAssured.given(requestSpecification)
            .when()
            .get("/pokemon/19")
            .then()
            .assertThat()
            .statusCode(200)
            .extract()
            .as(Pokemon.class);

    assertThat(pokemon.getId()).isEqualTo(19);
    assertThat(pokemon.getName()).isEqualTo("Ivyasaur");
    assertThat(pokemon.getImageUrl())
        .isEqualTo(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/19.png");
    assertThat(pokemon.getPower().getName()).isEqualTo("Fire");
  }

  @Test
  void deletePokemonById(@NotNull RequestSpecification requestSpecification) {
    RestAssured.given(requestSpecification)
        .when()
        .delete("/pokemon/22")
        .then()
        .assertThat()
        .statusCode(200);
  }
}
