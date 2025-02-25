package com.example.power;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@Repository
public interface PowerRepository extends CrudRepository<Power, Integer> {
    @Query("SELECT p FROM Power p WHERE p.name = :name")
    Optional<Power> findByName(String name);

    @NotNull
    List<Power> findAll();
}
