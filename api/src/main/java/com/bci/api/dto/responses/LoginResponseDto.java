package com.bci.api.dto.responses;

import com.bci.api.dto.BaseDto;
import com.bci.api.dto.PhoneDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class LoginResponseDto extends SignInResponseDto {
    private String name;
    private String email;
    private String password;
    private List<PhoneDto> phones;
}
