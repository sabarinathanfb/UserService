package org.scaler.userservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.scaler.userservice.models.SessionStatus;

@Getter
@Setter
public class ValidateTokenResponseDto {
    private UserDto userDto;
    private SessionStatus sessionStatus;



}
