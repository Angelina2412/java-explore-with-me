package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.model.Comment;
import ru.practicum.main.model.Event;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByEvent(Event event, Pageable pageable);
}

