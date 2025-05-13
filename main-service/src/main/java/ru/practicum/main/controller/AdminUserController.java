package ru.practicum.main.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.NewUserRequest;
import ru.practicum.main.dto.UserDto;
import ru.practicum.main.service.AdminUserService;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@AllArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @RequestParam(value = "ids", required = false) List<Long> ids,
            @RequestParam(value = "from", defaultValue = "0") int from,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        List<UserDto> users = adminUserService.getUsers(ids, from, size);
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        UserDto userDto = adminUserService.registerUser(newUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminUserService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}

