package test.tenpo.percentajecalculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import test.tenpo.percentajecalculator.model.ResponseWrapper;
import test.tenpo.percentajecalculator.service.CalculatorService;

@RestController
@RequestMapping("/api/v1/percentage")
@Tag(name = "Percentage Calculator", description = "API to calculate percentages")
public class PercentageController {

    private final CalculatorService calculatorService;

    public PercentageController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @Operation(
        summary = "Calculate percentage",
        description = "Calculates a percentage based on two input numbers and returns the result"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Calculation successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResponseWrapper.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/calculate")
    public ResponseEntity<ResponseWrapper> calculate(
            @Parameter(description = "First number for calculation") @RequestParam(value = "numA") Long numA,
            @Parameter(description = "Second number for calculation") @RequestParam(value = "numB") Long numB
    ) {
        Long calculatedValue = calculatorService.calculatePercentage(numA, numB);

        ResponseWrapper response = ResponseWrapper.builder()
                .data(calculatedValue)
                .build();

        return ResponseEntity.ok(response);
    }
}
