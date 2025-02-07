package com.bci.api.dto.errors;

import com.bci.api.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetailDto extends BaseDto {
    private LocalDateTime timestamp;
    private int code;
    private String detail;
}
