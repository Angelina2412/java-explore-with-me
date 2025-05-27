package ru.practicum.main.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.main.enums.EventState;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "varchar(2000)")
    private String annotation;

    @Column(nullable = false, columnDefinition = "varchar(7000)")
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Enumerated(EnumType.STRING)
    private EventState state = EventState.PENDING;

    @Column(nullable = false)
    private Boolean paid;
    private Integer participantLimit;
    private Long views = 0L;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @Column(nullable = false)
    private Boolean requestModeration = true;

    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;

    @Embedded
    private Location location;

    public boolean isRequestModeration() {
        return requestModeration;
    }
}


