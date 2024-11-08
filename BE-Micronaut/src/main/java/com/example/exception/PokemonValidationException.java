package com.example.exception;

public class PokemonValidationException extends PokemonException {

  public PokemonValidationException(String errorMessage) {
    super(errorMessage);
  }
}
