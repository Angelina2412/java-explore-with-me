package ru.practicum.main.dto;


import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.main.enums.StateAction;
import ru.practicum.main.model.Location;

@Data
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000, message = "Аннотация не может быть меньше 20 символов и больше 2000")
    private String annotation;
    private Long category;

    @Size(min = 20, max = 7000, message = "Описание не может быть меньше 20 символов и больше 7000")
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;

    @Size(min = 3, max = 120, message = "Заголовок не может быть меньше 3 символов и больше 120")
    private String title;
    private StateAction stateAction;
}
