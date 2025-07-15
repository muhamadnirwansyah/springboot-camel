package com.app.service_backend.service.eventlistener;

import com.app.service_backend.dto.event.ProductEvent;
import com.app.service_backend.entity.Log;
import com.app.service_backend.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductEventListener {

    private final LogRepository logRepository;

    @Async
    @EventListener
    public void handleProductEvent(ProductEvent productEvent){
        log.info("Saving log for product {} in background...",productEvent.getProductId());
        Log logEntity = new Log();
        logEntity.setAction(productEvent.getAction());
        logEntity.setLogId(productEvent.getProductId());
        logEntity.setModule(productEvent.getModule());
        logEntity.setLogTime(LocalDateTime.now());
        logRepository.save(logEntity);
        log.info("Saving log for product {} in background is successfully !", productEvent.getProductId());
    }
}
