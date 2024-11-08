package com.example.exception.handler;

import com.example.exception.PokemonException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {PokemonException.class})
public class PokemonExceptionHandler
    implements ExceptionHandler<PokemonException, HttpResponse<PokemonException>> {
  @Override
  public HttpResponse<PokemonException> handle(HttpRequest request, PokemonException exception) {
    return HttpResponse.badRequest(exception);
  }
}
