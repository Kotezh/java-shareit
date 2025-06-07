package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        return userMapper.mapModelToDto(userService.getUserById(userId));
    }

    @GetMapping()
    public Collection<UserDto> getAll() {
        return userMapper.mapListModelToDto(userService.getAll());
    }

    @PostMapping
    public UserDto create(@RequestBody @Validated UserCreateDto userDto) {
        User user = userMapper.createDtoToModel(userDto);
        return userMapper.mapModelToDto(userService.create(user));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId,
                          @RequestBody @Validated UserUpdateDto userDto) {
        User user = userMapper.updateDtoToModel(userDto, userId);
        return userMapper.mapModelToDto(userService.update(user));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }

}
