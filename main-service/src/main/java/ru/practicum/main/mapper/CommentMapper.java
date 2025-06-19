package ru.practicum.main.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main.dto.CommentDto;
import ru.practicum.main.dto.NewCommentDto;
import ru.practicum.main.model.Comment;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.User;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    public CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getName())
                .eventId(comment.getEvent().getId())
                .created(comment.getCreated())
                .build();
    }

    public Comment toEntity(NewCommentDto dto, User author, Event event) {
        return Comment.builder()
                .text(dto.getText())
                .author(author)
                .event(event)
                .created(LocalDateTime.now())
                .build();
    }
}

