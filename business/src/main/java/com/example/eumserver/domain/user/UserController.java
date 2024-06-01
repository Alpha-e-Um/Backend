package com.example.eumserver.domain.user;

import com.example.eumserver.domain.jwt.PrincipalDetails;
import com.example.eumserver.domain.team.Team;
import com.example.eumserver.domain.team.TeamService;
import com.example.eumserver.domain.user.domain.User;
import com.example.eumserver.domain.user.dto.UserResponse;
import com.example.eumserver.domain.user.dto.UserUpdateRequest;
import com.example.eumserver.global.dto.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TeamService teamService;

    @GetMapping("/me")
    public ResponseEntity<ApiResult<UserResponse>> getMyInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = userService.findById(principalDetails.getUserId());
        UserResponse userResponse = UserMapper.INSTANCE.userToUserResponse(user);
        return ResponseEntity
                .ok(new ApiResult<>("유저(자신) 조회 성공", userResponse));
    }

    @GetMapping("/me/teams")
    public ResponseEntity<ApiResult<List<Team>>> getMyTeams(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        List<Team> teams = teamService.findAllByUserId(principalDetails.getUserId());
        return ResponseEntity.ok(new ApiResult<>("유저(자신) 팀 전체 조회 성공", teams));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResult<UserResponse>> updateMyInfo(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody @Valid final UserUpdateRequest request) {
        long userId = principalDetails.getUserId();
        User user = userService.updateInfo(userId, request);
        UserResponse userResponse = UserMapper.INSTANCE.userToUserResponse(user);
        return ResponseEntity
                .ok(new ApiResult<>("유저(자신) 업데이트 성공", userResponse));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResult<UserResponse>> getUserInfo(@PathVariable long userId) {
        User user = userService.findById(userId);
        UserResponse userResponse = UserMapper.INSTANCE.userToUserResponse(user);
        return ResponseEntity
                .ok(new ApiResult<>("유저 조회 성공", userResponse));
    }

}
