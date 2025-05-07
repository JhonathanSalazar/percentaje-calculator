package test.tenpo.percentajecalculator.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ResponseWrapper {
    private Object data;
    private List<ErrorDetails> errors;
}
