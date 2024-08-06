package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.client.CreateClientDto;
import com.lenarsharipov.simplebank.dto.client.CreatedClientDto;
import com.lenarsharipov.simplebank.model.Client;
import com.lenarsharipov.simplebank.model.Email;
import com.lenarsharipov.simplebank.model.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class, AccountMapper.class},
        imports = {List.class, Map.class, UUID.class, Phone.class, Email.class}
)
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "dto", target = "user", qualifiedByName = "toUser")
    @Mapping(source = "dto", target = "account", qualifiedByName = "toAccount")
    @Mapping(target = "emails", expression = "java(List.of(Email.of(null, dto.email())))")
    @Mapping(target = "phones", expression = "java(createPhoneMap(dto.phone()))")
    Client toClientEntity(CreateClientDto dto);

    default Map<String, Phone> createPhoneMap(String phoneNumber) {
        Phone phone = Phone.of(phoneNumber);
        Map<String, Phone> phones = new HashMap<>();
        phones.put(phone.getExternalId(), phone);
        return phones;
    }


    CreatedClientDto toCreatedClientDto(Client client);
}
