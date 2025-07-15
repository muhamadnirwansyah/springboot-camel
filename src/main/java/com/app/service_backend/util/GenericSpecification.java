package com.app.service_backend.util;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class GenericSpecification {

    public static <T>Specification<T> searchByKeyword(String keyword, String... fields){
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(keyword) || keyword.isBlank() || fields.length == 0){
                //no filter data
                return criteriaBuilder.conjunction();
            }
            //filter with field data custom
            String likeKeywords = "%"+keyword.toLowerCase()+"%";
            Predicate[] predicates = new Predicate[fields.length];
            for (int i=0; i < fields.length; i++){
                predicates[i] = criteriaBuilder.like(criteriaBuilder.lower(root.get(fields[i])), likeKeywords);
            }
            return criteriaBuilder.or(predicates);
        };
    }
}
