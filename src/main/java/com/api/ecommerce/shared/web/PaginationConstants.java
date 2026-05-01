package com.api.ecommerce.shared.web;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationConstants {
    public static final int DEFAULT_PAGE_SIZE = 9;
    public static final int MAX_PAGE_SIZE = 12;

    public static Pageable validatePageable(int pageNumber, int pageSize, Sort sort) throws BadRequestException {
        if(pageNumber < 0){
            pageNumber = 0;
        }
        if ((pageSize <= 0) || (pageSize > MAX_PAGE_SIZE)){
            throw new BadRequestException("Page size must be greater than 0 and must not exceed " + MAX_PAGE_SIZE);
        }
        return PageRequest.of(
                pageNumber,
                pageSize,
                sort
        );
    }

}
