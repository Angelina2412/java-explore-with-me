package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.enums.EventState;
import ru.practicum.main.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByIdAndState(Long id, EventState state);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    List<Event> findByInitiatorId(Long userId, Pageable pageable);

    boolean existsByCategoryId(Long categoryId);

    @Query("SELECT e FROM Event e " +
            "WHERE e.state = 'PUBLISHED' " +
            "AND (:text IS NULL OR (LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "                        OR LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')))) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (:rangeStart IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (:rangeEnd IS NULL OR e.eventDate <= :rangeEnd) " +
            "AND (:categoriesSize = 0 OR e.category.id IN :categories)")
    List<Event> findPublishedEventsWithFilters(@Param("text") String text,
                                               @Param("categories") List<Long> categories,
                                               @Param("categoriesSize") int categoriesSize,
                                               @Param("paid") Boolean paid,
                                               @Param("rangeStart") LocalDateTime rangeStart,
                                               @Param("rangeEnd") LocalDateTime rangeEnd,
                                               Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE (:usersSize = 0 OR e.initiator.id IN :users) " +
            "AND (:statesSize = 0 OR e.state IN :states) " +
            "AND (:categoriesSize = 0 OR e.category.id IN :categories) " +
            "AND (:rangeStart IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (:rangeEnd IS NULL OR e.eventDate <= :rangeEnd)")
    Page<Event> searchByAdminFilters(@Param("users") List<Long> users,
                                     @Param("usersSize") int usersSize,
                                     @Param("states") List<String> states,
                                     @Param("statesSize") int statesSize,
                                     @Param("categories") List<Long> categories,
                                     @Param("categoriesSize") int categoriesSize,
                                     @Param("rangeStart") LocalDateTime rangeStart,
                                     @Param("rangeEnd") LocalDateTime rangeEnd,
                                     Pageable pageable);

}

