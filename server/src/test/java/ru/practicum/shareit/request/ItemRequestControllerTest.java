package ru.practicum.shareit.request;

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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateItemRequest() throws Exception {
        ItemRequestCreateDto createDto = ItemRequestCreateDto.builder().description("Нужен фен").build();
        ItemRequestDto responseDto = ItemRequestDto.builder()
                .id(1L)
                .description("Нужен фен")
                .requester(UserDto.builder().id(1L).name("Петр").email("pet@mail.com").build())
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .build();
        Mockito.when(itemRequestService.createItemRequest(eq(1L), any(ItemRequestCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Нужен фен"));

        Mockito.verify(itemRequestService, Mockito.times(1)).createItemRequest(eq(1L), any(ItemRequestCreateDto.class));
    }

    @Test
    void shouldReturnItemRequestsByUserId() throws Exception {
        List<ItemRequestDto> requests = List.of(
                ItemRequestDto.builder().id(1L).description("Нужен фен").requester(null).created(LocalDateTime.now()).items(Collections.emptyList()).build()
        );
        Mockito.when(itemRequestService.getItemRequestsByUserId(1L)).thenReturn(requests);

        mockMvc.perform(MockMvcRequestBuilders.get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("Нужен фен"));

        Mockito.verify(itemRequestService, Mockito.times(1)).getItemRequestsByUserId(1L);
    }

    @Test
    void shouldReturnItemRequestsByNotUserId() throws Exception {
        List<ItemRequestDto> requests = List.of(
                ItemRequestDto.builder().id(2L).description("Нужен переходник").requester(null).created(LocalDateTime.now()).items(Collections.emptyList()).build()
        );
        Mockito.when(itemRequestService.findAllByNotRequesterIdSorted(1L)).thenReturn(requests);

        mockMvc.perform(MockMvcRequestBuilders.get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].description").value("Нужен переходник"));

        Mockito.verify(itemRequestService, Mockito.times(1)).findAllByNotRequesterIdSorted(1L);
    }

    @Test
    void shouldReturnItemRequestById() throws Exception {
        ItemRequestDto responseDto = ItemRequestDto.builder()
                .id(3L)
                .description("Нужен чайник")
                .requester(null)
                .created(LocalDateTime.now())
                .items(List.of(ItemDto.builder().id(1L).name("Чайник").description("Чайник электрический").available(true).build()))
                .build();
        Mockito.when(itemRequestService.getItemRequestById(3L)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/requests/3")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.description").value("Нужен чайник"))
                .andExpect(jsonPath("$.items[0].id").value(1L))
                .andExpect(jsonPath("$.items[0].name").value("Чайник"));

        Mockito.verify(itemRequestService, Mockito.times(1)).getItemRequestById(3L);
    }
}