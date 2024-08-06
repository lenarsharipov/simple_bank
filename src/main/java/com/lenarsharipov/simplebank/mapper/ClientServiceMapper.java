package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.client.CreateClientDto;
import com.lenarsharipov.simplebank.dto.client.CreatedClientDto;
import com.lenarsharipov.simplebank.dto.client.PageClientDto;
import com.lenarsharipov.simplebank.dto.client.ReadClientDto;
import com.lenarsharipov.simplebank.dto.email.CreatedEmailDto;
import com.lenarsharipov.simplebank.dto.filter.ClientFiltersDto;
import com.lenarsharipov.simplebank.dto.phone.CreatedPhoneDto;
import com.lenarsharipov.simplebank.model.*;
import com.lenarsharipov.simplebank.service.search.ClientSpecification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Mapper(componentModel = "spring",
        imports = {Role.class, List.class, Map.class})
public interface ClientServiceMapper {

    @Mapping(source = "content", target = "content", qualifiedByName = "clientsToReadClientDtoList")
    @Mapping(source = "number", target = "pageNumber", qualifiedByName = "incrementPageNumber")
    @Mapping(source = "size", target = "pageSize")
    @Mapping(source = "numberOfElements", target = "numberOfElements")
    @Mapping(source = "totalElements", target = "totalElements")
    @Mapping(source = "totalPages", target = "totalPages")
    @Mapping(source = "first", target = "isFirst")
    @Mapping(source = "empty", target = "isEmpty")
    @Mapping(source = "sort.sorted", target = "isSorted")
    PageClientDto toPageClientDto(Page<Client> clientPage);

    @Named("clientsToReadClientDtoList")
    static List<ReadClientDto> clientsToReadClientDtoList(List<Client> clients) {
        return clients.stream()
                .map(ClientServiceMapper::toReturnClientDto)
                .collect(Collectors.toList());
    }

    static ReadClientDto toReturnClientDto(Client client) {
        return ReadClientDto.builder()
                .id(client.getId())
                .username(client.getUser().getUsername())
                .fullName(client.getFullName())
                .birthDate(client.getBirthDate())
                .accountNumber(client.getAccount().getId().toString())
                .phones(client.getPhones().values().stream().map(Phone::getNumber).collect(toList()))
                .emails(client.getEmails().stream().map(Email::getAddress).collect(toList()))
                .build();
    }

    @Named("incrementPageNumber")
    static int incrementPageNumber(int pageNumber) {
        return pageNumber + 1;
    }



    ClientSpecification toClientSpecification(ClientFiltersDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "emails", expression = "java(List.of(Email.of(null, dto.email())))")
    @Mapping(target = "phones", expression = "java(Map.of(phone.getExternalId(), phone))")
    Client toClientEntity(CreateClientDto dto, User user, Account account, Phone phone);

    @Mapping(source = "client.account.id", target = "accountNo", numberFormat = "#")
    @Mapping(target = "email", expression = "java(client.getEmails().iterator().next().getAddress())")
    @Mapping(target = "phone", expression = "java(client.getPhones().values().iterator().next().getNumber())")
    CreatedClientDto toCreatedClientDto(Client client, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(source = "initialBalance", target = "initialDeposit")
    @Mapping(source = "initialBalance", target = "balance")
    Account toAccountEntity(CreateClientDto dto);

    @Mapping(target = "password", constant = "")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(Role.ROLE_CLIENT)")
    User toUserEntity(CreateClientDto dto);

    @Mapping(source = "email.address", target = "email")
    CreatedEmailDto toCreatedEmailDto(Email email);

    @Mapping(source = "phone.number", target = "phone")
    CreatedPhoneDto toCreatedPhoneDto(Phone phone);
}
