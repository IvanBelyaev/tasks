package com.example.tasks.dto;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
@FieldNameConstants
public class PageResponse<T> {
    List<T> content;
    Metadata metadata;

    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(page.getContent(),
                new Metadata(page.getNumber(), page.getSize(), page.getTotalElements()));
    }

    @Value
    public static class Metadata {
        int page;
        int size;
        long totalElements;
    }
}
