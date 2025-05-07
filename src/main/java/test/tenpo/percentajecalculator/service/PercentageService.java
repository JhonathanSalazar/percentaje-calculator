package test.tenpo.percentajecalculator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PercentageService {

    @Value("${random.number.api.url}")
    private String randomNumberApiUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public PercentageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Long retrievePercentage() {
        Integer[] response = restTemplate.getForObject(randomNumberApiUrl, Integer[].class);
        return response != null && response.length > 0 ? response[0].longValue() : 0L;
    }
}
