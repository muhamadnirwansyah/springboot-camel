package com.app.service_backend.dto.event;

import org.springframework.context.ApplicationEvent;

public class ProductEvent extends ApplicationEvent {

    private final Long productId;
    private final String action;
    private final String module;

    public ProductEvent(Object source, Long productId, String action, String module){
        super(source);
        this.productId = productId;
        this.action = action;
        this.module = module;
    }

    public Long getProductId(){
        return productId;
    }

    public String getAction(){
        return action;
    }

    public String getModule(){
        return module;
    }
}
