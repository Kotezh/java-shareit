package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateComment() throws Exception {
        CommentCreateDto createDto = CommentCreateDto.builder().text("Хорошо").build();
        CommentDtoReturn responseDto = CommentDtoReturn.builder()
                .id(1L)
                .text("Хорошо")
                .authorName("Петр")
                .created(LocalDateTime.now())
                .build();
        Mockito.when(itemService.createComment(eq(1L), eq(2L), any(CommentCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/items/2/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value("Хорошо"));

        Mockito.verify(itemService, Mockito.times(1)).createComment(eq(1L), eq(2L), any(CommentCreateDto.class));
    }

    @Test
    void shouldCreateItem() throws Exception {
        ItemCreateDto createDto = ItemCreateDto.builder().name("Чайник").description("Чайник электрический").available(true).build();
        ItemDto responseDto = ItemDto.builder()
                .id(1L)
                .name("Чайник")
                .description("Чайник электрический")
                .available(true)
                .owner(UserDto.builder().id(1L).name("Петр").email("pet@mail.com").build())
                .build();
        Mockito.when(itemService.createItem(any(ItemCreateDto.class), eq(1L))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Чайник"));

        Mockito.verify(itemService, Mockito.times(1)).createItem(any(ItemCreateDto.class), eq(1L));
    }

    @Test
    void shouldUpdateItem() throws Exception {
        ItemUpdateDto updateDto = ItemUpdateDto.builder().name("Новый чайник").description("Новый чайник электрический").available(false).build();
        ItemDto responseDto = ItemDto.builder()
                .id(1L)
                .name("Новый чайник")
                .description("Новый чайник электрический")
                .available(false)
                .owner(UserDto.builder().id(1L).name("Петр").email("pet@mail.com").build())
                .build();
        Mockito.when(itemService.updateItem(any(ItemUpdateDto.class), eq(1L), eq(1L))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Новый чайник"));

        Mockito.verify(itemService, Mockito.times(1)).updateItem(any(ItemUpdateDto.class), eq(1L), eq(1L));
    }

    @Test
    void shouldReturnItemByItemIdWithComment() throws Exception {
        ItemCommentDto responseDto = ItemCommentDto.builder()
                .id(1L)
                .name("Чайник")
                .description("Чайник электрический")
                .available(true)
                .owner(UserDto.builder().id(1L).name("Петр").email("pet@mail.com").build())
                .comments(Collections.emptyList())
                .build();
        Mockito.when(itemService.getByItemId(1L, 1L)).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Чайник"));

        Mockito.verify(itemService, Mockito.times(1)).getByItemId(1L, 1L);
    }

    @Test
    void shouldReturnAllItemsByOwnerIdWithComment() throws Exception {
        List<ItemCommentDto> items = List.of(
                ItemCommentDto.builder().id(1L).name("Чайник").description("Чайник электрический").available(true).owner(null).comments(Collections.emptyList()).build()
        );
        Mockito.when(itemService.getAllItems(1L)).thenReturn(items);

        mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Чайник"));

        Mockito.verify(itemService, Mockito.times(1)).getAllItems(1L);
    }

    @Test
    void shouldReturnItemsByNameOrDescription() throws Exception {
        List<ItemDto> items = List.of(
                ItemDto.builder().id(2L).name("Переходник").description("Переходник на micro usb").available(true).owner(null).build()
        );
        Mockito.when(itemService.getItemsByNameOrDescription("переходник")).thenReturn(items);

        mockMvc.perform(MockMvcRequestBuilders.get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "переходник"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].name").value("Переходник"));

        Mockito.verify(itemService, Mockito.times(1)).getItemsByNameOrDescription("переходник");
    }
}