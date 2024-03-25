package com.example.eumserver.domain.user;

import com.example.eumserver.domain.jwt.PrincipalDetails;
import com.example.eumserver.domain.user.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "mbti", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "birthday", ignore = true)
    @Mapping(target = "name", source = "nameObject")
    UserResponse principalDetailsToUserResponse(PrincipalDetails principalDetails);

    UserResponse userToUserResponse(User user);
}
