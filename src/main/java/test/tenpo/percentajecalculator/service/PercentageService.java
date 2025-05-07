package test.tenpo.percentajecalculator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class PercentageService {

    @Value("${percentage-api.url}")
    private String percentageApiURL;

    private final RestTemplate restTemplate;

    @Autowired
    public PercentageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Long retrievePercentage() {
        try {
            Integer[] response = restTemplate.getForObject(percentageApiURL, Integer[].class);
            return response != null && response.length > 0 ? response[0].longValue() : 0L;
        } catch (Exception e) {
            throw new RestClientException("Failed to retrieve percentage from external API: " + e.getMessage(), e);
        }
    }
}
