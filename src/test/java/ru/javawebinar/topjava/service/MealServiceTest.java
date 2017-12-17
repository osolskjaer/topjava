package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

import static org.junit.Assert.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void get() throws Exception {
        Meal apple = mealService.get(APPLE_ID, USER_ID);
        assertMatch(apple, APPLE);
    }

    @Test(expected = NotFoundException.class)
    public void getNotOwn() throws Exception {
        Meal apple = mealService.get(APPLE_ID, ADMIN_ID);
    }

    @Test
    public void delete() throws Exception {
        mealService.delete(APPLE_ID, USER_ID);
        assertMatch(mealService.getAll(USER_ID), CUCUMBER, BANANA);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotOwn() throws Exception {
        mealService.delete(APPLE_ID, ADMIN_ID);
    }

    @Test
    public void getBetweenDates() throws Exception {
        List<Meal> meals = mealService.getBetweenDates(LocalDate.of(2015, 12, 31),
                LocalDate.of(2015, 12, 31), USER_ID);
        assertMatch(meals, CUCUMBER, BANANA);
    }

    @Test
    public void getBetweenDateTimes() throws Exception {
        List<Meal> meals = mealService.getBetweenDateTimes(LocalDateTime.of(2015, 12, 31, 0, 0, 0),
                LocalDateTime.of(2015, 12, 31, 7, 0, 0), USER_ID);
        assertMatch(meals, CUCUMBER, BANANA);
    }

    @Test
    public void getAll() throws Exception {
        List<Meal> mealList = mealService.getAll(USER_ID);
        assertMatch(mealList, APPLE, CUCUMBER, BANANA);
    }

    @Test
    public void update() throws Exception {
        Meal updated = new Meal(APPLE);
        updated.setDescription("YammyApple");
        updated.setCalories(120);
        mealService.update(updated, USER_ID);
        assertMatch(mealService.get(APPLE_ID, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotOwn() throws Exception {
        Meal updated = new Meal(APPLE);
        updated.setDescription("YammyApple");
        updated.setCalories(120);
        mealService.update(updated, ADMIN_ID);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = new Meal(null, LocalDateTime.now(), "jelly", 250);
        Meal created = mealService.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(mealService.getAll(USER_ID), created, APPLE, CUCUMBER, BANANA);
    }

}