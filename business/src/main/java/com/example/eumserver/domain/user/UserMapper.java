package com.example.eumserver.domain.user;

import com.example.eumserver.domain.user.domain.User;
import com.example.eumserver.domain.user.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "userId", source = "id")
    UserResponse userToUserResponse(User user);
}
