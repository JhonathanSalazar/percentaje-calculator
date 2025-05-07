package test.tenpo.percentajecalculator.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PercentageService {
    public Long retrievePercentage() {
        return new Random().nextLong(1, 201);
    }
}
