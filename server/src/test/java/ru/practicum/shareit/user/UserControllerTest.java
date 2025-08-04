package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private UserService service;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateUser() throws Exception {
        UserCreateDto createDto = UserCreateDto.builder().name("Петр").email("pet@mail.com").build();
        UserDto responseDto = UserDto.builder().id(1L).name("Петр").email("pet@mail.com").build();
        Mockito.when(service.create(any(UserCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Петр"))
                .andExpect(jsonPath("$.email").value("pet@mail.com"));

        Mockito.verify(service, Mockito.times(1)).create(any(UserCreateDto.class));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserUpdateDto updateDto = UserUpdateDto.builder().name("Жанна").email("janna@example.com").build();
        UserDto responseDto = UserDto.builder().id(1L).name("Жанна").email("janna@example.com").build();
        Mockito.when(service.update(eq(1L), any(UserUpdateDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Жанна"))
                .andExpect(jsonPath("$.email").value("janna@example.com"));

        Mockito.verify(service, Mockito.times(1)).update(eq(1L), any(UserUpdateDto.class));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        UserDto responseDto = UserDto.builder().id(1L).name("Петр").email("pet@mail.com").build();
        Mockito.when(service.getUserById(1L)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Петр"))
                .andExpect(jsonPath("$.email").value("pet@mail.com"));

        Mockito.verify(service, Mockito.times(1)).getUserById(1L);
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        List<UserDto> users = List.of(
                UserDto.builder().id(1L).name("Петр").email("pet@mail.com").build(),
                UserDto.builder().id(2L).name("Жанна").email("janna@example.com").build()
        );
        Mockito.when(service.getAll()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Петр"))
                .andExpect(jsonPath("$[0].email").value("pet@mail.com"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Жанна"))
                .andExpect(jsonPath("$[1].email").value("janna@example.com"));

        Mockito.verify(service, Mockito.times(1)).getAll();
    }

    @Test
    void shouldDeleteUser() throws Exception {
        Mockito.doNothing().when(service).deleteUser(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1)).deleteUser(1L);
    }
}