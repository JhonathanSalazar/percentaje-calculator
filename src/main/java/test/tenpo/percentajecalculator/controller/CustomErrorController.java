package test.tenpo.percentajecalculator.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import test.tenpo.percentajecalculator.model.ErrorDetails;
import test.tenpo.percentajecalculator.model.ResponseWrapper;
import test.tenpo.percentajecalculator.service.CallHistoryService;

import java.util.Collections;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);
    private final CallHistoryService callHistoryService;
    private final ObjectMapper objectMapper;

    public CustomErrorController(CallHistoryService callHistoryService, ObjectMapper objectMapper) {
        this.callHistoryService = callHistoryService;
        this.objectMapper = objectMapper;
    }

    @RequestMapping("/error")
    public ResponseEntity<ResponseWrapper> handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String path = (String) request.getAttribute("jakarta.servlet.error.request_uri");
        String errorMessage = statusCode == 404 ? "Path not found" : "An error occurred";

        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(errorMessage)
                .rejectedValue(path)
                .field("path")
                .build();

        ResponseWrapper responseWrapper = ResponseWrapper.builder()
                .errors(Collections.singletonList(errorDetails))
                .build();

        // Convert response to JSON for logging
        try {
            String responseJson = objectMapper.writeValueAsString(responseWrapper);

            // Log the error asynchronously
            callHistoryService.logApiCall(path, "", responseJson);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing response: {}", e.getMessage(), e);
        }

        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
        return new ResponseEntity<>(responseWrapper, httpStatus);
    }
}
