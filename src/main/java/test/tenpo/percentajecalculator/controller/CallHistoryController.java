package test.tenpo.percentajecalculator.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import test.tenpo.percentajecalculator.model.CallHistory;
import test.tenpo.percentajecalculator.model.PagedResponse;
import test.tenpo.percentajecalculator.model.ResponseWrapper;
import test.tenpo.percentajecalculator.service.CallHistoryService;

@RestController
@RequestMapping("/api/v1/call-history")
public class CallHistoryController {

    private final CallHistoryService callHistoryService;

    public CallHistoryController(CallHistoryService callHistoryService) {
        this.callHistoryService = callHistoryService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getCallHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<CallHistory> callHistoryPage = callHistoryService.getCallHistory(page, size);

        PagedResponse<CallHistory> pagedResponse = PagedResponse.<CallHistory>builder()
                .content(callHistoryPage.getContent())
                .page(page)
                .size(size)
                .totalElements(callHistoryPage.getTotalElements())
                .totalPages(callHistoryPage.getTotalPages())
                .last(callHistoryPage.isLast())
                .build();

        ResponseWrapper response = ResponseWrapper.builder()
                .data(pagedResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
