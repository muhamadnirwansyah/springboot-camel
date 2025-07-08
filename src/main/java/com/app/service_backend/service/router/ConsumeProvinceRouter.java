package com.app.service_backend.service.router;

import com.app.service_backend.config.SftpProperties;
import com.app.service_backend.dto.ConsumeProvinceDTO;
import com.app.service_backend.entity.Province;
import com.app.service_backend.repository.ProvinceRepository;
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
public class ConsumeProvinceRouter extends RouteBuilder {

    private final ProvinceRepository provinceRepository;
    private final SftpProperties sftpProperties;

    @Override
    public void configure() throws Exception {

        String sftpUriFormat = String.format("sftp://%s@%s:%s/%s?password=%s&delete=false&move=%s/provinces_consumer_${date:now:yyyyMMddHHmmss}.json&include=provinces.json&delay=10000",
                sftpProperties.getUsername(),
                sftpProperties.getHost(),
                sftpProperties.getPort(),
                sftpProperties.getFolder(),
                sftpProperties.getPassword(),
                sftpProperties.getMoveFolder());

        log.info("Format Uri Sftp connected : {} ",sftpUriFormat);

        from(sftpUriFormat)
                .routeId("sftp-province-consumer")
                .log("File received : ${file:name}")
                .unmarshal().json(JsonLibrary.Jackson, ConsumeProvinceDTO[].class)
                .process(exchange -> {

                    if (!provinceRepository.findAll().isEmpty()){
                        provinceRepository.deleteAll();
                    }
                    ConsumeProvinceDTO[] consumeProvinceDTOS = exchange.getIn()
                            .getBody(ConsumeProvinceDTO[].class);
                    List<Province> provincesList = new ArrayList<>();
                    for (ConsumeProvinceDTO consumeProvinceDTO : consumeProvinceDTOS){
                        Province province = new Province();
                        province.setId(consumeProvinceDTO.getId());
                        province.setName(consumeProvinceDTO.getName());
                        province.setAltName(consumeProvinceDTO.getAltName());
                        province.setLatitude(Double.parseDouble(consumeProvinceDTO.getLatitude()));
                        province.setLongitude(Double.parseDouble(consumeProvinceDTO.getLongitude()));
                        province.setCreatedTime(LocalDateTime.now());
                        province.setUpdatedTime(LocalDateTime.now());
                        provincesList.add(province);
                    }
                    log.info("process save all provinces !!");
                    provinceRepository.saveAll(provincesList);
                    log.info("success save all provinces !!");
                });
    }
}
