package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.client.PageClientDto;
import com.lenarsharipov.simplebank.dto.client.ReadClientDto;
import com.lenarsharipov.simplebank.model.Client;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.util.List;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class ClientPageMapper {
//    public static PageClientDto toPageClientDto(Page<Client> clientPage) {
//        return PageClientDto.builder()
//                .content(toReturnClientDto(clientPage))
//                .pageNumber(clientPage.getNumber() + 1)
//                .pageSize(clientPage.getSize())
//                .numberOfElements(clientPage.getNumberOfElements())
//                .totalElements(clientPage.getTotalElements())
//                .totalPages(clientPage.getTotalPages())
//                .isFirst(clientPage.isFirst())
//                .isEmpty(clientPage.isEmpty())
//                .isSorted(clientPage.getSort().isSorted())
//                .build();
//    }
//
//    public static List<ReadClientDto> toReturnClientDto(Page<Client> clientPage) {
//        return clientPage.getContent().stream()
//                .map(ClientServiceMapper::toReturnClientDto)
//                .collect(toList());
//    }
}
