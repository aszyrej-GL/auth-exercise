package com.bci.api.controller;

import com.bci.api.dto.errors.ErrorResponseDto;
import com.bci.api.dto.requests.UserRequestDto;
import com.bci.api.dto.responses.LoginResponseDto;
import com.bci.api.dto.responses.SignInResponseDto;
import com.bci.api.service.AuthService;
import com.bci.api.service.JwtService;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

    @PostMapping(value = "/sign-up", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Signs up a new user",
            description = "Creates a new user and returns its token")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created", content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = SignInResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "409", description = "Conflict", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))})
    })
    public ResponseEntity<SignInResponseDto> signUp(@RequestBody @Valid final UserRequestDto request){
        final SignInResponseDto response = authService.createUser(request);
        return ResponseEntity.status(CREATED).body(response);
    }

    @PostMapping(value = "/login", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Login an user and retrieve its data",
            description = "Login an user with a valid token and retrieve all fields related to that user",
            security = { @SecurityRequirement(name = "bearer-key") })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ok", content = {
            @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = LoginResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseDto.class))})
    })
    public ResponseEntity<LoginResponseDto> login(@RequestHeader(HttpHeaders.AUTHORIZATION) final String tokenHeader){
        final String uuid = jwtService.getUserUuidFromAuthHeader(tokenHeader);
        final LoginResponseDto response = authService.getUser(uuid);
        return ResponseEntity.ok(response);
    }

}
