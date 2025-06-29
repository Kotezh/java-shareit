package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ConstraintViolationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = false)
    public UserDto create(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        if (user.getEmail() == null) {
            throw new ConstraintViolationException("Нужно указать email пользователя");
        }
        if (checkEmailExist(user.getEmail())) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }
        User createdUser = userRepository.save(user);
        return UserMapper.mapToDto(createdUser);
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto update(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        if (!userRepository.existsById(user.getId())) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        if (user.getEmail() != null && checkEmailExist(user.getEmail())) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }

        User savedUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new NotFoundException("Пользователь с таким id не найден")
        );
        if (user.getEmail() != null) {
            String email = userDto.getEmail();
            savedUser.setEmail(email);
        }

        if (user.getName() != null) {
            savedUser.setName(userDto.getName());
        }
        return UserMapper.mapToDto(userRepository.save(savedUser));
    }

    @Override
    public UserDto get(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User отсутствует"));
        return UserMapper.mapToDto(user);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids) {
        return userRepository.findByIdIn(ids).stream().map(UserMapper::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public boolean checkUserId(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean checkEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }
}
