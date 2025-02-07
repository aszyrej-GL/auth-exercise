package com.bci.api.dto.requests;

import com.bci.api.dto.BaseDto;
import com.bci.api.dto.PhoneDto;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserRequestDto extends BaseDto {
    private String name;
    @NotBlank(message = "Email must not be blank")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email format.")
    private String email;
    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, max = 12, message = "Password length must be 8 to 12")
    @Pattern(regexp = "^(?=[^A-Z]*[A-Z][^A-Z]*$)(?=[^0-9]*[0-9][^0-9]*[0-9][^0-9]*$)[a-zA-Z0-9]*$", message = "Password must contain only one A-Z, two 0-9 and multiple a-z")
    private String password;
    @NotEmpty
    @Valid
    private List<PhoneDto> phones;
}
