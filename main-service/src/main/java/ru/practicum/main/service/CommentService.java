package ru.practicum.main.service;

import ru.practicum.main.dto.CommentDto;
import ru.practicum.main.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(Long userId, Long eventId, NewCommentDto dto);

    List<CommentDto> getCommentsForEvent(Long eventId, int from, int size);

    CommentDto updateComment(Long userId, Long commentId, NewCommentDto dto);

    void deleteComment(Long userId, Long commentId);
}

