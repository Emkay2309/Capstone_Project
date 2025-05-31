package com.capstone.backend.helpers;

import com.capstone.backend.dtos.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class PageResponseHelper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> targetType) {
        List<U> entity = page.getContent();
        List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object, targetType)).collect(Collectors.toList());

        PageableResponse<V> response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElement(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());

        return response;
    }
}
