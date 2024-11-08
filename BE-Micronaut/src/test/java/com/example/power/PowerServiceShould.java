package com.example.power;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PowerServiceShould {
  @Mock
  PowerRepository powerRepository;
  @InjectMocks
  PowerService powerService;

  Power mockPower;

  @BeforeEach
  void setUp() {
    mockPower = new Power(1L, "fire");
    powerService = new PowerService(powerRepository);
  }

  @Test
  void getPower() {

    Mockito.when(powerRepository.findByName(anyString())).thenReturn(mockPower);

    Power returnedPower = powerService.get("fire");

    verify(powerRepository).findByName(anyString());

    assertThat(returnedPower).isEqualTo(mockPower);
  }

  @Test
  void getAllPower() {

    Mockito.when(powerRepository.findAll()).thenReturn(List.of(mockPower));

    List<Power> returnedPower = powerService.getAll();

    verify(powerRepository).findAll();

    assertThat(returnedPower).isEqualTo(List.of(mockPower));
  }
}
