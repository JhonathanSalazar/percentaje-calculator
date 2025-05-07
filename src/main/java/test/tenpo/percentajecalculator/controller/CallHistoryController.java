package test.tenpo.percentajecalculator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.tenpo.percentajecalculator.model.CallHistory;
import test.tenpo.percentajecalculator.model.ResponseWrapper;
import test.tenpo.percentajecalculator.service.CallHistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/call-history")
public class CallHistoryController {

    private final CallHistoryService callHistoryService;

    public CallHistoryController(CallHistoryService callHistoryService) {
        this.callHistoryService = callHistoryService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getCallHistory() {
        List<CallHistory> callHistory = callHistoryService.getCallHistory();
        
        ResponseWrapper response = ResponseWrapper.builder()
                .data(callHistory)
                .build();
                
        return ResponseEntity.ok(response);
    }
}