package com.example.exception;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class PokemonException extends RuntimeException {
  @JsonProperty(value = "msg")
  private final String message;

  public PokemonException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
