package com.example.usermgmtservice.mapper;

import com.example.usermgmtservice.domain.User;
import com.example.usermgmtservice.model.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);

    User toEntity(UserDTO userDTO);
}
