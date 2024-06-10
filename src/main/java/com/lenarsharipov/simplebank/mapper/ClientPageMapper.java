package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.client.PageClientDto;
import com.lenarsharipov.simplebank.dto.client.ReadUserDto;
import com.lenarsharipov.simplebank.model.Client;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.util.List;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class ClientPageMapper {
//    public static PageClientDto toPageUserDto(Page<Client> userPage) {
//        return PageClientDto.builder()
//                .content(toReturnUserDto(userPage))
//                .pageNumber(userPage.getNumber() + 1)
//                .pageSize(userPage.getSize())
//                .numberOfElements(userPage.getNumberOfElements())
//                .totalElements(userPage.getTotalElements())
//                .totalPages(userPage.getTotalPages())
//                .isFirst(userPage.isFirst())
//                .isEmpty(userPage.isEmpty())
//                .isSorted(userPage.getSort().isSorted())
//                .build();
//    }

//    public static List<ReadUserDto> toReturnUserDto(Page<Client> userPage) {
//        return userPage.getContent().stream()
//                .map(UserMapper::toReturnUserDto)
//                .collect(toList());
//    }
}
