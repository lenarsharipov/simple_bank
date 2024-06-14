package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.client.CreatedClientDto;
import com.lenarsharipov.simplebank.dto.client.ReadClientDto;
import com.lenarsharipov.simplebank.model.Client;
import com.lenarsharipov.simplebank.model.Email;
import com.lenarsharipov.simplebank.model.Phone;
import com.lenarsharipov.simplebank.model.User;
import lombok.experimental.UtilityClass;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class ClientMapperManual {

    public static CreatedClientDto toCreatedClientDto(Client client, User user) {
        return CreatedClientDto.builder()
                .username(user.getUsername())
                .fullName(client.getFullName())
                .birthDate(client.getBirthDate())
                .accountNo(client.getAccount().getId().toString())
                .email(client.getEmails().get(0).getAddress())
                .phone(client.getPhones().get(0).getNumber())
                .build();
    }

    public static ReadClientDto toReturnClientDto(Client client) {
        return ReadClientDto.builder()
                .id(client.getId())
                .username(client.getUser().getUsername())
                .fullName(client.getFullName())
                .birthDate(client.getBirthDate())
                .accountNumber(client.getAccount().getId().toString())
                .phones(client.getPhones().stream().map(Phone::getNumber).collect(toList()))
                .emails(client.getEmails().stream().map(Email::getAddress).collect(toList()))
                .build();
    }
}
