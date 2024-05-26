package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.user.*;
import com.lenarsharipov.simplebank.model.Email;
import com.lenarsharipov.simplebank.model.Phone;
import com.lenarsharipov.simplebank.model.User;
import lombok.experimental.UtilityClass;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class UserMapper {

    public static User toEntity(CreateUserDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .fullName(dto.getFullName())
                .birthDate(dto.getBirthDate())
                .password("")
                .build();
    }

    public static CreatedUserDto toCreatedUserDto(User user) {
        return CreatedUserDto.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .birthDate(user.getBirthDate())
                .accountNo(user.getAccount().getId().toString())
                .phone(user.getPhones().get(0).getNumber())
                .email(user.getEmails().get(0).getAddress())
                .build();
    }

    public static ReadUserDto toReturnUserDto(User user) {
        return ReadUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .birthDate(user.getBirthDate())
                .accountNumber(user.getAccount().getId().toString())
                .emails(user.getEmails().stream()
                        .map(Email::getAddress)
                        .collect(toList()))
                .phones(user.getPhones().stream()
                        .map(Phone::getNumber)
                        .collect(toList()))
                .build();
    }
}
