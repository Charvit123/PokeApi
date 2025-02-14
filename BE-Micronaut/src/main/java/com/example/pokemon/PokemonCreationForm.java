package com.example.pokemon;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PokemonCreationForm {
    private final String name;

    private final String power;
    private final String imageUrl;

    @JsonCreator
    public PokemonCreationForm(
            @JsonProperty("name") String name, @JsonProperty("power") String power, @JsonProperty("imageUrl") String imageUrl) {
        this.name = name;
        this.power = power;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getPower() {
        return power;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
