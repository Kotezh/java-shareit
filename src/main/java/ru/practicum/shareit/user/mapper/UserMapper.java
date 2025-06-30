package ru.practicum.shareit.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@UtilityClass
public class UserMapper {

    public static User mapToUser(UserDto dto) {
        if (dto == null) return null;
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public static UserDto mapToDto(User user) {
        if (user == null) return null;
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static List<User> mapToUserList(List<UserDto> usersDto) {
        return usersDto.stream().map(UserMapper::mapToUser).toList();
    }

    public static List<UserDto> mapToDtoList(List<User> users) {
        return users.stream().map(UserMapper::mapToDto).toList();
    }
}
