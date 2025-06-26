package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto user);

    UserDto update(UserDto user);

    UserDto get(long id);

    List<UserDto> getAll();

    List<UserDto> getUsers(List<Long> ids);

    void delete(long userId);

    boolean checkUserId(long id);

    boolean checkEmailExist(String email);
}
