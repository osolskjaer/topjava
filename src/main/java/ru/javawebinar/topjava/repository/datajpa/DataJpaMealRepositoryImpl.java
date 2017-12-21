package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DataJpaMealRepositoryImpl implements MealRepository {
    private static final Sort SORT_DATE = new Sort(Sort.Direction.DESC, "dateTime");

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CrudMealRepository crudRepository;

    @Override
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }
        meal.setUser(em.getReference(User.class, userId));
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Optional<Meal> found = crudRepository.findById(id);
        if (found.isPresent())
        {
            if (found.get().getUser().getId() == userId) return found.orElse(null);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAll(SORT_DATE).stream()
                .filter(meal -> meal.getUser().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return em.createNamedQuery(Meal.GET_BETWEEN, Meal.class)
                .setParameter("userId", userId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate).getResultList();
    }
}
