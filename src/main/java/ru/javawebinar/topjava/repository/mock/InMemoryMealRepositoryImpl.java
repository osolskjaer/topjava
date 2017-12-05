package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;

import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    private boolean hasRights(Meal meal)
    {
        if (meal.getUserId() != AuthorizedUser.id()) {
            log.warn("trying to change another's meal");
            return false;
        }
        else return true;
    }

    private boolean hasRights(int id)
    {
        return hasRights(get(id));
    }

    @Override
    public Meal save(Meal meal) {
        //if (hasRights(meal)) {
            log.info("save {}", meal);
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
            }
            repository.put(meal.getId(), meal);
       // }
        return meal;
    }

    @Override
    public boolean delete(int id) {
        if (hasRights(id)) {
            log.info("delete {}", id);
            return repository.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        log.info("getAll");
        List<Meal> meals = new ArrayList<>(repository.values());
        Collections.sort(meals, (o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()));
        return meals;
    }
}

