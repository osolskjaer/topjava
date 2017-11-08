package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> filteredWithExceeded = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        filteredWithExceeded.forEach(System.out::println);
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        /* For-loops
        Map<LocalDate, Integer> dayCalories = new HashMap<>();
        for (UserMeal userMeal: mealList)
        {
            LocalDate localDate = userMeal.getDateTime().toLocalDate();
            dayCalories.merge(localDate, userMeal.getCalories(), (v1, v2) -> v1 + v2);
        }

        List<UserMealWithExceed> result = new ArrayList<>();
        for (UserMeal userMeal: mealList)
        {
            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                result.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), dayCalories.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
        }
        */
        Map<LocalDate, Integer> dayCalories = mealList.stream().collect(Collectors.toMap(m -> m.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));
        List<UserMealWithExceed> result = mealList.stream()
                .filter(n -> TimeUtil.isBetween(n.getDateTime().toLocalTime(), startTime, endTime))
                .map(n -> new UserMealWithExceed(n.getDateTime(), n.getDescription(), n.getCalories(), dayCalories.get(n.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
        return result;
    }
}
