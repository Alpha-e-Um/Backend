package com.example.eumserver.domain.user;

import com.example.eumserver.domain.jwt.PrincipalDetails;
import com.example.eumserver.domain.user.domain.User;
import com.example.eumserver.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.eumserver.global.error.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CustomUsserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        return new PrincipalDetails(user.getId(), user.getEmail(), user.getName(), user.getAvatar(), user.getAuthorities());
    }
}
