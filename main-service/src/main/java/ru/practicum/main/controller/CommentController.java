package ru.practicum.main.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CommentDto;
import ru.practicum.main.dto.NewCommentDto;
import ru.practicum.main.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 @RequestBody @Valid NewCommentDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.addComment(userId, eventId, dto));
    }

    @GetMapping("/events/{eventId}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long eventId,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getCommentsForEvent(eventId, from, size));
    }

    @PatchMapping("/users/{userId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long userId,
                                                    @PathVariable Long commentId,
                                                    @RequestBody @Valid NewCommentDto dto) {
        return ResponseEntity.ok(commentService.updateComment(userId, commentId, dto));
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long userId,
                                              @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.noContent().build();
    }

}

