package com.bci.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PhoneDto extends BaseDto {
    @NotNull
    private long number;
    @NotNull
    private int cityCode;
    @NotBlank
    private String countryCode;
}
