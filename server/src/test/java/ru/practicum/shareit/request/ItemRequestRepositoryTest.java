package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNot.not;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/data/cleanup.sql", "/data/data.sql"})
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository repository;

    @Test
    void shouldReturnAllRequestsByRequesterIdSorted() {
        Long requesterId = 2L;
        List<ItemRequest> list = repository.findAllByRequesterIdSorted(requesterId);
        assertThat(list.size(), equalTo(1));
        assertThat(list.getFirst().getRequester().getId(), equalTo(requesterId));
        assertThat(list.getFirst().getItems().getFirst(), notNullValue());
    }

    @Test
    void shouldReturnAllRequestsByNotRequesterIdSorted() {
        Long requesterId = 2L;
        List<ItemRequest> list = repository.findAllByNotRequesterIdSorted(requesterId);
        assertThat(list.size(), equalTo(2));
        assertThat(list.getFirst().getRequester().getId(), not(equalTo(requesterId)));
        assertThat(list.getFirst().getItems().getFirst(), notNullValue());
        assertThat(list.getLast().getRequester().getId(), not(equalTo(requesterId)));
        assertThat(list.getLast().getItems().getFirst(), notNullValue());
    }
}