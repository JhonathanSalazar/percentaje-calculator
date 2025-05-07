package test.tenpo.percentajecalculator.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorDetails {
    private String message;
    private String rejectedValue;
    private String field;
}
