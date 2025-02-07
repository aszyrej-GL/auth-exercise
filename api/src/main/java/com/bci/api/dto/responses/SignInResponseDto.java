package com.bci.api.dto.responses;

import com.bci.api.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponseDto extends BaseDto {

    private String id;
    private LocalDateTime created;
    private LocalDateTime lastLogin;
    private String token;
    private boolean isActive;
}
