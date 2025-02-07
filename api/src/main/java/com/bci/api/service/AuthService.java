package com.bci.api.service;

import com.bci.api.dto.requests.UserRequestDto;
import com.bci.api.dto.responses.LoginResponseDto;
import com.bci.api.dto.responses.SignInResponseDto;
import com.bci.api.mappers.UserMapper;
import com.bci.api.model.Token;
import com.bci.api.model.User;
import com.bci.api.repository.UsersRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService (final UsersRepository usersRepository,
                        final UserMapper userMapper,
                        final PasswordEncoder passwordEncoder,
                        final JwtService jwtService) {
        this.jwtService = jwtService;
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public SignInResponseDto createUser(final UserRequestDto request) {
        final User user = userMapper.toEntity(request, passwordEncoder);
        user.setToken(Token.builder().user(user).value(jwtService.generateToken(user)).build());
        try {
            return userMapper.toSignInDto(usersRepository.save(user));
        } catch (DataIntegrityViolationException ex) {
            throw new EntityExistsException();
        }
    }

    public LoginResponseDto getUser(final String uuid){
        final User user = usersRepository.findByUuid(uuid).orElseThrow(EntityNotFoundException::new);
        user.setToken(Token.builder().user(user).value(jwtService.generateToken(user)).build());
        user.setLastLogin(LocalDateTime.now());
        return userMapper.toLogInDto(usersRepository.save(user));
    }
}
