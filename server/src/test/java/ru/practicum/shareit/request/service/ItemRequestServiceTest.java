package ru.practicum.shareit.request.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(scripts = "/data/data_repository.sql")
class ItemRequestServiceTest {
    private final ItemRequestService itemRequestService;
    private final UserService service;

    ItemRequestCreateDto createDto = ItemRequestCreateDto.builder()
            .description("Нужен переходник")
            .build();

    @Test
    void shouldCreateItemRequest() {
        ItemRequestDto createdRequestDto = itemRequestService.createItemRequest(1, createDto);
        assertThat(createDto.getDescription(), equalTo(createdRequestDto.getDescription()));
        assertThat(createdRequestDto.getRequester(), notNullValue());
        assertThat(createdRequestDto.getItems().size(), equalTo(0));
        assertThrows(NotFoundException.class, () ->
                itemRequestService.createItemRequest(999, createDto));
    }

    @Test
    void shouldReturnItemRequestsByUserId() {
        long requesterId = 1L;
        ItemRequestCreateDto createDto = ItemRequestCreateDto.builder()
                .description("Нужен переходник")
                .build();
        UserCreateDto usercreateDto = UserCreateDto.builder()
                .name("Катерина")
                .email("user@mail.ru")
                .build();

        UserDto userDtoCreated = service.create(usercreateDto);
        ItemRequestDto createdRequestDto = itemRequestService.createItemRequest(2, createDto);
        List<ItemRequestDto> list = itemRequestService.getItemRequestsByUserId(requesterId);

        assertThat(list.size(), equalTo(1));
        assertThat(list.getFirst().getRequester().getId(), equalTo(requesterId));
        assertThat(list.getFirst().getItems().getFirst(), notNullValue());
        assertThrows(NotFoundException.class, () ->
                itemRequestService.getItemRequestsByUserId(999));
        assertThrows(NotFoundException.class, () ->
                itemRequestService.getItemRequestsByUserId(createdRequestDto.getId()));
        assertThrows(NotFoundException.class, () ->
                itemRequestService.getItemRequestsByUserId(userDtoCreated.getId()));
    }

    @Test
    void shouldReturnAllByNotRequesterIdSorted() {
        long requesterId = 1L;
        List<ItemRequestDto> list = itemRequestService.findAllByNotRequesterIdSorted(requesterId);

        assertThat(list.size(), equalTo(4));
        assertThrows(NotFoundException.class, () ->
                itemRequestService.findAllByNotRequesterIdSorted(999));
        for (ItemRequestDto dto : list) {
            assertThat(dto.getRequester().getId(), not(equalTo(requesterId)));
            assertThat(dto.getItems().getFirst(), notNullValue());
        }

        ItemRequestDto createdRequestDto = itemRequestService.createItemRequest(2, createDto);
        assertThrows(NotFoundException.class, () ->
                itemRequestService.findAllByNotRequesterIdSorted(createdRequestDto.getId()));
    }

    @Test
    void shouldReturnItemRequestById() {
        ItemRequestDto createdRequestDto = itemRequestService.createItemRequest(2, createDto);
        ItemRequestDto findRequestDto = itemRequestService.getItemRequestById(createdRequestDto.getId());

        assertThat(createDto.getDescription(), equalTo(findRequestDto.getDescription()));
        assertThat(createdRequestDto.getRequester(), equalTo(findRequestDto.getRequester()));
        assertThat(createdRequestDto.getItems().size(), equalTo(findRequestDto.getItems().size()));
        assertThrows(NotFoundException.class, () ->
                itemRequestService.getItemRequestById(999));
    }
}