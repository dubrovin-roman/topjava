package ru.javawebinar.topjava.repository.datajpa;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudMealRepository;
    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudMealRepository, CrudUserRepository crudUserRepository) {
        this.crudMealRepository = crudMealRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew() || get(meal.getId(), userId) != null) {
            User ref = crudUserRepository.getReferenceById(userId);
            meal.setUser(ref);
            return crudMealRepository.save(meal);
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudMealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudMealRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.findAllByUserId(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudMealRepository.findBetweenHalfOpen(startDateTime, endDateTime, userId);
    }

    @Override
    @Transactional
    public Meal getWithUser(int id, int userId) {
        Meal meal = get(id, userId);
        if (meal == null) {
            return null;
        } else {
            User user = Hibernate.unproxy(meal.getUser(), User.class);
            meal.setUser(user);
            return meal;
        }
    }
}
