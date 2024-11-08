package com.example.pokemon;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

// @Singleton
@Repository
public interface PokemonRepository extends CrudRepository<Pokemon, Integer> {
  boolean existsByNameIgnoreCase(String name);

  Optional<Pokemon> findByNameIgnoreCase(String name);
}
