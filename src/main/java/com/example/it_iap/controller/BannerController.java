package com.example.it_iap.controller;

import com.example.it_iap.dto.ApiResponse;
import com.example.it_iap.dto.banner.request.BannerRequest;
import com.example.it_iap.dto.banner.response.BannerResponse;
import com.example.it_iap.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    @Operation(
        summary = "Lấy danh sách Banner [ADMIN]",
        description = "Admin lấy danh sách tất cả banner có phân trang"
    )
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<ApiResponse<Page<BannerResponse>>> getAllBanners(
        @RequestParam(defaultValue = "0") int page
    ) {
        Page<BannerResponse> data = bannerService.getAllBanners(page);
        return ResponseEntity.ok(
            ApiResponse.<Page<BannerResponse>>builder()
                .message("Láy danh sách banner thành công")
                .data(data)
                .build()
        );
    }

    @Operation(
        summary = "Lấy Banner đang hoạt động [PUBLIC]",
        description = "Lấy banner đang được active để hiển thị ra trang chủ"
    )
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<BannerResponse>> getActiveBanner() {
        BannerResponse data = bannerService.getActiveBanner();
        return ResponseEntity.ok(
            ApiResponse.<BannerResponse>builder()
                .message("Lấy banner thành công")
                .data(data)
                .build()
        );
    }

    @Operation(
        summary = "Tạo mới Banner [ADMIN]",
        description = "Admin tạo một banner mới (Hỗ trợ upload ảnh bằng form-data)"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<ApiResponse<BannerResponse>> createBanner(
        @Valid @ModelAttribute BannerRequest request
    ) {
        BannerResponse data = bannerService.createBanner(request);
        return ResponseEntity.ok(
            ApiResponse.<BannerResponse>builder()
                .message("Tạo banner thành công")
                .data(data)
                .build()
        );
    }

    @Operation(
        summary = "Cập nhật thông tin Banner [ADMIN]",
        description = "Admin cập nhật tiêu đề, nội dung hoặc ảnh của banner"
    )
    @PutMapping(value = "/{bannerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<ApiResponse<BannerResponse>> updateBannerInfo(
        @PathVariable Long bannerId,
        @Valid @ModelAttribute BannerRequest request
    ) {
        BannerResponse data = bannerService.updateBannerInfo(bannerId,
            request);
        return ResponseEntity.ok(
            ApiResponse.<BannerResponse>builder()
                .message("Cập nhật thông tin banner thành công")
                .data(data)
                .build()
        );
    }

    @Operation(
        summary = "Bật/Tắt trạng thái Banner [ADMIN]",
        description = "Admin thay đổi nhanh trạng thái active của banner"
    )
    @PatchMapping("/{bannerId}/status")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> changeActiveStatus(
        @PathVariable Long bannerId,
        @RequestParam boolean isActive
    ) {
        bannerService.changeActiveStatus(bannerId,
            isActive);
        String message = isActive ? "Bật banner thành công" : "Tắt banner thành công";

        return ResponseEntity.ok(
            ApiResponse.<String>builder()
                .data(message)
                .build()
        );
    }
}
