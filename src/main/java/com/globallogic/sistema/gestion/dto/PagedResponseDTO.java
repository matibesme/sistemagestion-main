package com.globallogic.sistema.gestion.dto;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PagedResponseDTO<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext
) {
    public static <T> PagedResponseDTO<T> from(Page<T> page) {
        return PagedResponseDTO.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .build();
    }
}
