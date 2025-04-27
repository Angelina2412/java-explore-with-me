package ru.practicum.explorewithme.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitRequest;
import ru.practicum.dto.StatsResponse;
import ru.practicum.explorewithme.stats.mapper.HitMapper;
import ru.practicum.explorewithme.stats.model.HitEntity;
import ru.practicum.explorewithme.stats.repository.HitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final HitRepository hitRepository;
    private final HitMapper hitMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public void saveHit(HitRequest hitRequest) {
        HitEntity hitEntity = hitMapper.toEntity(hitRequest);
        hitRepository.save(hitEntity);
    }

    @Override
    public List<StatsResponse> getStats(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startDateTime = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endDateTime = LocalDateTime.parse(end, FORMATTER);

        List<Object[]> results;
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                results = hitRepository.findAllUniqueIpStats(startDateTime, endDateTime);
            } else {
                results = hitRepository.findAllStats(startDateTime, endDateTime);
            }
        } else {
            if (unique) {
                results = hitRepository.findUniqueIpStats(startDateTime, endDateTime, uris);
            } else {
                results = hitRepository.findStats(startDateTime, endDateTime, uris);
            }
        }

        return results.stream()
                .map(r -> new StatsResponse((String) r[0], (String) r[1], ((Number) r[2]).intValue()))
                .sorted((s1, s2) -> Integer.compare(s2.getHits(), s1.getHits()))
                .toList();
    }
}
