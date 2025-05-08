package test.tenpo.percentajecalculator.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import test.tenpo.percentajecalculator.model.CallHistory;
import test.tenpo.percentajecalculator.service.CallHistoryService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CallHistoryController.class)
public class CallHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CallHistoryService callHistoryService;

    @Test
    public void testGetCallHistoryWithPagination() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        CallHistory call1 = CallHistory.builder()
                .id(1L)
                .callDate(now)
                .endpoint("/api/v1/percentage")
                .parameters("param1=value1")
                .response("response1")
                .build();
        CallHistory call2 = CallHistory.builder()
                .id(2L)
                .callDate(now.minusMinutes(5))
                .endpoint("/api/v1/percentage")
                .parameters("param2=value2")
                .response("response2")
                .build();
        CallHistory call3 = CallHistory.builder()
                .id(3L)
                .callDate(now.minusMinutes(10))
                .endpoint("/api/v1/percentage")
                .parameters("param3=value3")
                .response("response3")
                .build();

        List<CallHistory> calls = Arrays.asList(call1, call2, call3);
        Page<CallHistory> page = new PageImpl<>(calls, PageRequest.of(0, 10), 3);

        when(callHistoryService.getCallHistory(anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/v1/call-history")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content", hasSize(3)))
                .andExpect(jsonPath("$.data.page", is(0)))
                .andExpect(jsonPath("$.data.size", is(10)))
                .andExpect(jsonPath("$.data.totalElements", is(3)))
                .andExpect(jsonPath("$.data.totalPages", is(1)))
                .andExpect(jsonPath("$.data.last", is(true)))
                .andExpect(jsonPath("$.data.content[0].id", is(1)))
                .andExpect(jsonPath("$.data.content[1].id", is(2)))
                .andExpect(jsonPath("$.data.content[2].id", is(3)));
    }
}
