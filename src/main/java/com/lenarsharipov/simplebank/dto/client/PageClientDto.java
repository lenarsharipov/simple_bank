package com.lenarsharipov.simplebank.dto.client;

import lombok.Builder;

import java.util.List;

@Builder
public record PageClientDto(
    List<ReadClientDto> content,
    int pageNumber,
    int pageSize,
    long numberOfElements,
    long totalElements,
    int totalPages,
    boolean isFirst,
    boolean isEmpty,
    boolean isSorted) {
}
