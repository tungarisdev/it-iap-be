package com.example.it_iap.controller;

import com.example.it_iap.dto.notification.request.CreateNotificationRequest;
import com.example.it_iap.dto.notification.request.ReadNotificationRequest;
import com.example.it_iap.dto.notification.response.AdminGetNotificationResponse;
import com.example.it_iap.dto.notification.response.ReadNotificationResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.it_iap.dto.ApiResponse;
import com.example.it_iap.dto.notification.response.NotificationSliceResponse;
import com.example.it_iap.dto.notification.response.NotificationResponse;
import com.example.it_iap.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(
        summary = "Lấy thông báo"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<NotificationSliceResponse<NotificationResponse>>> getNotification(
        @RequestParam(defaultValue = "1") @Min(1) int page) {
        NotificationSliceResponse<NotificationResponse> response = notificationService.getNotification(page);
        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.<NotificationSliceResponse<NotificationResponse>>builder()
                .message("Lấy thông báo thành công")
                .data(response)
                .build());
    }

    @Operation(summary = "Đọc thông báo")
    @PutMapping
    public ResponseEntity<ApiResponse<ReadNotificationResponse>> readNotification(
        @RequestBody ReadNotificationRequest request) {
        ReadNotificationResponse response = notificationService.readNotification(request);
        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.<ReadNotificationResponse>builder()
                .message("Đọc thông báo thành công")
                .data(response)
                .build());
    }

    @Operation(summary = "Đọc tất cả thông báo")
    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> readAllNotification() {
        notificationService.readAllNotification();
        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.<Void>builder()
                .message("Đọc tất cả thông báo thành công")
                .build());
    }

    @Operation(summary = "Admin tạo thông báo cho toàn bộ người dùng") // Tạm thời gửi toàn bộ sau custom thêm
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponse>> createNotification(
        @RequestBody @Valid CreateNotificationRequest request) {
        notificationService.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.<NotificationResponse>builder()
                .code(201)
                .message("Tạo thông báo thành công")
                .build());
    }

    @Operation(summary = "Admin lấy thông báo đã tạo từ admin hoặc system")
    // Tạm thời lấy toàn bộ không lọc sau custom thêm
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<Page<AdminGetNotificationResponse>>> adminGetNotification(
        @RequestParam(defaultValue = "1") @Min(1) int page,
        @RequestParam(defaultValue = "10") @Max(50) int size) {
        Page<AdminGetNotificationResponse> response = notificationService.adminGetNotification(page,
            size);
        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.<Page<AdminGetNotificationResponse>>builder()
                .message("Lấy thông báo đã tạo thành công")
                .data(response)
                .build());
    }

    @Operation(summary = "Admin xóa thông báo theo identify code")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{identifyCode}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable String identifyCode) {
        notificationService.deleteNotification(identifyCode);
        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.<Void>builder()
                .message("Xóa thông báo thành công")
                .build());
    }
}
