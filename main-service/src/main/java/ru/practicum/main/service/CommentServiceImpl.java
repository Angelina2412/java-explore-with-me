package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.CommentDto;
import ru.practicum.main.dto.NewCommentDto;
import ru.practicum.main.enums.EventState;
import ru.practicum.main.exceptions.ForbiddenException;
import ru.practicum.main.exceptions.NotFoundException;
import ru.practicum.main.mapper.CommentMapper;
import ru.practicum.main.model.Comment;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.CommentRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto dto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + userId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено: " + eventId));

        if (event.getState() != EventState.PUBLISHED) {
            throw new ForbiddenException("Нельзя комментировать неопубликованное событие.");
        }

        Comment comment = commentMapper.toEntity(dto, author, event);
        Comment saved = commentRepository.save(comment);

        return commentMapper.toDto(saved);
    }

    @Override
    public List<CommentDto> getCommentsForEvent(Long eventId, int from, int size) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено: " + eventId));

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "created"));
        return commentRepository.findByEvent(event, pageable).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, NewCommentDto dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден: " + commentId));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("Пользователь не может редактировать чужой комментарий.");
        }

        comment.setText(dto.getText());
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден: " + commentId));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("Пользователь не может удалить чужой комментарий.");
        }

        commentRepository.delete(comment);
    }

}

