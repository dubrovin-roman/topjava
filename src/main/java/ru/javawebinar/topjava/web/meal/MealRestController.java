package ru.javawebinar.topjava.web.meal;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController extends AbstractMealController {

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        return super.get(id, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        super.delete(id, userId);
    }

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        return super.getAll(userId);
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        return super.create(meal, userId);
    }

    public void update(Meal meal, int id) {
        int userId = SecurityUtil.authUserId();
        super.update(meal, id, userId);
    }

    /**
     * <ol>Filter separately
     * <li>by date</li>
     * <li>by time for every date</li>
     * </ol>
     */
    public List<MealTo> getBetween(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                   @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        return super.getBetween(startDate, startTime, endDate, endTime, userId);
    }
}