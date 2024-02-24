package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int NOT_FOUND = 100;
    public static final int USER_MEAL_1_ID = START_SEQ + 3;
    public static final int USER_MEAL_2_ID = START_SEQ + 4;
    public static final int USER_MEAL_3_ID = START_SEQ + 5;
    public static final int USER_MEAL_4_ID = START_SEQ + 6;
    public static final int USER_MEAL_5_ID = START_SEQ + 7;
    public static final int USER_MEAL_6_ID = START_SEQ + 8;
    public static final int ADMIN_MEAL_1_ID = START_SEQ + 9;
    public static final int ADMIN_MEAL_2_ID = START_SEQ + 10;
    public static final int ADMIN_MEAL_3_ID = START_SEQ + 11;
    public static final int ADMIN_MEAL_4_ID = START_SEQ + 12;
    public static final int ADMIN_MEAL_5_ID = START_SEQ + 13;
    public static final int ADMIN_MEAL_6_ID = START_SEQ + 14;
    public static final Meal userMeal1 = new Meal(USER_MEAL_1_ID, LocalDateTime.of(2024, 2, 21, 8, 5), "Завтрак", 1000);
    public static final Meal userMeal2 = new Meal(USER_MEAL_2_ID, LocalDateTime.of(2024, 2, 21, 13, 30), "Обед", 1500);
    public static final Meal userMeal3 = new Meal(USER_MEAL_3_ID, LocalDateTime.of(2024, 2, 21, 18, 10), "Ужин", 1000);
    public static final Meal userMeal4 = new Meal(USER_MEAL_4_ID, LocalDateTime.of(2024, 2, 22, 8, 10), "Завтрак", 600);
    public static final Meal userMeal5 = new Meal(USER_MEAL_5_ID, LocalDateTime.of(2024, 2, 22, 14, 25), "Обед", 900);
    public static final Meal userMeal6 = new Meal(USER_MEAL_6_ID, LocalDateTime.of(2024, 2, 22, 19, 0), "Ужин", 450);
    public static final Meal adminMeal1 = new Meal(ADMIN_MEAL_1_ID, LocalDateTime.of(2024, 2, 10, 8, 5), "Завтрак", 3000);
    public static final Meal adminMeal2 = new Meal(ADMIN_MEAL_2_ID, LocalDateTime.of(2024, 2, 10, 13, 30), "Обед", 1500);
    public static final Meal adminMeal3 = new Meal(ADMIN_MEAL_3_ID, LocalDateTime.of(2024, 2, 10, 18, 10), "Ужин", 1000);
    public static final Meal adminMeal4 = new Meal(ADMIN_MEAL_4_ID, LocalDateTime.of(2024, 2, 15, 8, 10), "Завтрак", 750);
    public static final Meal adminMeal5 = new Meal(ADMIN_MEAL_5_ID, LocalDateTime.of(2024, 2, 15, 14, 25), "Обед", 900);
    public static final Meal adminMeal6 = new Meal(ADMIN_MEAL_6_ID, LocalDateTime.of(2024, 2, 15, 19, 0), "Ужин", 250);

    public static List<Meal> getUserMeals() {
        return Stream.of(userMeal1, userMeal2, userMeal3, userMeal4, userMeal5, userMeal6)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    public static List<Meal> getAdminMeals() {
        return Stream.of(adminMeal1, adminMeal2, adminMeal3, adminMeal4, adminMeal5, adminMeal6)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    public static List<Meal> getAdminMealsBetweenInclusive() {
        return Stream.of(adminMeal4, adminMeal5, adminMeal6)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2024, 2, 23, 11, 0, 0), "Test meal", 1000);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal1);
        updated.setDateTime(LocalDateTime.of(2024, 2, 24, 13, 0, 0));
        updated.setDescription("UpdatedDescription");
        updated.setCalories(3000);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
