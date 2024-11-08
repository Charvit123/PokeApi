package com.example.power;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PowerControllerShould {

  @Mock
  PowerService powerService;
  @InjectMocks
  PowerController powerController;

  @Test
  void get_all_powers() {
    powerController.getAll();
    verify(powerService).getAll();
  }
}
