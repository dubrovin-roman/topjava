package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.NOT_FOUND;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
    @Test
    public void getWithUser() {
        Meal actual = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, adminMeal1);
        User userActual = actual.getUser();
        USER_MATCHER.assertMatch(userActual, admin);
    }

    @Test
    public void getWithUserNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(NOT_FOUND, USER_ID));
    }

    @Test
    public void getWithUserNotOwn() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(MEAL1_ID, ADMIN_ID));
    }
}
