package com.app.service_backend.controller;

import com.app.service_backend.dto.response.ApiResponse;
import com.app.service_backend.service.IndonesianRegionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/indonesian-region")
@RequiredArgsConstructor
public class IndonesianRegionController extends BaseController{

    private final IndonesianRegionService indonesianRegionService;

    @GetMapping(value = "/province/v1.0/list")
    public ResponseEntity<ApiResponse> listProvince(HttpServletRequest httpServletRequest){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.isOk(indonesianRegionService.getAllProvinces(),
                        checkAcceptLanguage(httpServletRequest), null, null));
    }

    @GetMapping(value = "/city/v1.0/list")
    public ResponseEntity<ApiResponse> listCity(
            @RequestParam(value = "provinceId", defaultValue = "")String provinceId,
            HttpServletRequest httpServletRequest){
        if (provinceId.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.isFailed(HttpStatus.BAD_REQUEST.value(),
                            List.of("Province ID cannot be empty !"),null, null));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.isOk(indonesianRegionService.getAllCities(provinceId),
                        checkAcceptLanguage(httpServletRequest), null, null));
    }

    @GetMapping(value = "/district/v1.0/list")
    public ResponseEntity<ApiResponse> listDistrict(
            @RequestParam(value = "cityId", defaultValue = "")String cityId,
            HttpServletRequest httpServletRequest){
        if (cityId.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.isFailed(HttpStatus.BAD_REQUEST.value(),
                            List.of("City ID cannot be empty !"),null, null));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.isOk(indonesianRegionService.getAllSubdistricts(cityId),
                        checkAcceptLanguage(httpServletRequest), null, null));
    }

    @GetMapping(value = "/village/v1.0/list")
    public ResponseEntity<ApiResponse> listVillage(
            @RequestParam(value = "districtId", defaultValue = "")String districtId,
            HttpServletRequest httpServletRequest){
        if (districtId.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.isFailed(HttpStatus.BAD_REQUEST.value(),
                            List.of("District ID cannot be empty !"),null, null));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.isOk(indonesianRegionService.getAllVillages(districtId),
                        checkAcceptLanguage(httpServletRequest), null, null));
    }
}
