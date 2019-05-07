package com.asiainfo.contants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:application-localCore.properties")
public class ApplicationProperty {
    public ApplicationProperty(){
        System.out.println("构造方法 ApplicationProperty ");
    }

    @Value("${security.address}")
    private String address;
    @Value("${jwt.key}")
    private String key;


    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
