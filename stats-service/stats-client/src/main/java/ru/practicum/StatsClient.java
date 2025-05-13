package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.HitRequest;
import ru.practicum.dto.StatsResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

//@Component
public class StatsClient {

    private final String statsServerUrl;
    private final RestTemplate restTemplate;

    public StatsClient(String statsServerUrl, RestTemplate restTemplate) {
        this.statsServerUrl = statsServerUrl;
        this.restTemplate = restTemplate;
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void sendHit(HitRequest hitRequest) {
        String url = statsServerUrl + "/hit";
        restTemplate.postForEntity(url, hitRequest, Void.class);
    }

    public List<StatsResponse> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(statsServerUrl + "/stats")
                    .queryParam("start", formatter.format(start))
                    .queryParam("end", formatter.format(end))
                    .queryParam("unique", unique);
            if (uris != null && !uris.isEmpty()) {
                uris.forEach(uri -> builder.queryParam("uris", uri));
            }

            ResponseEntity<StatsResponse[]> response = restTemplate.getForEntity(builder.toUriString(), StatsResponse[].class);
            return response.getBody() != null ? List.of(response.getBody()) : List.of();
        } catch (RestClientException e) {
            System.err.println("Ошибка при вызове stats-server: " + e.getMessage());
            return List.of(); // безопасное поведение
        }
    }

}

