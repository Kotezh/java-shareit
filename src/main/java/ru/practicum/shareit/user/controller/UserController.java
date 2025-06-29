package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public UserDto get(@PathVariable @Positive Long userId) {
        log.info("Запрос данных пользователя по id {}", userId);
        return userService.get(userId);
    }

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        log.info("Создание пользователя {}", userDto);
        validateUser(userDto);
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable @Positive Long userId,
                          @RequestBody @Valid UserDto userDto) {
        userDto.setId(userId);
        log.info("Обновление данных пользователя id {} data {}", userId, userDto);
        validateUser(userDto);
        return userService.update(userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable @Positive long userId) {
        log.info("Удаление пользователя с id {}", userId);
        userService.delete(userId);
    }

//    @GetMapping
//    public List<UserDto> getAll() {
//        log.info("Запрос всех пользователей");
//        return userService.getAll();
//    }

    public static void validateUser(@Valid UserDto user) {
        if (user.getEmail() != null && (user.getEmail().isBlank() || !user.getEmail().contains("@")
//                || !isValidEmail(user.getEmail()))) {
                )) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }

        if (user.getName() != null && user.getName().isBlank()) {
            user.setName("Нет имени");
        }
    }
}
