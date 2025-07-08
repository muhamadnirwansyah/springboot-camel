package com.app.service_backend.service.router;

import com.app.service_backend.config.SftpProperties;
import com.app.service_backend.dto.ConsumeSubDistrictDTO;
import com.app.service_backend.entity.SubDistrict;
import com.app.service_backend.repository.SubDistrictRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumeDistrictRouter extends RouteBuilder {

    private final SubDistrictRepository subDistrictRepository;
    private final SftpProperties sftpProperties;

    @Override
    public void configure() throws Exception {
        String sftpUriFormat = String.format("sftp://%s@%s:%s/%s?password=%s&delete=false&move=%s/district_consumer_${date:now:yyyyMMddHHmmss}.json&include=district.json&delay=10000",
                sftpProperties.getUsername(),
                sftpProperties.getHost(),
                sftpProperties.getPort(),
                sftpProperties.getFolder(),
                sftpProperties.getPassword(),
                sftpProperties.getMoveFolder());

        log.info("Format Uri Sftp connected : {} ",sftpUriFormat);

        from(sftpUriFormat)
                .routeId("sftp-district-consumer")
                .log("File received : ${file:name}")
                .unmarshal().json(JsonLibrary.Jackson, ConsumeSubDistrictDTO[].class)
                .process(exchange -> {

                    if (!subDistrictRepository.findAll().isEmpty()){
                        subDistrictRepository.deleteAll();
                    }
                    ConsumeSubDistrictDTO[] consumeSubDistrictDTOS = exchange.getIn()
                            .getBody(ConsumeSubDistrictDTO[].class);
                    List<SubDistrict> subDistrictList = new ArrayList<>();
                    for (ConsumeSubDistrictDTO consumeSubDistrictDTO : consumeSubDistrictDTOS){
                        SubDistrict subDistrict = new SubDistrict();
                        subDistrict.setId(consumeSubDistrictDTO.getId());
                        subDistrict.setName(consumeSubDistrictDTO.getName());
                        subDistrict.setCityId(consumeSubDistrictDTO.getRegencyId());
                        subDistrict.setAltName(consumeSubDistrictDTO.getAltName());
                        subDistrict.setCreatedTime(LocalDateTime.now());
                        subDistrict.setUpdatedTime(LocalDateTime.now());
                        subDistrict.setLatitude(Objects.isNull(consumeSubDistrictDTO.getLatitude()) ? null : Double.parseDouble(consumeSubDistrictDTO.getLatitude()));
                        subDistrict.setLongitude(Objects.isNull(consumeSubDistrictDTO.getLongitude()) ? null :  Double.parseDouble(consumeSubDistrictDTO.getLongitude()));
                        subDistrictList.add(subDistrict);
                    }
                    log.info("process save all subdistricts !!");
                    subDistrictRepository.saveAll(subDistrictList);
                    log.info("success save all subdistricts !!");
                });
    }
}
