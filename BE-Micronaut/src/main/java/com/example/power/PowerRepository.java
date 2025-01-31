package com.example.power;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Repository
public interface PowerRepository extends CrudRepository<Power, Long> {
    Power findByName(String name);

    @NotNull
    List<Power> findAll();
}
