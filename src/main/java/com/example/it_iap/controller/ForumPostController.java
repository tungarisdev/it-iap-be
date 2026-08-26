package com.example.it_iap.controller;

import com.example.it_iap.dto.forumPost.response.StreakLeaderBoardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.it_iap.dto.ApiResponse;
import com.example.it_iap.dto.forumPost.response.ForumPostSliceResponse;
import com.example.it_iap.dto.forumPost.request.ReactPostRequest;
import com.example.it_iap.dto.forumPost.response.GetForumPostDTO;
import com.example.it_iap.service.ForumPostService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/forum-posts")
@RequiredArgsConstructor
public class ForumPostController {
    private final ForumPostService forumPostService;

    @Operation(
        summary = "Chia sẻ bài đăng Streak",
        description = "Chia sẻ số chuỗi hiện tại thành bài đăng"
    )
    @PostMapping("/share/streak")
    public ResponseEntity<ApiResponse<Void>> shareStreakPost() {
        forumPostService.shareStreakPost();
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                ApiResponse.<Void>builder()
                    .code(201)
                    .message("Chia sẻ bài đăng chuỗi thành công")
                    .build());
    }

    @Operation(
        summary = "Chia sẻ bài đăng GPA",
        description = "Chia sẻ điểm GPA thành bài đăng"
    )
    @PostMapping("/share/grade/{profileId}")
    public ResponseEntity<ApiResponse<Void>> shareGradePost(@PathVariable Long profileId) {
        forumPostService.shareGradePost(profileId);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                ApiResponse.<Void>builder()
                    .code(201)
                    .message("Chi sẻ bài đăng GPA thành công")
                    .build());
    }

    @Operation(
        summary = "Lấy bài đăng của chung",
        description = "Seed từ (10000 - 99999)"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<ForumPostSliceResponse<GetForumPostDTO>>> getPosts(
        @RequestParam @Min(1) int page,
        @RequestParam @Min(10000) @Max(99999) int seed) {
        ForumPostSliceResponse<GetForumPostDTO> response = forumPostService.getPosts(page,
            seed);
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ApiResponse.<ForumPostSliceResponse<GetForumPostDTO>>builder()
                    .data(response)
                    .message("Lấy bài đăng thành công")
                    .build());
    }

    @Operation(
        summary = "Lấy bài đăng của bản thân",
        description = "Người dùng lấy bài đăng của bản thân"
    )
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ForumPostSliceResponse<GetForumPostDTO>>> getMyPosts(
        @RequestParam @Min(1) int page,
        @RequestParam(required = false) Boolean visible
    ) {
        ForumPostSliceResponse<GetForumPostDTO> response = forumPostService.getMyPosts(page,
            visible);
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ApiResponse.<ForumPostSliceResponse<GetForumPostDTO>>builder()
                    .message("Lấy bài đăng cá nhân thành công")
                    .data(response)
                    .build());
    }

    @Operation(
        summary = "Đổi chế độ hiển thị bài đăng",
        description = "true thì người khác sẽ thấy được bài và ngược lai"
    )
    @PutMapping("/change-visible/{postId}")
    public ResponseEntity<ApiResponse<Void>> changePostVisible(@PathVariable Long postId) {
        forumPostService.changePostVisible(postId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ApiResponse.<Void>builder()
                    .message("Đối chế độ hiển thị bài đăng thành công")
                    .build());
    }

    @Operation(
        summary = "Thả cảm xúc bài đăng",
        description = "LOVE - HAHA - WOW"
    )
    @PostMapping("/react/{postId}")
    public ResponseEntity<ApiResponse<GetForumPostDTO>> reactPost(@PathVariable Long postId,
                                                                  @RequestBody @Valid ReactPostRequest request) {
        GetForumPostDTO response = forumPostService.reactPost(postId,
            request);
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ApiResponse.<GetForumPostDTO>builder()
                    .message("Thả cảm xúc bài đăng thành công")
                    .data(response)
                    .build());
    }

    @Operation(
        summary = "Lấy bảng xếp hạng chuỗi",
        description = "TOP 10 User có chuỗi hiện tại cao nhất"
    )
    @GetMapping("/streak-leader-board")
    public ResponseEntity<ApiResponse<List<StreakLeaderBoardResponse>>> getStreakLeaderBoard() {
        List<StreakLeaderBoardResponse> response = forumPostService.getStreakLeaderBoard();
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ApiResponse.<List<StreakLeaderBoardResponse>>builder()
                    .message("Lấy bảng xếp hạng chuỗi thành công")
                    .data(response)
                    .build());
    }

    @Operation(
        summary = "Xóa bài đăng bằng id",
        description = "Người dùng xóa bài đăng cá nhân"
    )
    @DeleteMapping("/{forumPostId}")
    public ResponseEntity<ApiResponse<Void>> deleteForumPost(@PathVariable Long forumPostId) {
        forumPostService.deleteForumPost(forumPostId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                ApiResponse.<Void>builder()
                    .message("Xóa bài đăng thành công")
                    .build());
    }
}
