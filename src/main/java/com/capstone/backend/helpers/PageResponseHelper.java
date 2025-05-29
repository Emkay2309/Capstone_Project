package com.capstone.backend.helpers;

import com.capstone.backend.dtos.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class PageResponseHelper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> targetType) {
        List<V> dtoList = page.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, targetType))
                .collect(Collectors.toList());

        return PageableResponse.<V>builder()
                .content(dtoList)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElement(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .lastPage(page.isLast())
                .build();
    }
}
