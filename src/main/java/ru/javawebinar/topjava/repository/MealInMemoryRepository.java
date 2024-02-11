package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealInMemoryRepository implements MealRepository {
    private final Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();
    private final AtomicInteger counterId = new AtomicInteger(0);

    public MealInMemoryRepository() {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        meals.forEach(this::save);
    }

    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public Meal findById(int id) {
        return mealMap.get(id);
    }

    @Override
    public Integer save(Meal meal) {
        int id;
        if (meal.getId() == null) {
            id = counterId.getAndIncrement();
            meal.setId(id);
            mealMap.put(id, meal);
        } else {
            Meal mealNew = mealMap.computeIfPresent(meal.getId(), (integer, mealOld) -> meal);
            id = mealNew != null ? mealNew.getId() : null;
        }
        return id;
    }

    @Override
    public void deleteById(int id) {
        mealMap.remove(id);
    }
}
