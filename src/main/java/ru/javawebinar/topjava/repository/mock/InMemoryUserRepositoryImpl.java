package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final List<User> USERS = Arrays.asList(
            new User(1, "Петр", "piotr@mail.ru", "123456", Role.ROLE_USER),
            new User(2, "Василий", "vasiliy@gmail.com", "1qW35g45a6", Role.ROLE_ADMIN),
            new User("Дмитрий Валентинович", "dimitry@yandex.ru", "FDbemvi21n"),
            new User("Патрик", "bonjour@yandex.fr", "EWbnl23b5di"),
            new User("Barack Obama", "barack@yahoo.com", "9379992")
    );

    {
        USERS.forEach(this::save);
    }
    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew())
            user.setId(counter.incrementAndGet());
        repository.put(user.getId(), user);
        return user;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        List<User> users = new ArrayList<>(repository.values());
        Collections.sort(users, Comparator.comparing(AbstractNamedEntity::getName));
        return users;
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        Objects.requireNonNull(email);
        return repository.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(null);
    }
}
