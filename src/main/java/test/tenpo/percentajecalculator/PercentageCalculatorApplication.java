package test.tenpo.percentajecalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PercentageCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PercentageCalculatorApplication.class, args);
    }

}
