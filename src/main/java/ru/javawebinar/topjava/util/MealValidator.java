package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

@Component
public class MealValidator implements Validator {
    @Autowired
    private MealRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Meal.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Meal mealTarget = (Meal) target;
        if (mealTarget.getDateTime() == null) {
            return;
        }
        int userId = SecurityUtil.authUserId();
        List<Meal> mealList = repository.getBetweenHalfOpen(mealTarget.getDateTime(), mealTarget.getDateTime().plusMinutes(1), userId);
        if (!mealList.isEmpty()) {
            if (!mealTarget.isNew() && mealTarget.getId().equals(mealList.getFirst().getId())) {
                return;
            }
            errors.rejectValue("dateTime", "error.meal.dateTime.exists");
        }
    }
}
