package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDao {
    private static AtomicInteger idCounter = new AtomicInteger(0);
    private static List<Meal> meals = Collections.synchronizedList(new ArrayList<>());
    {
        addMeal(new Meal( LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 550));
        addMeal(new Meal( LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 950));
        addMeal(new Meal( LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        addMeal(new Meal( LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        addMeal(new Meal( LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        addMeal(new Meal( LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    public MealDao() {
    }

    public void addMeal(Meal meal) {
        meal.setId(idCounter.incrementAndGet());
        meals.add(meal);
    }

    public synchronized void deleteMeal(int id) {
            ListIterator<Meal> listIterator = meals.listIterator();
            while (listIterator.hasNext()) {
                if (listIterator.next().getId() == id)
                {
                    listIterator.remove();
                    break;
                }
            }
    }

    public synchronized void updateMeal(Meal meal) {

        ListIterator<Meal> listIterator = meals.listIterator();
        while (listIterator.hasNext()) {
            if (listIterator.next().getId() == meal.getId())
            {
                listIterator.set(meal);
                break;
            }
        }
    }

    public List<Meal> getAllMeals() {
        return meals;
    }

    public Meal getMealById(int id) {
        Meal meal = null;
        ListIterator<Meal> listIterator = meals.listIterator();
        while (listIterator.hasNext())
        {
            Meal current = listIterator.next();
            if (current.getId() == id)
            {
                meal = current;
                break;
            }
        }
        return meal;
    }
}
