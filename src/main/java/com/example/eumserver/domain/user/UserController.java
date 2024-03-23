package com.example.eumserver.domain.user;

import com.example.eumserver.domain.jwt.PrincipleDetails;
import com.example.eumserver.domain.user.dto.UserResponse;
import com.example.eumserver.domain.user.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal PrincipleDetails principleDetails) {
        String email = principleDetails.getEmail();
        User user = userService.findByEmail(email);
        UserResponse userResponse = UserMapper.INSTANCE.userToUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyInfo(@AuthenticationPrincipal PrincipleDetails principleDetails, @RequestBody @Valid final UserUpdateRequest request){
        String email = principleDetails.getEmail();
        User user = userService.updateInfo(email, request);
        UserResponse userResponse = UserMapper.INSTANCE.userToUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{email:.+}")
    public ResponseEntity<UserResponse> getUserInfo(@PathVariable String email){
        User user = userService.findByEmail(email);
        UserResponse userResponse = UserMapper.INSTANCE.userToUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }
}
