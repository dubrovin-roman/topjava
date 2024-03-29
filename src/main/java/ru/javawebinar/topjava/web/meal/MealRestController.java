package ru.javawebinar.topjava.web.meal;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController extends AbstractMealController {

    public Meal getMeal(int id) {
        return super.get(id);
    }

    public void deleteMeal(int id) {
        super.delete(id);
    }

    public List<MealTo> getAllMeals() {
        return super.getAll();
    }

    public Meal createMeal(Meal meal) {
        return super.create(meal);
    }

    public void updateMeal(Meal meal, int id) {
        super.update(meal, id);
    }

    /**
     * <ol>Filter separately
     * <li>by date</li>
     * <li>by time for every date</li>
     * </ol>
     */
    public List<MealTo> getBetweenMeals(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                   @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}