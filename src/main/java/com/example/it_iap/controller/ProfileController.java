package com.example.it_iap.controller;

import com.example.it_iap.dto.ApiResponse;
import com.example.it_iap.dto.profile.request.CreateProfileRequest;
import com.example.it_iap.dto.profile.request.UpdateProfileRequest;
import com.example.it_iap.dto.profile.response.ProfileResponse;
import com.example.it_iap.dto.profile.response.ProfileSummaryResponse;
import com.example.it_iap.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    @Operation(summary = "Tạo hồ sơ mới", description = "Tạo mới một hồ sơ ứng viên vào hệ thống")
    @PostMapping()
    public ResponseEntity<ApiResponse<ProfileResponse>> createProfile(
            @RequestBody @Valid CreateProfileRequest request) {
        ProfileResponse response = profileService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<ProfileResponse>builder()
                                .code(201)
                                .message("Tạo hồ sơ mới thành công")
                                .data(response)
                                .build());
    }

    @Operation(summary = "Cập nhật hồ sơ", description = "Cập nhật thông tin chi tiết của hồ sơ dựa theo profileId")
    @PutMapping("/{profileId}")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            @RequestBody @Valid UpdateProfileRequest request,
            @PathVariable long profileId) {
        ProfileResponse response = profileService.updateProfile(request,
                profileId);
        return ResponseEntity.ok(
                ApiResponse.<ProfileResponse>builder()
                        .message("Cập nhật hồ sơ thành công")
                        .data(response)
                        .build());
    }

    @Operation(summary = "Xóa hồ sơ")
    @DeleteMapping("/{profileId}")
    public ResponseEntity<?> deleteProfile(@PathVariable long profileId) {
        profileService.deleteProfile(profileId);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .message("Xóa hồ sơ thành công")
                        .build());
    }

    @Operation(summary = "Lấy chi tiết một hồ sơ", description = "Trả về thông tin chi tiết đầy đủ của một hồ sơ ứng viên")
    @GetMapping("/{profileId}")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(@PathVariable long profileId) {
        ProfileResponse response = profileService.getProfile(profileId);
        return ResponseEntity.ok(
                ApiResponse.<ProfileResponse>builder()
                        .message("Xem chi tiết hồ sơ thành công")
                        .data(response)
                        .build());
    }

    @Operation(summary = "Lấy danh sách hồ sơ", description = "Trả về danh sách thu gọn của tất cả các hồ sơ hiện có")
    @GetMapping()
    public ResponseEntity<ApiResponse<List<ProfileSummaryResponse>>> getAllProfile() {
        List<ProfileSummaryResponse> response = profileService.getAllProfiles();
        return ResponseEntity.ok(
                ApiResponse.<List<ProfileSummaryResponse>>builder()
                        .message("Lấy danh sách hồ sơ thành công")
                        .data(response)
                        .build());
    }
}
