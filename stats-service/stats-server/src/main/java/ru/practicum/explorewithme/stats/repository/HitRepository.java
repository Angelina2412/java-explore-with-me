package ru.practicum.explorewithme.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.stats.model.HitEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<HitEntity, Long> {

    @Query("SELECT h.app, h.uri, COUNT(h.id) FROM HitEntity h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri")
    List<Object[]> findAllStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT h.app, h.uri, COUNT(DISTINCT h.ip) FROM HitEntity h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri")
    List<Object[]> findAllUniqueIpStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT h.app, h.uri, COUNT(h.id) FROM HitEntity h " +
            "WHERE h.timestamp BETWEEN :start AND :end AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri")
    List<Object[]> findStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query("SELECT h.app, h.uri, COUNT(DISTINCT h.ip) FROM HitEntity h " +
            "WHERE h.timestamp BETWEEN :start AND :end AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri")
    List<Object[]> findUniqueIpStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);
}

