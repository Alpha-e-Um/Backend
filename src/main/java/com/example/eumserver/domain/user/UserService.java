package com.example.eumserver.domain.user;

import com.example.eumserver.domain.user.dto.UserUpdateRequest;
import com.example.eumserver.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException(400, "User not found."));
    }

    public User updateInfo(long userId, UserUpdateRequest dto){
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(400, "User not found."));
        if(!dto.isValid(dto.mbti())) throw new CustomException(400, "Invalid Arguments");
        user.updateProfile(new Name(dto.firstName(), dto.lastName()), dto.avatar(), dto.phoneNumber(),
                    dto.mbti(), dto.birthday());
        return user;
    }

    public User findById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(400, "User not found."));
    }
}
