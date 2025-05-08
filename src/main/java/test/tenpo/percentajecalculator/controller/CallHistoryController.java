package test.tenpo.percentajecalculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Call History", description = "API to retrieve call history with pagination")
public class CallHistoryController {

    private final CallHistoryService callHistoryService;

    public CallHistoryController(CallHistoryService callHistoryService) {
        this.callHistoryService = callHistoryService;
    }

    @Operation(
        summary = "Get call history",
        description = "Retrieves a paginated list of API call history records"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved call history",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResponseWrapper.class)
            )
        ),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<ResponseWrapper> getCallHistory(
            @Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "10") int size) {

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
