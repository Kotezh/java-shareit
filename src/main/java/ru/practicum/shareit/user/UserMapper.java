package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class UserMapper {

    public User createDtoToModel(UserCreateDto dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public User updateDtoToModel(UserUpdateDto dto, Long userId) {
        return User.builder()
                .id(userId)
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public UserDto mapModelToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public Collection<UserDto> mapListModelToDto(Collection<User> users) {
        List<UserDto> list = new ArrayList<>();
        for (User user : users) {
            list.add(mapModelToDto(user));
        }
        return list;
    }

}
