package ru.practicum.main.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;
}
