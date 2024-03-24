package com.example.eumserver.domain.user;

import com.example.eumserver.domain.user.dto.UserResponse;
import com.example.eumserver.domain.user.dto.UserUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponse userToUserResponse(User user);
}
