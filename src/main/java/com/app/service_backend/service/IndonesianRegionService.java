package com.app.service_backend.service;

import com.app.service_backend.constant.RedisConstant;
import com.app.service_backend.dto.response.*;
import com.app.service_backend.entity.City;
import com.app.service_backend.entity.Province;
import com.app.service_backend.entity.SubDistrict;
import com.app.service_backend.repository.CityRepository;
import com.app.service_backend.repository.ProvinceRepository;
import com.app.service_backend.repository.SubDistrictRepository;
import com.app.service_backend.repository.VillageRepository;
import com.app.service_backend.util.GenericSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndonesianRegionService {

    private final ProvinceRepository provinceRepository;
    private final CityRepository cityRepository;
    private final SubDistrictRepository subDistrictRepository;
    private final VillageRepository villageRepository;
    @SuppressWarnings("rawtypes")
    private final RedisService redisService;

    public List<ProvinceResponse> getAllProvinces(){
        log.info("fetching all provinces");
         List<ProvinceResponse> provinceResponseCache = redisService.cacheData(RedisConstant.PROVINCE_CACHE_KEY);
         if (!Objects.isNull(provinceResponseCache)){
             log.info("get province by cache");
             return provinceResponseCache;
         }
         List<ProvinceResponse> provinceResponses = new ArrayList<>();
         provinceRepository.findAll().forEach(province -> {
             ProvinceResponse provinceResponse = new ProvinceResponse();
             provinceResponse.setId(province.getId());
             provinceResponse.setName(province.getName());
             provinceResponse.setAltName(province.getAltName());
             provinceResponse.setLongitude(province.getLongitude());
             provinceResponse.setLongitude(province.getLongitude());
             provinceResponses.add(provinceResponse);
         });
         redisService.set(RedisConstant.PROVINCE_CACHE_KEY, provinceResponses);
         return provinceResponses;
    }

    public List<CityResponse> getAllCities(String provinceId){
        log.info("fetching all cities by province id : {} ",provinceId);
        List<CityResponse> cityResponses = new ArrayList<>();
        List<CityResponse> cacheCity = redisService.cacheData(RedisConstant.CITY_CACHE_KEY);
        if (!Objects.isNull(cacheCity)){
            log.info("get city by cache");
            return cacheCity;
        }
        cityRepository.cityByProvinceId(provinceId).forEach(city -> {
            CityResponse cityResponse = new CityResponse();
            cityResponse.setId(city.getId());
            cityResponse.setName(city.getName());
            cityResponse.setLongitude(city.getLongitude());
            cityResponse.setLatitude(city.getLatitude());
            cityResponse.setAltName(city.getAltName());
            cityResponse.setProvinceId(city.getProvinceId());
            cityResponses.add(cityResponse);
        });
        redisService.set(RedisConstant.CITY_CACHE_KEY, cityResponses);
        return cityResponses;
    }

    public List<SubDistrictResponse> getAllSubdistricts(String cityId){
        log.info("fetching all subdistricts by city id : {} ",cityId);
        List<SubDistrictResponse> subDistrictResponses = new ArrayList<>();
        List<SubDistrictResponse> cacheSubDistricts = redisService.cacheData(RedisConstant.SUBDISTRICT_CACHE_KEY);
        if (!Objects.isNull(cacheSubDistricts)){
            log.info("get subdistricts by cache");
            return cacheSubDistricts;
        }
        subDistrictRepository.subDistrictByCityId(cityId).forEach(subDistrict -> {
            SubDistrictResponse subDistrictResponse = new SubDistrictResponse();
            subDistrictResponse.setId(subDistrict.getId());
            subDistrictResponse.setName(subDistrict.getName());
            subDistrictResponse.setCityId(subDistrict.getCityId());
            subDistrictResponse.setLongitude(subDistrict.getLongitude());
            subDistrictResponse.setLatitude(subDistrict.getLatitude());
            subDistrictResponse.setAltName(subDistrict.getAltName());
            subDistrictResponses.add(subDistrictResponse);
        });
        redisService.set(RedisConstant.SUBDISTRICT_CACHE_KEY, subDistrictResponses);
        return subDistrictResponses;
    }

    public List<VillageResponse> getAllVillages(String districtId){
        log.info("fetching all villages by district id : {}",districtId);
        List<VillageResponse> villageResponses = new ArrayList<>();
        List<VillageResponse> cacheVillages = redisService.cacheData(RedisConstant.VILLAGE_CACHE_KEY);
        if (!Objects.isNull(cacheVillages)){
            log.info("get villages by caches");
            return cacheVillages;
        }
        villageRepository.villageBySubDistrictId(districtId).forEach(village -> {
            VillageResponse villageResponse = new VillageResponse();
            villageResponse.setId(village.getId());
            villageResponse.setName(village.getName());
            villageResponse.setDistrictId(village.getSubdistrictId());
            villageResponse.setLatitude(village.getLatitude());
            villageResponse.setLongitude(village.getLongitude());
            villageResponses.add(villageResponse);
        });
        redisService.set(RedisConstant.VILLAGE_CACHE_KEY, villageResponses);
        return villageResponses;
    }

    public PageableResponse searchProvince(PageableRequest request){
        log.info("search provinces : {}",request.getTextSearch());
        Specification<Province> provinceSpecification = GenericSpecification.searchByKeyword(request.getTextSearch(), "name", "altName");
        Page<ProvinceResponse> provinceResponses = provinceRepository
                .findAll(provinceSpecification, PageRequest.of(request.getPage(), request.getSize()))
                .map(this::toProvinceResponse);
        return new PageableResponse(provinceResponses);
    }

    public PageableResponse searchCity(PageableRequest request){
        log.info("search cities : {}",request.getTextSearch());
        Specification<City> citySpecification = GenericSpecification.searchByKeyword(request.getTextSearch(), "name", "altName");
        Page<CityResponse> cityResponses = cityRepository
                .findAll(citySpecification, PageRequest.of(request.getPage(), request.getSize()))
                .map(this::toCityResponse);
        return new PageableResponse(cityResponses);
    }

    public PageableResponse searchDistrict(PageableRequest request){
        log.info("search districts : {}",request.getTextSearch());
        Specification<SubDistrict> subDistrictSpec = GenericSpecification.searchByKeyword(request.getTextSearch());
        Page<SubDistrictResponse> districtResponses = subDistrictRepository.findAll(subDistrictSpec,
                PageRequest.of(request.getPage(), request.getSize()))
                .map(this::toSubDistrictResponse);
        return new PageableResponse(districtResponses);
    }

    private SubDistrictResponse toSubDistrictResponse(SubDistrict subDistrict){
        return SubDistrictResponse.builder()
                .id(subDistrict.getId())
                .name(subDistrict.getName())
                .altName(subDistrict.getAltName())
                .cityId(subDistrict.getCityId())
                .latitude(subDistrict.getLatitude())
                .longitude(subDistrict.getLongitude())
                .build();
    }

    private CityResponse toCityResponse(City city){
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .provinceId(city.getProvinceId())
                .altName(city.getAltName())
                .latitude(city.getLatitude())
                .longitude(city.getLongitude())
                .build();
    }

    private ProvinceResponse toProvinceResponse(Province province){
        return ProvinceResponse.builder()
                .id(province.getId())
                .name(province.getName())
                .altName(province.getAltName())
                .latitude(province.getLatitude())
                .longitude(province.getLongitude())
                .build();
    }
}
