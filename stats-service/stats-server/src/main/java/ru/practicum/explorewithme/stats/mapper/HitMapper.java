package ru.practicum.explorewithme.stats.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.HitRequest;
import ru.practicum.explorewithme.stats.model.HitEntity;

import java.time.format.DateTimeFormatter;

@Component
public class HitMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HitEntity toEntity(HitRequest request) {
        return new HitEntity(
                null,
                request.getApp(),
                request.getUri(),
                request.getIp(),
                request.getTimestamp()
        );
    }
}
