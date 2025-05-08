package test.tenpo.percentajecalculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CalculatorServiceTest {

    @Mock
    private PercentageService percentageService;

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        calculatorService = new CalculatorService(percentageService);
    }

    @Test
    void calculatePercentage_shouldReturnCorrectCalculation() {
        Long numA = 100L;
        Long numB = 200L;
        Long percentage = 10L;
        when(percentageService.retrievePercentage()).thenReturn(percentage);

        Long result = calculatorService.calculatePercentage(numA, numB);

        assertEquals(3000L, result);
        verify(percentageService, times(1)).retrievePercentage();
    }

    @Test
    void calculatePercentage_shouldHandleZeroValues() {
        Long numA = 0L;
        Long numB = 0L;
        Long percentage = 10L;
        when(percentageService.retrievePercentage()).thenReturn(percentage);

        Long result = calculatorService.calculatePercentage(numA, numB);

        assertEquals(0L, result);
        verify(percentageService, times(1)).retrievePercentage();
    }

    @Test
    void calculatePercentage_shouldHandleZeroPercentage() {
        Long numA = 100L;
        Long numB = 200L;
        Long percentage = 0L;
        when(percentageService.retrievePercentage()).thenReturn(percentage);

        Long result = calculatorService.calculatePercentage(numA, numB);

        assertEquals(0L, result);
        verify(percentageService, times(1)).retrievePercentage();
    }

    @Test
    void calculatePercentage_shouldHandleNegativeValues() {
        Long numA = -100L;
        Long numB = 200L;
        Long percentage = 10L;
        when(percentageService.retrievePercentage()).thenReturn(percentage);

        Long result = calculatorService.calculatePercentage(numA, numB);

        assertEquals(1000L, result);
        verify(percentageService, times(1)).retrievePercentage();
    }
}