package com.example.power;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PowerControllerShould {

  @Mock
  PowerService powerService;

  @InjectMocks
  PowerController powerController;

  @Test
  void get_all_powers() {
    List<Power> mockPowers = List.of(new Power("Fire"), new Power("Water"));
    when(powerService.getAll()).thenReturn(mockPowers);

    HttpResponse<List<Power>> response = powerController.getAll();

    assertEquals(HttpStatus.OK, response.getStatus());
    assertEquals(2, Objects.requireNonNull(response.body()).size());
    verify(powerService).getAll();
  }

  @Test
  void create_power() {
    Power createdPower = new Power("Electric");
    when(powerService.getOrCreate("Electric")).thenReturn(createdPower);

    HttpResponse<Power> response = powerController.getOrCreatePower("Electric");

    assertEquals(HttpStatus.CREATED, response.getStatus());
    assertEquals("Electric", Objects.requireNonNull(response.body()).getName());
    verify(powerService).getOrCreate("Electric");
  }


  @Test
  void delete_power() {
    HttpResponse<Void> response = powerController.delete(1);

    assertEquals(HttpStatus.OK, response.getStatus());
    verify(powerService).delete(1);
  }

  @Test
  void delete_all_powers() {
    HttpResponse<Void> response = powerController.deleteAll();

    assertEquals(HttpStatus.OK, response.getStatus());
    verify(powerService).deleteAll();
  }
}
