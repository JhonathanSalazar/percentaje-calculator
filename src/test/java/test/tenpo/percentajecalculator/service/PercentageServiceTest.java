package test.tenpo.percentajecalculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PercentageServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private PercentageService percentageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        percentageService = new PercentageService(restTemplate);
        ReflectionTestUtils.setField(percentageService, "percentageApiURL", "http://test-url");
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

    @Test
    void retrievePercentage_shouldThrowRestClientExceptionWhenApiFails() {
        when(restTemplate.getForObject(anyString(), eq(Integer[].class))).thenThrow(new RuntimeException("API connection failed"));

        RestClientException exception = assertThrows(RestClientException.class, () -> percentageService.retrievePercentage());

        assertEquals("Failed to retrieve percentage from external API and no cached value available: API connection failed", exception.getMessage());
    }

    @Test
    void retrievePercentage_shouldUseCachedValueWhenApiFails() {
        Integer[] mockResponse = new Integer[]{42};
        when(restTemplate.getForObject(anyString(), eq(Integer[].class)))
                .thenReturn(mockResponse);

        Long firstResult = percentageService.retrievePercentage();
        assertEquals(42L, firstResult);

        when(restTemplate.getForObject(anyString(), eq(Integer[].class)))
                .thenThrow(new RuntimeException("API connection failed"));

        Long secondResult = percentageService.retrievePercentage();
        assertEquals(42L, secondResult);
    }

    @Test
    void retrievePercentage_shouldCacheResultsForMultipleCalls() {
        Integer[] mockResponse = new Integer[]{42};
        when(restTemplate.getForObject(anyString(), eq(Integer[].class))).thenReturn(mockResponse);

        Long result1 = percentageService.retrievePercentage();
        Long result2 = percentageService.retrievePercentage();
        Long result3 = percentageService.retrievePercentage();

        assertEquals(42L, result1);
        assertEquals(42L, result2);
        assertEquals(42L, result3);

        verify(restTemplate, times(3)).getForObject(anyString(), eq(Integer[].class));
    }
}
