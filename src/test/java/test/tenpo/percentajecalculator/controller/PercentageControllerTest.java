package test.tenpo.percentajecalculator.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import test.tenpo.percentajecalculator.service.CalculatorService;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PercentageController.class)
public class PercentageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CalculatorService calculatorService;

    @Test
    public void testCalculateSuccess() throws Exception {
        // Given
        Long numA = 10L;
        Long numB = 20L;
        Long expectedResult = 60L; // Example result
        
        when(calculatorService.calculatePercentage(numA, numB)).thenReturn(expectedResult);

        // When & Then
        mockMvc.perform(get("/api/v1/percentage/calculate")
                .param("numA", numA.toString())
                .param("numB", numB.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(expectedResult));
    }

    @Test
    public void testCalculateWithNegativeNumbers() throws Exception {
        // Given
        Long numA = -10L;
        Long numB = 20L;
        Long expectedResult = 20L; // Example result
        
        when(calculatorService.calculatePercentage(numA, numB)).thenReturn(expectedResult);

        // When & Then
        mockMvc.perform(get("/api/v1/percentage/calculate")
                .param("numA", numA.toString())
                .param("numB", numB.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(expectedResult));
    }

    @Test
    public void testCalculateWithZeroValues() throws Exception {
        // Given
        Long numA = 0L;
        Long numB = 0L;
        Long expectedResult = 0L; // Example result
        
        when(calculatorService.calculatePercentage(numA, numB)).thenReturn(expectedResult);

        // When & Then
        mockMvc.perform(get("/api/v1/percentage/calculate")
                .param("numA", numA.toString())
                .param("numB", numB.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(expectedResult));
    }

    @Test
    public void testCalculateWithLargeNumbers() throws Exception {
        // Given
        Long numA = 1000000L;
        Long numB = 2000000L;
        Long expectedResult = 6000000L; // Example result
        
        when(calculatorService.calculatePercentage(numA, numB)).thenReturn(expectedResult);

        // When & Then
        mockMvc.perform(get("/api/v1/percentage/calculate")
                .param("numA", numA.toString())
                .param("numB", numB.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(expectedResult));
    }

    @Test
    public void testCalculateWithMissingParameters() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/percentage/calculate")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}