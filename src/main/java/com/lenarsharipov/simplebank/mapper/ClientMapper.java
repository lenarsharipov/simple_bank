package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.client.CreateClientDto;
import com.lenarsharipov.simplebank.dto.client.CreatedUserDto;
import com.lenarsharipov.simplebank.dto.client.PageClientDto;
import com.lenarsharipov.simplebank.model.Client;
import com.lenarsharipov.simplebank.model.User;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

@UtilityClass
public class ClientMapper {

    public static Client toEntity(CreateClientDto dto) {
        return Client.builder()
                .fullName(dto.getFullName())
                .birthDate(dto.getBirthDate())
                .build();
    }

    public static CreatedUserDto toCreatedClientDto(Client client,
                                                    User user) {
        return CreatedUserDto.builder()
                .username(user.getUsername())
                .fullName(client.getFullName())
                .birthDate(client.getBirthDate())
                .accountNo(client.getAccount().getId().toString())
                .email(client.getEmails().get(0).getAddress())
                .phone(client.getPhones().get(0).getNumber())
                .build();
    }

    public static PageClientDto toPageUserDto(Page<Client> userPage) {
        return null;
    }
}
