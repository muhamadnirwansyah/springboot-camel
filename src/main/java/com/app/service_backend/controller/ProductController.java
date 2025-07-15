package com.app.service_backend.controller;

import com.app.service_backend.dto.request.ProductRequest;
import com.app.service_backend.dto.response.ApiResponse;
import com.app.service_backend.dto.response.GetImageResponse;
import com.app.service_backend.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/product")
@RequiredArgsConstructor
public class ProductController extends BaseController{

    private final ProductService productService;

    @PostMapping(value = "/v1.0/create")
    public ResponseEntity<ApiResponse> create(@RequestBody ProductRequest request,
                                              HttpServletRequest httpServletRequest){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.isOk(productService.create(request),
                        checkAcceptLanguage(httpServletRequest),null, null));
    }

    @GetMapping(value = "/v1.0/image")
    public ResponseEntity<Resource> getImage(@RequestParam("path")String path){
        GetImageResponse getImageResponse = productService.readImageFromFolder(path);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(getImageResponse.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + "display_image.jpg" + "\"")
                .body(getImageResponse.getResource());
    }
}
