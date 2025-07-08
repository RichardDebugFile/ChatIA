package demo.chat.gateway.service;

import demo.chat.gateway.config.ServicesProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Map;

@Service
public class HealthCheckService {

    private final WebClient webClient;
    private final Map<String, String> endpoints;

    public HealthCheckService(WebClient webClient, ServicesProperties props) {
        this.webClient  = webClient;
        this.endpoints  = props.getEndpoints();
    }

    public Mono<Map<String,String>> checkAll() {
        return Flux.fromIterable(endpoints.entrySet())
                .flatMap(entry ->
                        webClient.get()
                                .uri(entry.getValue())
                                .retrieve()
                                .bodyToMono(Map.class)
                                .map(body -> body.getOrDefault("status","UNKNOWN").toString())
                                .onErrorReturn("DOWN")
                                .map(status -> Tuples.of(entry.getKey(), status))
                )
                .collectMap(Tuple2::getT1, Tuple2::getT2);
    }
}
