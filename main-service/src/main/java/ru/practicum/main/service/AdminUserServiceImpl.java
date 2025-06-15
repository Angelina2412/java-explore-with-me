package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.NewUserRequest;
import ru.practicum.main.dto.UserDto;
import ru.practicum.main.exceptions.BadRequestException;
import ru.practicum.main.exceptions.ConflictException;
import ru.practicum.main.exceptions.NotFoundException;
import ru.practicum.main.mapper.UserMapper;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        if (ids != null && !ids.isEmpty()) {
            List<User> users = userRepository.findByIdIn(ids);
            return users.stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            Page<User> usersPage = userRepository.findAll(pageable);
            return usersPage.stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public UserDto registerUser(NewUserRequest newUserRequest) {
        String[] parts = newUserRequest.getEmail().split("@");
        if (parts.length != 2) {
            throw new BadRequestException("Email должен содержать один символ '@'");
        }

        String localPart = parts[0];
        String domainPart = parts[1];

        if (localPart.length() > 64) {
            throw new BadRequestException("Часть email до '@' не должна превышать 64 символа");
        }

        String[] domainSegments = domainPart.split("\\.");
        for (String segment : domainSegments) {
            if (segment.length() > 63) {
                throw new BadRequestException("Каждый сегмент домена не должен превышать 63 символа");
            }
        }

        if (userRepository.existsByEmail(newUserRequest.getEmail())) {
            throw new ConflictException("Пользователь с таким email уже зарегистрирован");
        }

        User user = userMapper.toEntity(newUserRequest);
        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        userRepository.delete(user);
    }
}

