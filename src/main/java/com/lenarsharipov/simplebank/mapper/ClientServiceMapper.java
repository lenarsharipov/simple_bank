package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.client.CreateClientDto;
import com.lenarsharipov.simplebank.dto.client.CreatedClientDto;
import com.lenarsharipov.simplebank.dto.email.CreatedEmailDto;
import com.lenarsharipov.simplebank.dto.phone.CreatedPhoneDto;
import com.lenarsharipov.simplebank.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        imports = {Role.class, List.class})
public abstract class ClientServiceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "emails", expression = "java(List.of(Email.of(null, dto.email())))")
    @Mapping(target = "phones", expression = "java(List.of(Phone.of(null, dto.phone())))")
    public abstract Client toClientEntity(CreateClientDto dto, User user, Account account);
    @Mapping(source = "client.account.id", target = "accountNo", numberFormat = "#")
    @Mapping(target = "email", expression = "java(client.getEmails().get(0).getAddress())")
    @Mapping(target = "phone", expression = "java(client.getPhones().get(0).getNumber())")
    public abstract CreatedClientDto toCreatedClientDto(Client client, User user);

    @Mapping(source = "initialBalance", target = "initialDeposit")
    @Mapping(source = "initialBalance", target = "balance")
    public abstract Account toAccountEntity(CreateClientDto dto);

    @Mapping(target = "password", constant = "")
    @Mapping(target = "role", expression = "java(Role.ROLE_CLIENT)")
    public abstract User toUserEntity(CreateClientDto dto);

    @Mapping(source = "email.address", target = "email")
    public abstract CreatedEmailDto toCreatedEmailDto(Email email);

    @Mapping(source = "phone.number", target = "phone")
    public abstract CreatedPhoneDto toCreatedPhoneDto(Phone phone);
}
