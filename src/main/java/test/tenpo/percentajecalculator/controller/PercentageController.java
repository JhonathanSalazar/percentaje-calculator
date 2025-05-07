package test.tenpo.percentajecalculator.controller;

import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import test.tenpo.percentajecalculator.model.ResponseWrapper;
import test.tenpo.percentajecalculator.service.CalculatorService;

@RestController("api/v1/percentage")
public class PercentageController {

    private final CalculatorService calculatorService;

    public PercentageController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("calculate")
    public ResponseEntity<ResponseWrapper> calculate(
            @RequestParam(value = "numA") Long numA,
            @RequestParam(value = "numB") Long numB
    ) {
        // Service layer -> Calculate percentage
        Long calculatedValue = calculatorService.calculatePercentage(numA, numB);

        ResponseWrapper response = ResponseWrapper.builder()
                .data(calculatedValue)
                .build();

        return ResponseEntity.ok(response);
    }
}
