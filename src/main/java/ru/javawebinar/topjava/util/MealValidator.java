package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;
import java.util.Locale;

@Component
public class MealValidator implements Validator {
    @Autowired
    private MealRepository repository;
    @Autowired
    private MessageSource messageSource;
    private Locale locale = Locale.getDefault();

    @Override
    public boolean supports(Class<?> clazz) {
        return Meal.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Meal mealTarget = (Meal) target;
        int userId = SecurityUtil.authUserId();
        List<Meal> mealList = repository.getAll(userId);
        boolean isPresent = mealList.stream().anyMatch(meal -> meal.getDateTime().equals(mealTarget.getDateTime()));
        if(isPresent) {
            errors.rejectValue("dateTime",
                    "dateTime.exists",
                    messageSource.getMessage("error.meal.dateTime.exists", new Object[] {}, locale));
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
