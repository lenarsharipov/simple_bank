package com.lenarsharipov.simplebank.mapper;

import com.lenarsharipov.simplebank.dto.user.PageUserDto;
import com.lenarsharipov.simplebank.dto.user.ReadUserDto;
import com.lenarsharipov.simplebank.model.User;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.util.List;

import static java.util.stream.Collectors.toList;

@UtilityClass
public class UserPageMapper {
    public static PageUserDto toPageUserDto(Page<User> userPage) {
        return PageUserDto.builder()
                .content(toReturnUserDto(userPage))
                .pageNumber(userPage.getNumber() + 1)
                .pageSize(userPage.getSize())
                .numberOfElements(userPage.getNumberOfElements())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .isFirst(userPage.isFirst())
                .isEmpty(userPage.isEmpty())
                .isSorted(userPage.getSort().isSorted())
                .build();
    }

    public static List<ReadUserDto> toReturnUserDto(Page<User> userPage) {
        return userPage.getContent().stream()
                .map(UserMapper::toReturnUserDto)
                .collect(toList());
    }
}
