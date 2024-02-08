package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealInMemoryRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

/**
 * @see <a href="https://javaops-demo.ru/topjava">Demo application</a>
 * @see <a href="https://github.com/JavaOPs/topjava">Initial project</a>
 */
public class Main {
    public static void main(String[] args) {
        System.out.format("Hello TopJava Enterprise!");

        MealInMemoryRepository inMemoryRepository = MealInMemoryRepository.getInstance();

        List<Meal> mealList = inMemoryRepository.findAll();
        mealList.forEach(System.out::println);
        System.out.println("-------------------------------------------------------");

        Meal newMeal = new Meal(LocalDateTime.of(2024, Month.JANUARY, 31, 8, 0), "Завтрак", 1000);
        inMemoryRepository.saveMeal(newMeal);

        List<Meal> mealList2 = inMemoryRepository.findAll();
        mealList2.forEach(System.out::println);
        System.out.println("-------------------------------------------------------");

        Meal mealFromDB = inMemoryRepository.findById(2);
        System.out.println(mealFromDB);
        System.out.println("-------------------------------------------------------");

        Meal newMeal1 = new Meal(LocalDateTime.of(2024, Month.JANUARY, 31, 8, 0), "Завтрак", 1000);
        newMeal1.setId(3);
        inMemoryRepository.saveMeal(newMeal1);

        List<Meal> mealList3 = inMemoryRepository.findAll();
        mealList3.forEach(System.out::println);
        System.out.println("-------------------------------------------------------");
    }
}
