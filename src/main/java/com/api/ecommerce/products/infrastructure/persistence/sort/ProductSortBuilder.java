package com.api.ecommerce.products.infrastructure.persistence.sort;
import org.springframework.data.domain.Sort;

public class ProductSortBuilder {

    public static Sort build(String sort,Boolean bestSellers){

        Sort sortRequest = Sort.unsorted();

        if(Boolean.TRUE.equals(bestSellers)){
            sortRequest.and(Sort.by("unitsSold").descending());
        }

        if (sort != null && !sort.isBlank()){
            Sort userSort = switch (sort.toLowerCase()) {
                case "price_asc" -> sortRequest = Sort.by("unitPrice").ascending();
                case "price_desc" -> sortRequest = Sort.by("unitPrice").descending();
                default -> sortRequest = Sort.unsorted();
            };
            sortRequest.and(userSort);
        }
        return sortRequest;
    }
}
