package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger(0);

    public InMemoryMealRepository() {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        MealsUtil.meals2.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUserId(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }

        // handle case: update, but not present in storage
        return get(meal.getId(), userId) == null ? null : repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return get(id, userId) != null && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        return meal != null && meal.getUserId() != userId ? null : meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return filterByPredicate(meal -> meal.getUserId() == userId);
    }

    @Override
    public List<Meal> getAllFilterByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return filterByPredicate(meal -> meal.getUserId() == userId
                && DateTimeUtil.isBetweenDate(meal.getDate(), startDate, endDate));
    }

    private List<Meal> filterByPredicate(Predicate<Meal> filter) {
        return repository.values()
                .stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

