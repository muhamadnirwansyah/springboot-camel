package com.app.service_backend.service;

import com.app.service_backend.dto.event.ProductEvent;
import com.app.service_backend.dto.request.ProductRequest;
import com.app.service_backend.dto.request.ProductResponse;
import com.app.service_backend.entity.Product;
import com.app.service_backend.exception.ValidationMessageException;
import com.app.service_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;


    public ProductResponse create(ProductRequest request){
        log.info("process create new product : {}",request);
        List<String> validationErrors = request.doValidate();
        if (!validationErrors.isEmpty()){
            throw new ValidationMessageException(validationErrors, HttpStatus.BAD_REQUEST.value());
        }
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setDeleted(BigDecimal.ZERO.intValue());
        LocalDateTime startDateTime = LocalDate.parse(request.getActiveDate()).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.parse(request.getExpireDate()).atTime(LocalTime.MAX);
        product.setStartActiveDate(startDateTime);
        product.setEndActiveDate(endDateTime);
        Product responseProduct = productRepository.save(product);
        eventPublisher.publishEvent(new ProductEvent(this, responseProduct.getId(), "INSERT", "PRODUCT"));
        return ProductResponse.builder()
                .id(responseProduct.getId())
                .name(responseProduct.getName())
                .description(responseProduct.getDescription())
                .imagePath(responseProduct.getImagePath())
                .startActiveDate(responseProduct.getStartActiveDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:ss")))
                .endActiveDate(responseProduct.getEndActiveDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:ss")))
                .deleted(responseProduct.getDeleted())
                .build();
    }
}
