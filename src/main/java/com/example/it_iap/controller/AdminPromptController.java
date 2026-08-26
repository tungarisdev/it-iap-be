package com.example.it_iap.controller;

import com.example.it_iap.dto.ApiResponse;
import com.example.it_iap.dto.adminPrompt.request.CreateAdminPromptRequest;
import com.example.it_iap.dto.adminPrompt.request.SearchAdminPromptRequest;
import com.example.it_iap.dto.adminPrompt.response.AdminPromptResponse;
import com.example.it_iap.dto.adminPrompt.response.AdminPromptSummaryResponse;
import com.example.it_iap.dto.promptVersion.request.CreatePromptVersionRequest;
import com.example.it_iap.entity.enums.PromptUseCase;
import com.example.it_iap.service.AdminPromptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin-prompts")
@Tag(name = "Quản lý câu lệnh quản trị viên")
@RequiredArgsConstructor
public class AdminPromptController {

    private final AdminPromptService adminPromptService;

    @Operation(
        summary = "Tạo mới một Admin Prompt gốc",
        description = "Tạo một cấu hình Prompt gốc mới (VD: CUSTOMER_SUPPORT) đi kèm với phiên bản (version) đầu tiên của nó."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<AdminPromptSummaryResponse>> createAdminPrompt(@RequestBody @Valid CreateAdminPromptRequest request) {
        AdminPromptSummaryResponse data = adminPromptService.createAdminPrompt(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.<AdminPromptSummaryResponse>builder()
                .code(201)
                .message("Tạo mới câu lệnh cấu hình thành công")
                .data(data)
                .build());
    }

    @Operation(
        summary = "Thêm phiên bản mới (Version) cho Prompt gốc",
        description = "Tạo và đính kèm một phiên bản mới (VD: version 0.0.2) cho một Admin Prompt đã tồn tại dựa vào ID của Prompt đó."
    )
    @PostMapping("/versions")
    public ResponseEntity<ApiResponse<AdminPromptResponse>> addNewPromptVersion(@Valid @RequestBody CreatePromptVersionRequest request) {
        AdminPromptResponse data = adminPromptService.addNewVersion(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.<AdminPromptResponse>builder()
                .code(201)
                .message("Tạo phiên bản mới câu lệnh cấu hình thành công")
                .data(data)
                .build());
    }

    @Operation(
        summary = "Tìm kiếm và phân trang danh sách Prompt",
        description = "Tìm kiếm danh sách các Admin Prompt. Hỗ trợ lọc theo từ khóa (promptKey), mục đích sử dụng (applyFor) và trạng thái (active). Phân trang mặc định 10 records/trang."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AdminPromptSummaryResponse>>> searchAdminPrompts(@ModelAttribute @Valid SearchAdminPromptRequest request) {
        PromptUseCase promptUseCase = PromptUseCase.from(request.getApplyFor());
        Page<AdminPromptSummaryResponse> data = adminPromptService.searchAdminPrompts(request.getPromptKey(),
            promptUseCase,
            request.getActive(),
            request.getPages());
        return ResponseEntity.ok(ApiResponse.<Page<AdminPromptSummaryResponse>>builder()
            .message("Lấy danh sách câu lệnh cấu hình thành công")
            .data(data)
            .build());
    }
}
