package ru.practicum.main.service;

import ru.practicum.main.dto.NewUserRequest;
import ru.practicum.main.dto.UserDto;

import java.util.List;

public interface AdminUserService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto registerUser(NewUserRequest newUserRequest);

    void deleteUser(Long userId);
}
