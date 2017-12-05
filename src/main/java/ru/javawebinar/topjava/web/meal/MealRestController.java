package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    private Collection<Meal> getAll() {
        log.info("getAll");
        return service.getAll()
                .stream()
                .filter(meal -> meal.getUserId() == AuthorizedUser.id())
                .collect(Collectors.toList());
    }

    private boolean hasRights(Meal meal)
    {
        if (meal.getUserId() != AuthorizedUser.id()) {
            log.warn("trying to get access to another's meal");
            throw new NotFoundException("We have not found such meal");
        }
        else return true;
    }

    private boolean hasRights(int id)
    {
        return hasRights(get(id));
    }

    public List<MealWithExceed> getAllWithExceed() {
        log.info("getAll");
        return MealsUtil.getWithExceeded(getAll(), AuthorizedUser.getCaloriesPerDay());
    }

    public List<MealWithExceed> getFilteredWithExceed(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getAll");
        return MealsUtil.getFilteredWithExceeded(getAll(), startTime, endTime, AuthorizedUser.getCaloriesPerDay())
                .stream()
                .filter(record -> (record.getDateTime().toLocalDate().isAfter(startDate) || record.getDateTime().toLocalDate().equals(startDate)) && record.getDateTime().toLocalDate().isBefore(endDate) || record.getDateTime().toLocalDate().isEqual(endDate))
                .collect(Collectors.toList());
    }

    public Meal get(int id) throws NotFoundException {
        Meal meal = service.get(id);
        if (meal != null && hasRights(meal)) {
            log.info("get meal with id = {}", id);
            return meal;
        }
        return null;
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal);
    }

    public void delete(int id) throws NotFoundException{
        if (hasRights(id))
        {
            log.info("delete {}", id);
            service.delete(id);
        }
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal);
    }

    public void update(int id) {
        log.info("update meal with id={}", id);
        service.update(get(id));
    }

}