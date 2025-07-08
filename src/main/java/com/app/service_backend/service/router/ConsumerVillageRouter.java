package com.app.service_backend.service.router;

import com.app.service_backend.config.SftpProperties;
import com.app.service_backend.dto.ConsumerVillageDTO;
import com.app.service_backend.entity.Village;
import com.app.service_backend.repository.VillageRepository;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ConsumerVillageRouter extends RouteBuilder {

    private final VillageRepository villageRepository;
    private final SftpProperties sftpProperties;

    @Override
    public void configure() throws Exception {

        String sftpUriFormat = String.format("sftp://%s@%s:%s/%s?password=%s&delete=false&move=%s/villages_consumer_${date:now:yyyyMMddHHmmss}.json&include=villages.json&delay=10000",
                sftpProperties.getUsername(),
                sftpProperties.getHost(),
                sftpProperties.getPort(),
                sftpProperties.getFolder(),
                sftpProperties.getPassword(),
                sftpProperties.getMoveFolder());

        log.info("Format Uri Sftp connected : {} ",sftpUriFormat);
        from(sftpUriFormat)
                .routeId("sftp-villages-consumer")
                .log("File received : ${file:name}")
                .unmarshal().json(JsonLibrary.Jackson, ConsumerVillageDTO[].class)
                .process(exchange -> {

                    if (!villageRepository.findAll().isEmpty()){
                        villageRepository.deleteAll();
                    }
                    ConsumerVillageDTO[] consumerVillageDTOS = exchange.getIn()
                            .getBody(ConsumerVillageDTO[].class);
                    List<Village> villageList = new ArrayList<>();
                    for (ConsumerVillageDTO consumerVillageDTO : consumerVillageDTOS){
                        Village village = new Village();
                        village.setId(consumerVillageDTO.getId());
                        village.setName(consumerVillageDTO.getName());
                        village.setSubdistrictId(consumerVillageDTO.getDistrictId());
                        village.setCreatedTime(LocalDateTime.now());
                        village.setUpdatedTime(LocalDateTime.now());
                        village.setLatitude(Objects.isNull(consumerVillageDTO.getLatitude()) ? null : Double.parseDouble(consumerVillageDTO.getLatitude()));
                        village.setLongitude(Objects.isNull(consumerVillageDTO.getLongitude()) ? null :  Double.parseDouble(consumerVillageDTO.getLongitude()));
                        villageList.add(village);
                    }
                    log.info("process save all villages !!");
                    villageRepository.saveAll(villageList);
                    log.info("success save all villages !!");
                });
    }
}
