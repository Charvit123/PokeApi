package com.example.power;

import com.example.exception.PokemonValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PowerServiceShould {
    @Mock
    PowerRepository powerRepository;
    @InjectMocks
    PowerService powerService;

    Power mockPower;

    @BeforeEach
    void setUp() {
        mockPower = new Power("fire");
        powerService = new PowerService(powerRepository);
    }

    @Test
    void get_power() {
        when(powerRepository.findByName(anyString())).thenReturn(Optional.ofNullable(mockPower));

        Power returnedPower = powerService.getOrCreate("fire");

        verify(powerRepository).findByName(anyString());

        assertThat(returnedPower).isEqualTo(mockPower);
    }

    @Test
    void get_all_power() {
        when(powerRepository.findAll()).thenReturn(List.of(mockPower));

        List<Power> returnedPower = powerService.getAll();

        verify(powerRepository).findAll();

        assertThat(returnedPower).isEqualTo(List.of(mockPower));
    }


    @Test
    void add_power_successfully_when_not_exists() {
        when(powerRepository.findByName("Fire")).thenReturn(Optional.empty());

        Power newPower = new Power("Fire");
        when(powerRepository.save(any(Power.class))).thenReturn(newPower);

        Power savedPower = powerService.getOrCreate("Fire");

        assertEquals("Fire", savedPower.getName());
        verify(powerRepository).save(any(Power.class));
    }

    @Test
    void delete_power_successfully_when_exists() {
        Integer powerId = 1;
        when(powerRepository.existsById(powerId)).thenReturn(true);

        powerService.delete(powerId);

        verify(powerRepository).deleteById(powerId);
    }

    @Test
    void throw_exception_when_deleting_non_existent_power() {
        Integer powerId = 1;
        when(powerRepository.existsById(powerId)).thenReturn(false);

        PokemonValidationException exception = assertThrows(
                PokemonValidationException.class, () -> powerService.delete(powerId)
        );

        assertEquals("Power with id 1 Not Found", exception.getMessage());
        verify(powerRepository, never()).deleteById(any());
    }

    @Test
    void delete_all_powers() {
        powerService.deleteAll();

        verify(powerRepository).deleteAll();
    }
}
