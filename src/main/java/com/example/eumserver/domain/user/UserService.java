package com.example.eumserver.domain.user;

import com.example.eumserver.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException(500, "User not found."));
    }

    public User updateInfo(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(500, "User not found."));

        return user;
    }
}
