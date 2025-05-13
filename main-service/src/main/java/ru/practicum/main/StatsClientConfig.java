package ru.practicum.main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.practicum.StatsClient;

@Configuration
public class StatsClientConfig {

    @Value("${stats-server.url}")
    private String statsServerUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public StatsClient statsClient(RestTemplate restTemplate) {
        return new StatsClient(statsServerUrl, restTemplate);
    }
}


