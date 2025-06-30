package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto user);

    UserDto update(UserDto user);

    UserDto get(Long id);

//    List<UserDto> getAll();

    List<UserDto> getUsers(List<Long> ids);

    void delete(Long userId);

    boolean checkUserId(Long id);

    boolean checkEmailExist(String email);
}
