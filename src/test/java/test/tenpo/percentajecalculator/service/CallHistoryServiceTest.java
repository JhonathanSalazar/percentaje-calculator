package test.tenpo.percentajecalculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import test.tenpo.percentajecalculator.model.CallHistory;
import test.tenpo.percentajecalculator.repository.CallHistoryRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CallHistoryServiceTest {

    @Mock
    private CallHistoryRepository callHistoryRepository;

    private CallHistoryService callHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        callHistoryService = new CallHistoryService(callHistoryRepository);
    }

    @Test
    void logApiCall_shouldSaveCallHistory() {
        String endpoint = "/api/test";
        String parameters = "param1=value1&param2=value2";
        String response = "{\"result\": \"success\"}";
        
        when(callHistoryRepository.save(any(CallHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        callHistoryService.logApiCall(endpoint, parameters, response);
        
        ArgumentCaptor<CallHistory> callHistoryCaptor = ArgumentCaptor.forClass(CallHistory.class);
        verify(callHistoryRepository, timeout(1000)).save(callHistoryCaptor.capture());
        
        CallHistory savedCallHistory = callHistoryCaptor.getValue();
        assertNotNull(savedCallHistory);
        assertEquals(endpoint, savedCallHistory.getEndpoint());
        assertEquals(parameters, savedCallHistory.getParameters());
        assertEquals(response, savedCallHistory.getResponse());
        assertNotNull(savedCallHistory.getCallDate());
    }

    @Test
    void logApiCall_shouldHandleException() {
        String endpoint = "/api/test";
        String parameters = "param1=value1";
        String response = "{\"result\": \"success\"}";
        
        when(callHistoryRepository.save(any(CallHistory.class))).thenThrow(new RuntimeException("Database error"));
        
        callHistoryService.logApiCall(endpoint, parameters, response);
        
        verify(callHistoryRepository).save(any(CallHistory.class));
    }

    @Test
    void getCallHistory_shouldReturnPagedResults() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by("callDate").descending());
        
        CallHistory call1 = CallHistory.builder()
                .id(1L)
                .callDate(LocalDateTime.now().minusMinutes(5))
                .endpoint("/api/test1")
                .parameters("param1=value1")
                .response("{\"result\": \"success1\"}")
                .build();
                
        CallHistory call2 = CallHistory.builder()
                .id(2L)
                .callDate(LocalDateTime.now())
                .endpoint("/api/test2")
                .parameters("param2=value2")
                .response("{\"result\": \"success2\"}")
                .build();
                
        List<CallHistory> calls = Arrays.asList(call1, call2);
        Page<CallHistory> expectedPage = new PageImpl<>(calls, pageable, calls.size());
        
        when(callHistoryRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);
        
        Page<CallHistory> result = callHistoryService.getCallHistory(page, size);
        
        assertEquals(2, result.getTotalElements());
        assertEquals(calls, result.getContent());
        verify(callHistoryRepository).findAll(any(Pageable.class));
    }

    @Test
    void getCallHistory_shouldUseCorrectPageable() {
        int page = 2;
        int size = 15;
        
        callHistoryService.getCallHistory(page, size);
        
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(callHistoryRepository).findAll(pageableCaptor.capture());
        
        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(page, capturedPageable.getPageNumber());
        assertEquals(size, capturedPageable.getPageSize());
    }
}