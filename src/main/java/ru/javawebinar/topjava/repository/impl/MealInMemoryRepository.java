package ru.javawebinar.topjava.repository.impl;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class MealInMemoryRepository implements MealRepository {
    private static MealInMemoryRepository mealInMemoryRepository;
    private final CopyOnWriteArrayList<Meal> mealList = new CopyOnWriteArrayList<>();
    private static final AtomicInteger counterId = new AtomicInteger(1);

    private MealInMemoryRepository() {
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

    public static MealInMemoryRepository getInstance() {
        if (mealInMemoryRepository == null)
            mealInMemoryRepository = new MealInMemoryRepository();

        return mealInMemoryRepository;
    }

    @Override
    public List<Meal> findAll() {
        return Collections.unmodifiableList(this.mealList);
    }

    @Override
    public Meal findById(int id) {
        return this.mealList.get(getIndexMealInListById(id));
    }

    @Override
    public void save(Meal meal) {
        if (meal.getId() == 0) {
            int newId = counterId.getAndIncrement();
            meal.setId(newId);
            this.mealList.add(meal);
        } else {
            Meal mealFromList = this.mealList.get(getIndexMealInListById(meal.getId()));
            mealFromList.setDateTime(meal.getDateTime());
            mealFromList.setDescription(meal.getDescription());
            mealFromList.setCalories(meal.getCalories());
        }
    }

    @Override
    public void deleteById(int mealId) {
        this.mealList.remove(getIndexMealInListById(mealId));
    }

    private int getIndexMealInListById(int mealId) {
        int index = -1;
        for (int i = 0; i < this.mealList.size(); i++) {
            if (this.mealList.get(i).getId() == mealId)
                index = i;
        }
        return index;
    }
}
