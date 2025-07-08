package demo.chat.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;


@ConfigurationProperties(prefix = "services")
public class ServicesProperties {
    private Map<String,String> endpoints = new LinkedHashMap<>();

    public Map<String,String> getEndpoints() {
        return endpoints;
    }
    public void setEndpoints(Map<String,String> endpoints) {
        this.endpoints = endpoints;
    }
}
