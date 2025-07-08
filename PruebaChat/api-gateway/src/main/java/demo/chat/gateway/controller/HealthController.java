// api-gateway/src/main/java/demo/chat/gateway/controller/HealthController.java
package demo.chat.gateway.controller;

import demo.chat.gateway.service.HealthCheckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class HealthController {

    private final HealthCheckService checker;

    public HealthController(HealthCheckService checker) {
        this.checker = checker;
    }

    @GetMapping("/health")
    public Mono<Map<String,String>> health() {
        // Llama a tu servicio, combina todos los monos y devuelve el mapa
        return checker.checkAll();
    }
}
