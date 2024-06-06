package com.example.eumserver.domain.resume;

import com.example.eumserver.domain.jwt.PrincipalDetails;
import com.example.eumserver.domain.resume.dto.ResumeRequest;
import com.example.eumserver.domain.resume.dto.ResumeResponse;
import com.example.eumserver.domain.resume.entity.Resume;
import com.example.eumserver.global.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
@Tag(name = "Resume", description = "Resume CRUD API")
public class ResumeController {
    private final ResumeService resumeService;

    @PostMapping("")
    @Operation(summary = "이력서 생성", description = "유저에 이력서를 작성하는 API, Homepage Enum Type은 Schema 참조")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "이력서 작성 성공", content = @Content(schema = @Schema(implementation = Resume.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResult<Resume>> createResume(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid ResumeRequest resumeRequest
    ) {
        long userId = principalDetails.getUserId();
        Resume resume = resumeService.postResume(resumeRequest, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResult<>("이력서 생성 성공", resume));
    }

    @PutMapping("/{resumeId}")
    @Operation(summary = "이력서 수정", description = "유저에 이력서를 수정하는 API, Homepage Enum Type은 Schema 참조")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이력서 수정 성공", content = @Content(schema = @Schema(implementation = Resume.class)))
    })
    public ResponseEntity<ApiResult<Resume>> updateResume(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("resumeId") long resumeId,
            @RequestBody @Valid ResumeRequest resumeRequest
    ) {
        long userId = principalDetails.getUserId();
        Resume resume = resumeService.updateResume(resumeRequest, resumeId, userId);
        return ResponseEntity
                .ok(new ApiResult<>("이력서 업데이트 성공", resume));
    }

    @DeleteMapping("/{resumeId}")
    @Operation(summary = "이력서 삭제", description = "유저에 이력서를 삭제하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "이력서 삭제 성공")
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResult<Object>> deleteResume(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("resumeId") long resumeId
    ) {
        long userId = principalDetails.getUserId();
        resumeService.deleteResume(resumeId, userId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new ApiResult<>("이력서 삭제 성공"));
    }

    @GetMapping("")
    @Operation(summary = "내 이력서 전부 받아오기", description = "내 이력서를 전부 받아오는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이력서 받아오기 성공")
    })
    public ResponseEntity<ApiResult<List<ResumeResponse>>> getAllMyResume(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        long userId = principalDetails.getUserId();
        List<ResumeResponse> resumes = resumeService.getAllResumeByUserId(userId);
        return ResponseEntity
                .ok(new ApiResult<>("내 이력서 전부 받아오기 성공", resumes));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "타인의 이력서 전부 받아오기", description = "타인의 공개된 이력서를 전부 받아오는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이력서 받아오기 성공")
    })
    public ResponseEntity<ApiResult<List<ResumeResponse>>> getAllMyResume(
            @PathVariable("userId") long userId
    ) {
        List<ResumeResponse> resumes = resumeService.getAllResumeByUserId(userId);
        return ResponseEntity
                .ok(new ApiResult<>("타인 이력서 전부 받아오기 성공", resumes));
    }

    @GetMapping("/{resumeId}")
    @Operation(summary = "특정 이력서 받아오기", description = "특정 이력서를 받아오는 API, 비공개 이력서는 내 이력서일 경우만 제공")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이력서 받아오기 성공")
    })
    public ResponseEntity<ApiResult<ResumeResponse>> getResume(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("resumeId") long resumeId
    ) {
        long userId = 0;
        if (principalDetails != null) userId = principalDetails.getUserId();
        ResumeResponse resume = resumeService.getResume(userId, resumeId);
        return ResponseEntity
                .ok(new ApiResult<>("특정 이력서 받아오기 성공", resume));
    }
}
