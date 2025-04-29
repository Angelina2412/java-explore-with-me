package ru.practicum.explorewithme.stats.service;

import ru.practicum.dto.HitRequest;
import ru.practicum.dto.StatsResponse;

import java.util.List;

public interface StatsService {
    void saveHit(HitRequest hitRequest);

    List<StatsResponse> getStats(String start, String end, List<String> uris, boolean unique);

}
