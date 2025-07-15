package com.app.service_backend.service;

import com.app.service_backend.dto.event.ProductEvent;
import com.app.service_backend.dto.request.ProductRequest;
import com.app.service_backend.dto.request.ProductResponse;
import com.app.service_backend.dto.response.GetImageResponse;
import com.app.service_backend.entity.Product;
import com.app.service_backend.exception.ValidationMessageException;
import com.app.service_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;
    @Value("${folder.upload.product}")
    private String folderUpload;

    private String uploadFile(ProductRequest productRequest){
        log.info("process upload files..");
        String fileName = UUID.randomUUID().toString()+".jpg";
        File directory = new File(folderUpload);
        if (!directory.exists()){
            directory.mkdirs();
        }
        String extractBase64File = productRequest.getFileBase64().split(",")[1];
        byte[] fileBytes = Base64.getDecoder().decode(extractBase64File);

        Path filePath = Paths.get(folderUpload+fileName);
        try {
            Files.write(filePath, fileBytes);
        } catch (IOException e) {
            log.error("Failed write images : {}",e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("final location image : {}",filePath.toString());
        return filePath.toString();
    }

    public GetImageResponse readImageFromFolder(String originPath){
        log.info("origin path : {}",originPath);
        if (originPath.contains("..")){
            throw new ValidationMessageException(List.of("Invalid image path !"),HttpStatus.BAD_REQUEST.value());
        }
        Path filePath = Paths.get(originPath).normalize();
        if (!Files.exists(filePath)){
            throw new ValidationMessageException(List.of("Invalid images path because image notfound !"),HttpStatus.BAD_REQUEST.value());
        }
        try {
            Resource resource = new UrlResource(filePath.toUri());
            String contentType = Files.probeContentType(filePath);
            if (Objects.isNull(contentType)){
                contentType = "application/octet-stream";
            }
            return GetImageResponse.builder()
                    .contentType(contentType)
                    .resource(resource)
                    .build();
        } catch (IOException e){
            log.error("Failed read image : {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ProductResponse create(ProductRequest request){
        log.info("process create new product : {}",request.getName());
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
        if (!Objects.isNull(request.getFileBase64()) || !request.getFileBase64().trim().isEmpty()){
            log.info("process upload file..");
            product.setImagePath(uploadFile(request));
        }
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
