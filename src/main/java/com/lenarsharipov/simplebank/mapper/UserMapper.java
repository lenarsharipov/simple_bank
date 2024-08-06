package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.client.CreateClientDto;
import com.lenarsharipov.simplebank.model.Role;
import com.lenarsharipov.simplebank.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",
        imports = Role.class,
        uses = PasswordEncoderMapper.class)
public interface UserMapper {

    @Mapping(target = "password", qualifiedByName = "encodePassword")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(Role.ROLE_CLIENT)")
    @Named("toUser")
    User toUserEntity(CreateClientDto dto);
}
