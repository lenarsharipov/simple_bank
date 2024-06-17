package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.client.ReadClientDto;
import com.lenarsharipov.simplebank.model.Client;
import com.lenarsharipov.simplebank.model.Email;
import com.lenarsharipov.simplebank.model.Phone;
import lombok.experimental.UtilityClass;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class ClientMapperManual {

//    public static ReadClientDto toReturnClientDto(Client client) {
//        return ReadClientDto.builder()
//                .id(client.getId())
//                .username(client.getUser().getUsername())
//                .fullName(client.getFullName())
//                .birthDate(client.getBirthDate())
//                .accountNumber(client.getAccount().getId().toString())
//                .phones(client.getPhones().stream().map(Phone::getNumber).collect(toList()))
//                .emails(client.getEmails().stream().map(Email::getAddress).collect(toList()))
//                .build();
//    }
}
