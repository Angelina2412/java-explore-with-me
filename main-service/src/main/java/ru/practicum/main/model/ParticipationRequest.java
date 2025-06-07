package ru.practicum.main.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.enums.RequestStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "participation_requests", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"requester_id", "event_id"})
})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Column(name = "created", nullable = false, updatable = false)
    private LocalDateTime created;
}
