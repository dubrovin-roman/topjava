package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.MealTestData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(MealTestData.ADMIN_MEAL_1_ID, UserTestData.ADMIN_ID);
        MealTestData.assertMatch(meal, MealTestData.adminMeal1);
    }

    @Test
    public void getSomeoneElseMeal() {
        assertThrows(NotFoundException.class,
                () -> mealService.get(MealTestData.USER_MEAL_1_ID, UserTestData.ADMIN_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class,
                () -> mealService.get(MealTestData.NOT_FOUND, UserTestData.USER_ID));
    }

    @Test
    public void delete() {
        mealService.delete(MealTestData.ADMIN_MEAL_1_ID, UserTestData.ADMIN_ID);
        assertThrows(NotFoundException.class,
                () -> mealService.get(MealTestData.ADMIN_MEAL_1_ID, UserTestData.ADMIN_ID));
    }

    @Test
    public void deleteSomeoneElseMeal() {
        assertThrows(NotFoundException.class,
                () -> mealService.delete(MealTestData.USER_MEAL_1_ID, UserTestData.ADMIN_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class,
                () -> mealService.delete(MealTestData.NOT_FOUND, UserTestData.USER_ID));
    }

    @Test
    public void getAdminMealsBetweenInclusive() {
        List<Meal> adminMealsBetweenInclusive = mealService.getBetweenInclusive(LocalDate.of(2024, 2, 11),
                LocalDate.of(2024, 2, 15),
                UserTestData.ADMIN_ID);
        MealTestData.assertMatch(adminMealsBetweenInclusive, MealTestData.getAdminMealsBetweenInclusive());
    }

    @Test
    public void getUserMealsBetweenInclusive() {
        List<Meal> userMealsBetweenInclusive = mealService.getBetweenInclusive(LocalDate.of(2024, 2, 21),
                LocalDate.of(2024, 2, 22),
                UserTestData.USER_ID);
        MealTestData.assertMatch(userMealsBetweenInclusive, MealTestData.getUserMeals());
    }

    @Test
    public void getAllByUserId() {
        List<Meal> userMeals = mealService.getAll(UserTestData.USER_ID);
        MealTestData.assertMatch(userMeals, MealTestData.getUserMeals());
    }

    @Test
    public void getAllByAdminId() {
        List<Meal> adminMeals = mealService.getAll(UserTestData.ADMIN_ID);
        MealTestData.assertMatch(adminMeals, MealTestData.getAdminMeals());
    }

    @Test
    public void update() {
        mealService.update(MealTestData.getUpdated(), UserTestData.USER_ID);
        Meal updated = MealTestData.getUpdated();
        MealTestData.assertMatch(mealService.get(updated.getId(), UserTestData.USER_ID), updated);
    }

    @Test
    public void updateSomeoneElseMeal() {
        Meal updated = MealTestData.getUpdated();
        assertThrows(NotFoundException.class,
                () -> mealService.update(updated, UserTestData.ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = mealService.create(MealTestData.getNew(), UserTestData.GUEST_ID);
        Integer newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        MealTestData.assertMatch(created, newMeal);
        MealTestData.assertMatch(mealService.get(newId, UserTestData.GUEST_ID), newMeal);
    }

    @Test
    public void createDuplicateDateTime() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 2, 26, 12, 0, 0);
        mealService.create(new Meal(localDateTime, "Description", 1000), UserTestData.USER_ID);
        assertThrows(DataAccessException.class, () -> mealService.create(new Meal(localDateTime, "DuplicateDescription", 2500), UserTestData.USER_ID));
    }
}