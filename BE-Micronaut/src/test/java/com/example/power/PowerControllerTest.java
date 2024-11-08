package com.example.power;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@MicronautTest
class PowerControllerTest {
  @Test
  void getAll(RequestSpecification requestSpecification) {
    List<Power> pokemons =
        List.of(
            RestAssured.given(requestSpecification)
                .when()
                .get("/power")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(Power[].class));
    Assertions.assertThat(pokemons.size()).isNotNegative();
  }
}