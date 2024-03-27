package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

public abstract class AbstractMealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService mealService;

    public List<MealTo> getAll(int userId) {
        log.info("getAll for user {}", userId);
        return MealsUtil.getTos(mealService.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id, int userId) {
        log.info("get meal {} for user {}", id, userId);
        return mealService.get(id, userId);
    }

    public void delete(int id, int userId) {
        log.info("delete meal {} for user {}", id, userId);
        mealService.delete(id, userId);
    }

    public Meal create(Meal meal, int userId) {
        checkNew(meal);
        log.info("create {} for user {}", meal, userId);
        return mealService.create(meal, userId);
    }

    public void update(Meal meal, int id, int userId) {
        assureIdConsistent(meal, id);
        log.info("update {} for user {}", meal, userId);
        mealService.update(meal, userId);
    }

    public List<MealTo> getBetween(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                   @Nullable LocalDate endDate, @Nullable LocalTime endTime,
                                   int userId) {
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

        List<Meal> mealsDateFiltered = mealService.getBetweenInclusive(startDate, endDate, userId);
        return MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}