package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRepositoryImpl implements UserRepository {
    private long id = 1L;
    private final Map<Long, User> users = new HashMap<>();

    public long getNextId() {
        return id++;
    }

    @Override
    public User create(User user) {
        checkEmailExist(user.getEmail());
        long id = getNextId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        long id = user.getId();
        checkUserId(id);
        User userSaved = users.get(id);

        if (user.getName() != null) {
            userSaved.setName(user.getName());
        }
        if (user.getEmail() != null) {
            String email = user.getEmail();
            checkEmailExist(email);
            userSaved.setEmail(email);
        }
        return userSaved;
    }

    @Override
    public User getUserById(long id) {
        checkUserId(id);
        return users.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
    }

    private void checkEmailExist(String email) {
        List<User> sameEmailUsers = users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .toList();
        if (!sameEmailUsers.isEmpty()) {
            throw new ConflictException("email уже существует");
        }
    }

    private void checkUserId(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User отсутствует");
        }
    }
}