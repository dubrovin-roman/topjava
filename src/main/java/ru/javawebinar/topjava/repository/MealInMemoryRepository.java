package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class MealInMemoryRepository {
    private static MealInMemoryRepository mealInMemoryRepository;
    private final List<Meal> mealList = new ArrayList<>();
    private final static int CALORIES_PER_DAY = 2000;

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

        meals.forEach(this::saveMeal);
    }

    public static MealInMemoryRepository getInstance() {
        if (mealInMemoryRepository == null)
            mealInMemoryRepository = new MealInMemoryRepository();

        return mealInMemoryRepository;
    }

    public List<Meal> findAll() {
        return Collections.unmodifiableList(this.mealList);
    }

    public List<MealTo> findAllMealTo() {
        return MealsUtil.filteredByStreams(findAll(),
                LocalTime.of(0, 0),
                LocalTime.of(23, 59),
                CALORIES_PER_DAY);
    }

    public Meal findById(int id) {
        return this.mealList.get(getIndexMealInListById(id));
    }

    public void saveMeal(Meal meal) {
        if (meal.getId() == 0) {
            int newId = getLastIdMealInList() + 1;
            meal.setId(newId);
            this.mealList.add(meal);
        } else {
            Meal mealFromList = this.mealList.get(getIndexMealInListById(meal.getId()));
            mealFromList.setDateTime(meal.getDateTime());
            mealFromList.setDescription(meal.getDescription());
            mealFromList.setCalories(meal.getCalories());
        }
    }

    private int getIndexMealInListById(int mealId) {
        int index = -1;
        for (int i = 0; i < this.mealList.size(); i++) {
            if (this.mealList.get(i).getId() == mealId)
                index = i;
        }
        return index;
    }

    private int getLastIdMealInList() {
        if (this.mealList.isEmpty())
            return 0;

        List<Integer> idList = this.mealList.stream()
                .map(Meal::getId)
                .sorted()
                .collect(Collectors.toList());

        return idList.get(idList.size() - 1);
    }
}
