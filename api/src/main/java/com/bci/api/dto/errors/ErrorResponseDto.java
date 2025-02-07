package com.bci.api.dto.errors;

import com.bci.api.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto extends BaseDto {

    private List<ErrorDetailDto> errors;

    public static ErrorResponseDto createError(final int code, final String detail) {
        ErrorResponseDto errorResponse = new ErrorResponseDto();
        List<ErrorDetailDto> errorsList = new ArrayList<>();
        errorsList.add(new ErrorDetailDto(LocalDateTime.now(), code, detail));
        errorResponse.setErrors(errorsList);
        return errorResponse;

    }

}
