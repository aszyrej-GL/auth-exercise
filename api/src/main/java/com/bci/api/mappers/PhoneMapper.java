package com.bci.api.mappers;

import com.bci.api.dto.PhoneDto;
import com.bci.api.dto.requests.UserRequestDto;
import com.bci.api.model.Phone;
import com.bci.api.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface PhoneMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "number", source = "number")
    @Mapping(target = "cityCode", source = "cityCode")
    @Mapping(target = "countryCode", source = "countryCode")
    Phone toEntity(PhoneDto dto);

    @Mapping(target = "number", source = "number")
    @Mapping(target = "cityCode", source = "cityCode")
    @Mapping(target = "countryCode", source = "countryCode")
    PhoneDto toEntity(Phone entity);
}
