package org.scaler.userservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.scaler.userservice.models.Role;
import org.scaler.userservice.models.User;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
public class UserDto {
    private String email;
    private Set<Role> roles = new HashSet<>();

    public static UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());

        return userDto;
    }
}
