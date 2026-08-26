package com.example.it_iap.controller;

import com.example.it_iap.dto.auth.response.TwoFactorResponse;
import org.springframework.web.bind.annotation.*;

import com.example.it_iap.dto.ApiResponse;
import com.example.it_iap.dto.auth.request.TwoFactorRequest;
import com.example.it_iap.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.it_iap.dto.auth.request.ResetTwoFactorRequest;

@RestController
@RequestMapping("/api/2fa")
@RequiredArgsConstructor
public class TotpController {
    private final AuthService authService;

    @Operation(summary = "Thiết lập xác thực 2 bước")
    @PostMapping("/setup")
    public ResponseEntity<ApiResponse<TwoFactorResponse>> setup() {
        TwoFactorResponse response = authService.setup2fa();
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ApiResponse.<TwoFactorResponse>builder()
                    .message("Yêu cầu thiết lập xác minh 2 bước thành công")
                    .data(response)
                    .build());
    }

    @Operation(
        summary = "Xác nhận xác thực 2 bước",
        description = "[TEST API] dán secret của api /setup vào https://stefansundin.github.io/2fa-qr/"
    )
    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<Void>> confirm(@RequestBody @Valid TwoFactorRequest request) {
        authService.confirm2fa(request);
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ApiResponse.<Void>builder()
                    .message("Bật xác thực 2 bước thành công")
                    .build());
    }

    @Operation(summary = "Hủy xác thực 2 bước")
    @PostMapping("/disable")
    public ResponseEntity<ApiResponse<Void>> disable(@RequestBody @Valid TwoFactorRequest request) {
        authService.disable2fa(request);
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ApiResponse.<Void>builder()
                    .message("Hủy xác thực 2 bước thành công")
                    .build());
    }

    @Operation(summary = "Lấy trạng thái xác thực 2 bước của tài khoản")
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Boolean>> status() {
        boolean data = authService.status2fa();
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ApiResponse.<Boolean>builder()
                    .message("Lấy trạng thái xác thực 2 bước thành công")
                    .data(data)
                    .build());
    }

    @Operation(
        summary = "Yêu cầu khôi phục / gỡ 2FA qua Email",
        description = "Gửi email xác thực khôi phục 2FA có hiệu lực 10 phút"
    )
    @PostMapping("/request-reset")
    public ResponseEntity<ApiResponse<Void>> requestReset() {
        authService.requestReset2fa();
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ApiResponse.<Void>builder()
                    .message("Yêu cầu xử lý xác thực 2 bước thành công")
                    .build());
    }

    @Operation(
        summary = "Xác nhận gỡ 2FA (Bắt đầu đếm ngược 24h)",
        description = "Xác nhận từ link email, hệ thống sẽ đưa vào đếm ngược 24 giờ trước khi gỡ hẳn 2FA"
    )
    @PostMapping("/confirm-reset")
    public ResponseEntity<ApiResponse<Void>> confirmReset(@RequestBody @Valid ResetTwoFactorRequest request) {
        authService.confirmReset2fa(request);
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ApiResponse.<Void>builder()
                    .message("Xác nhận gỡ xác thực 2 bước thành công")
                    .build());
    }

    @Operation(
        summary = "Từ chối / Hủy yêu cầu gỡ 2FA (Không phải tôi)",
        description = "Hủy bỏ đợt yêu cầu gỡ 2FA và giữ nguyên trạng thái an toàn cho tài khoản"
    )
    @PostMapping("/cancel-reset")
    public ResponseEntity<ApiResponse<Void>> cancelReset(@RequestBody @Valid ResetTwoFactorRequest request) {
        authService.cancelReset2fa(request);
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ApiResponse.<Void>builder()
                    .message("Hủy bỏ xử lý xác thực 2 bước thành công")
                    .build());
    }
}