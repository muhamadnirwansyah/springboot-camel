package com.app.service_backend.service.router;

import com.app.service_backend.config.SftpProperties;
import com.app.service_backend.dto.ConsumeCityDTO;
import com.app.service_backend.entity.City;
import com.app.service_backend.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerCityRouter extends RouteBuilder {

    private final CityRepository cityRepository;
    private final SftpProperties sftpProperties;

    @Override
    public void configure() throws Exception {

        String sftpUriFormat = String.format("sftp://%s@%s:%s/%s?password=%s&delete=false&move=%s/regencies_or_city_${date:now:yyyyMMddHHmmss}.json&include=regencies.json&delay=10000",
                sftpProperties.getUsername(),
                sftpProperties.getHost(),
                sftpProperties.getPort(),
                sftpProperties.getFolder(),
                sftpProperties.getPassword(),
                sftpProperties.getMoveFolder());

        log.info("Format Uri Sftp connected : {} ",sftpUriFormat);

        from(sftpUriFormat)
                .routeId("sftp-regencies-consumer")
                .log("File received : ${file:name}")
                .unmarshal().json(JsonLibrary.Jackson, ConsumeCityDTO[].class)
                .process(exchange -> {

                    if (!cityRepository.findAll().isEmpty()){
                        cityRepository.deleteAll();
                    }
                    ConsumeCityDTO[] consumeCityDTOS = exchange.getIn()
                            .getBody(ConsumeCityDTO[].class);
                    List<City> cityList = new ArrayList<>();
                    for (ConsumeCityDTO consumeCityDTO : consumeCityDTOS){
                        City city = new City();
                        city.setId(consumeCityDTO.getId());
                        city.setName(consumeCityDTO.getName());
                        city.setAltName(consumeCityDTO.getAltName());
                        city.setProvinceId(consumeCityDTO.getProvinceId());
                        city.setLongitude(Double.parseDouble(consumeCityDTO.getLongitude()));
                        city.setLatitude(Double.parseDouble(consumeCityDTO.getLatitude()));
                        city.setCreatedTime(LocalDateTime.now());
                        city.setUpdatedTime(LocalDateTime.now());
                        cityList.add(city);
                    }
                    log.info("process save all cities !!");
                    cityRepository.saveAll(cityList);
                    log.info("success save all cities !!");
                });
    }
}
