package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collections;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getWithListMeals() {
        User userActual = service.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(userActual, user);
        MEAL_MATCHER.assertMatch(userActual.getMeals(), getUserWithMeals().getMeals());
    }

    @Test
    public void getWithEmptyListMeals() {
        User userActual = service.getWithMeals(GUEST_ID);
        USER_MATCHER.assertMatch(userActual, guest);
        MEAL_MATCHER.assertMatch(userActual.getMeals(), Collections.emptyList());
    }

    @Test
    public void getWithMealsNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(NOT_FOUND));
    }
}
