package com.example.power;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

@MicronautTest
class PowerControllerTest {
    Power createdPower;

    @BeforeEach
    void setup(RequestSpecification requestSpecification) {
        String powerName = "TestPower";

        Response response = RestAssured.given(requestSpecification)
                .contentType(ContentType.JSON)
                .body(powerName)
                .when()
                .post("/power")
                .then().extract()
                .response();

        createdPower=response.as(Power.class);

    }

    @AfterEach
    void tearDown(RequestSpecification requestSpecification) {
        RestAssured.given(requestSpecification)
                .when()
                .delete("/power/delete-all");
    }

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
        Assertions.assertThat(pokemons.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void addPower(RequestSpecification requestSpecification) {
        String powerName = "Electric";

        Power createdPower =
                RestAssured.given(requestSpecification)
                        .contentType(ContentType.JSON)
                        .body(powerName)
                        .when()
                        .post("/power")
                        .then()
                        .assertThat()
                        .statusCode(201)
                        .extract()
                        .as(Power.class);

        Assertions.assertThat(createdPower).isNotNull();
        Assertions.assertThat(createdPower.getName()).isEqualTo(powerName);
    }

    @Test
    void deletePower(RequestSpecification requestSpecification) {
                RestAssured.given(requestSpecification)
                        .contentType(ContentType.JSON)
                        .when()
                        .delete("/power/"+createdPower.getId())
                        .then()
                        .assertThat()
                        .statusCode(200);
    }
}