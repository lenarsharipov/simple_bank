package com.lenarsharipov.simplebank.dto.client;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PageClientDto {

    private List<ReadClientDto> content;
    private int pageNumber;
    private int pageSize;
    private long numberOfElements;
    private long totalElements;
    private int totalPages;
    private boolean isFirst;
    private boolean isEmpty;
    private boolean isSorted;
}