package test.tenpo.percentajecalculator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import test.tenpo.percentajecalculator.model.CallHistory;
import test.tenpo.percentajecalculator.repository.CallHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CallHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(CallHistoryService.class);
    private final CallHistoryRepository callHistoryRepository;

    public CallHistoryService(CallHistoryRepository callHistoryRepository) {
        this.callHistoryRepository = callHistoryRepository;
    }

    @Async
    public void logApiCall(String endpoint, String parameters, String response) {
        try {
            CallHistory callHistory = CallHistory.builder()
                    .callDate(LocalDateTime.now())
                    .endpoint(endpoint)
                    .parameters(parameters)
                    .response(response)
                    .build();
            
            callHistoryRepository.save(callHistory);
        } catch (Exception e) {
            logger.error("Error logging API call: {}", e.getMessage(), e);
        }
    }

    public List<CallHistory> getCallHistory() {
        return callHistoryRepository.findAll();
    }
}