package com.bci.api.controller;

import com.bci.api.dto.requests.UserRequestDto;
import com.bci.api.dto.responses.LoginResponseDto;
import com.bci.api.dto.responses.SignInResponseDto;
import com.bci.api.service.AuthService;
import com.bci.api.service.JwtService;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController (final AuthService authService,
                           final JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SignInResponseDto> signUp(@RequestBody @Valid final UserRequestDto request){
        final SignInResponseDto response = authService.createUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestHeader(HttpHeaders.AUTHORIZATION) final String tokenHeader){
        final String uuid = jwtService.getUserUuidFromAuthHeader(tokenHeader);
        final LoginResponseDto response = authService.getUser(uuid);
        return ResponseEntity.ok(response);
    }

}
