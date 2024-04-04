package com.example.eumserver.domain.resume;

import com.example.eumserver.domain.jwt.PrincipalDetails;
import com.example.eumserver.domain.resume.dto.ResumeRequest;
import com.example.eumserver.domain.resume.entity.Resume;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
@Tag(name = "Resume", description = "Resume CRUD API")
public class ResumeController {
    private final ResumeService resumeService;

    @PostMapping("")
    @Operation(summary = "이력서 생성", description = "유저에 이력서를 작성하는 API, Homepage Enum Type은 Schema 참조", parameters = {
            @Parameter(name = "Authorization",
                    description = "Access Token",
                    in = ParameterIn.HEADER,
                    schema = @Schema(type = "string"),
                    required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "이력서 작성 성공", content = @Content(schema = @Schema(implementation = Resume.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Resume> createResume(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid ResumeRequest resumeRequest) {
        long userId = principalDetails.getUserId();
        Resume resume = resumeService.postResume(resumeRequest, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resume);
    }

    @PutMapping("/{resumeId}")
    @Operation(summary = "이력서 수정", description = "유저에 이력서를 수정하는 API, Homepage Enum Type은 Schema 참조", parameters = {
            @Parameter(name = "Authorization",
                    description = "Access Token",
                    in = ParameterIn.HEADER,
                    schema = @Schema(type = "string"),
                    required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이력서 수정 성공", content = @Content(schema = @Schema(implementation = Resume.class)))
    })
    public ResponseEntity<Resume> updateResume(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("resumeId") long resumeId,
            @RequestBody @Valid ResumeRequest resumeRequest) {
        long userId = principalDetails.getUserId();
        Resume resume = resumeService.updateResume(resumeRequest, resumeId, userId);
        return ResponseEntity
                .ok(resume);
    }


}
