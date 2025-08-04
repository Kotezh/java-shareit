package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class UserServiceTest {
    private final UserService service;
    private final JdbcTemplate jdbcTemplate;

    UserCreateDto createDto1 = UserCreateDto.builder()
            .name("Катерина")
            .email("user@mail.ru")
            .build();

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM comments");
        jdbcTemplate.update("DELETE FROM booking");
        jdbcTemplate.update("DELETE FROM items");
        jdbcTemplate.update("DELETE FROM requests");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    void shouldCreateUser() {
        UserDto dtoCreated = service.create(createDto1);
        UserDto dtoGet = service.getUserById(dtoCreated.getId());

        assertThat(dtoCreated, equalTo(dtoGet));
    }

    @Test
    void shouldUpdateUser() {
        UserUpdateDto updateDto = UserUpdateDto.builder()
                .name("Юлия")
                .email("yuyu@mail.ru")
                .build();

        UserDto dtoCreated = service.create(createDto1);
        UserDto dtoUpdated = service.update(dtoCreated.getId(), updateDto);

        assertThat(dtoUpdated.getId(), equalTo(dtoCreated.getId()));
        assertThat(dtoUpdated.getName(), equalTo(updateDto.getName()));
        assertThat(dtoUpdated.getEmail(), equalTo(updateDto.getEmail()));
    }

    @Test
    void shouldReturnUserById() {
        UserDto dtoCreated = service.create(createDto1);
        UserDto dtoGet = service.getUserById(dtoCreated.getId());

        assertThat(dtoCreated, equalTo(dtoGet));
    }

    @Test
    void shouldReturnAllUsers() {
        UserDto dtoCreated = service.create(createDto1);
        List<UserDto> list = service.getAll();

        assertThat(list.size(), equalTo(1));

        assertThat(list.getFirst(), equalTo(dtoCreated));
    }

    @Test
    void shouldDeleteUser() {
        UserDto dtoCreated = service.create(createDto1);
        service.deleteUser(dtoCreated.getId());

        assertThrows(NotFoundException.class, () -> service.getUserById(dtoCreated.getId()));
    }
}