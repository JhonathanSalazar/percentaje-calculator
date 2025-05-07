package test.tenpo.percentajecalculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class PercentageServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private PercentageService percentageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        percentageService = new PercentageService(restTemplate);
        ReflectionTestUtils.setField(percentageService, "randomNumberApiUrl", "http://test-url");
    }

    @Test
    void retrievePercentage_shouldReturnValueFromApi() {
        Integer[] mockResponse = new Integer[]{42};
        when(restTemplate.getForObject(anyString(), eq(Integer[].class))).thenReturn(mockResponse);

        Long result = percentageService.retrievePercentage();
        assertEquals(42L, result);
    }

    @Test
    void retrievePercentage_shouldReturnZeroWhenApiReturnsNull() {
        when(restTemplate.getForObject(anyString(), eq(Integer[].class))).thenReturn(null);
        Long result = percentageService.retrievePercentage();
        assertEquals(0L, result);
    }

    @Test
    void retrievePercentage_shouldReturnZeroWhenApiReturnsEmptyArray() {
        Integer[] mockResponse = new Integer[]{};
        when(restTemplate.getForObject(anyString(), eq(Integer[].class))).thenReturn(mockResponse);
        Long result = percentageService.retrievePercentage();
        assertEquals(0L, result);
    }
}
