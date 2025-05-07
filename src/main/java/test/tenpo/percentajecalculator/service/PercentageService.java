package test.tenpo.percentajecalculator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class PercentageService {

    @Value("${percentage-api.url}")
    private String percentageApiURL;

    private final RestTemplate restTemplate;

    private Long lastKnownPercentage;

    @Autowired
    public PercentageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "percentageCache", key = "'percentage'")
    public Long retrievePercentage() {
        try {
            Integer[] response = restTemplate.getForObject(percentageApiURL, Integer[].class);
            Long percentage = response != null && response.length > 0 ? response[0].longValue() : 0L;

            this.lastKnownPercentage = percentage;
            return percentage;
        } catch (Exception e) {
            if (lastKnownPercentage != null) {
                return lastKnownPercentage;
            }

            throw new RestClientException("Failed to retrieve percentage from external API and no cached value available: " + e.getMessage(), e);
        }
    }
}
