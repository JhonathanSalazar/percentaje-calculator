package test.tenpo.percentajecalculator.service;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    private final PercentageService percentageService;

    public CalculatorService(PercentageService percentageService) {
        this.percentageService = percentageService;
    }

    public Long calculatePercentage(Long numA, Long numB) {
        Long percentage = percentageService.retrievePercentage();
        return percentage * (numA + numB);
    }
}
