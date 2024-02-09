package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MealInMemoryRepository implements MealRepository {
    private final List<Meal> mealList = new CopyOnWriteArrayList<>();
    private final AtomicInteger counterId = new AtomicInteger(1);

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
        return Collections.unmodifiableList(mealList);
    }

    @Override
    public Meal findById(int id) {
        return mealList.get(getIndexById(id));
    }

    @Override
    public int save(Meal meal) {
        int mealId;
        if (meal.getId() == 0) {
            mealId = counterId.getAndIncrement();
            meal.setId(mealId);
            mealList.add(meal);
        } else {
            mealId = meal.getId();
            Meal mealFromList = mealList.get(getIndexById(mealId));
            mealFromList.setDateTime(meal.getDateTime());
            mealFromList.setDescription(meal.getDescription());
            mealFromList.setCalories(meal.getCalories());
        }
        return mealId;
    }

    @Override
    public void deleteById(int mealId) {
        mealList.remove(getIndexById(mealId));
    }

    private int getIndexById(int mealId) {
        int index = -1;
        for (int i = 0; i < mealList.size(); i++) {
            if (mealList.get(i).getId() == mealId)
                index = i;
        }
        return index;
    }
}
