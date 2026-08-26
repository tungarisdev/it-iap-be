package com.example.it_iap.controller;

import com.example.it_iap.dto.ApiResponse;
import com.example.it_iap.dto.ai.request.GenerateQuestionRequest;
import com.example.it_iap.entity.enums.TargetLevel;
import com.example.it_iap.entity.enums.TargetPosition;
import com.example.it_iap.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {
    private final QuestionService questionService;

    @Operation(
        summary = "Tạo câu hỏi phỏng vấn tự động bằng AI",
        description = "Gọi AI sinh ra một số lượng câu hỏi nhất định dựa trên vị trí (Position) và cấp độ (Level) yêu cầu, sau đó lưu trực tiếp vào Database."
    )
    @PostMapping("/generate-question")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> generateQuestion(@RequestBody @Valid GenerateQuestionRequest request) {
        TargetLevel targetLevel = TargetLevel.fromString(request.getLevel());
        TargetPosition targetPosition = TargetPosition.fromString(request.getPosition());
        questionService.generateAndSaveAiQuestions(request.getQuantity(),
            targetLevel,
            targetPosition);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                ApiResponse.<Void>builder()
                    .code(201)
                    .message("Sinh câu hỏi phỏng vấn thành công")
                    .build());
    }
}
