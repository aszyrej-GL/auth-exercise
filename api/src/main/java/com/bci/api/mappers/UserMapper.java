package com.bci.api.mappers;

import com.bci.api.dto.requests.UserRequestDto;
import com.bci.api.dto.responses.LoginResponseDto;
import com.bci.api.dto.responses.SignInResponseDto;
import com.bci.api.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR, uses = {PhoneMapper.class})
public interface UserMapper {

    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "email", source = "dto.email")
    @Mapping(target = "phones", source = "dto.phones")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "lastLogin", expression = "java(java.time.LocalDateTime.now())")
    User toEntity(UserRequestDto dto, PasswordEncoder encoder);

    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "created", source = "created")
    @Mapping(target = "lastLogin", source = "lastLogin")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "token", source = "token.value")
    SignInResponseDto toSignInDto(User user);

    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "created", source = "created")
    @Mapping(target = "lastLogin", source = "lastLogin")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "token", source = "token.value")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "phones", source = "phones")
    LoginResponseDto toLogInDto(User user);

    @AfterMapping
    default void encryptPassword(@MappingTarget User user, UserRequestDto dto, PasswordEncoder encoder){
        user.setPassword(encoder.encode(dto.getPassword()));
    }
}
