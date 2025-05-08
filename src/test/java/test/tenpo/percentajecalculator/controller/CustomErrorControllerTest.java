package test.tenpo.percentajecalculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import test.tenpo.percentajecalculator.config.JacksonConfig;
import test.tenpo.percentajecalculator.service.CallHistoryService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomErrorController.class)
@Import(JacksonConfig.class)
public class CustomErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CallHistoryService callHistoryService;

    @Test
    public void testHandleError404() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("jakarta.servlet.error.status_code", 404);
        request.setAttribute("jakarta.servlet.error.request_uri", "/non-existent-path");

        mockMvc.perform(MockMvcRequestBuilders.get("/error")
                .requestAttr("jakarta.servlet.error.status_code", 404)
                .requestAttr("jakarta.servlet.error.request_uri", "/non-existent-path")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0].message").value("Path not found"))
                .andExpect(jsonPath("$.errors[0].rejectedValue").value("/non-existent-path"))
                .andExpect(jsonPath("$.errors[0].field").value("path"));

        verify(callHistoryService).logApiCall(anyString(), anyString(), anyString());
    }

    @Test
    public void testHandleError500() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("jakarta.servlet.error.status_code", 500);
        request.setAttribute("jakarta.servlet.error.request_uri", "/some-path");

        mockMvc.perform(MockMvcRequestBuilders.get("/error")
                .requestAttr("jakarta.servlet.error.status_code", 500)
                .requestAttr("jakarta.servlet.error.request_uri", "/some-path")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors[0].message").value("An error occurred"))
                .andExpect(jsonPath("$.errors[0].rejectedValue").value("/some-path"))
                .andExpect(jsonPath("$.errors[0].field").value("path"));

        verify(callHistoryService).logApiCall(anyString(), anyString(), anyString());
    }
}
