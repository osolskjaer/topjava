package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;


import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final int APPLE_ID = START_SEQ + 2;
    private static final int BANANA_ID = START_SEQ + 3;
    private static final int CUCUMBER_ID = START_SEQ + 4;

    public static final Meal APPLE = new Meal(APPLE_ID, LocalDateTime.of(2016, 1, 1, 12, 0), "apple", 100);
    public static final Meal BANANA = new Meal(BANANA_ID, LocalDateTime.of(2015, 12, 31, 0, 0), "banana", 200);
    public static final Meal CUCUMBER = new Meal(CUCUMBER_ID, LocalDateTime.of(2015, 12, 31, 7, 0), "cucumber", 20);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    private static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
