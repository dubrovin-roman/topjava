package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealInMemoryRepository;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private final static int CALORIES_PER_DAY = 2000;
    private static final Logger log = getLogger(MealServlet.class);
    private MealRepository mealRepository;

    @Override
    public void init() throws ServletException {
        mealRepository = new MealInMemoryRepository();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();

        switch (action) {
            case "/meals":
                displayMeals(req, resp);
                break;
            case "/meals/delete":
                deleteMeal(req, resp);
                break;
            case "/meals/new":
                showNewForm(req, resp);
                break;
            case "/meals/insert":
                insertMeals(req, resp);
                break;
            case "/meals/edit":
                showEditForm(req, resp);
                break;
            case "/meals/update":
                updateMeal(req, resp);
                break;
        }
    }

    private void displayMeals(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");
        req.setAttribute("meals", MealsUtil.convertMealListTo(mealRepository.findAll(), CALORIES_PER_DAY));
        req.getRequestDispatcher("/meal/meals.jsp").forward(req, resp);
    }

    private void deleteMeal(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        mealRepository.deleteById(id);
        log.debug("delete meal with id = " + id);
        resp.sendRedirect(req.getContextPath() + "/meals");
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to form new meal");
        req.getRequestDispatcher("/meal/meal-form.jsp").forward(req, resp);
    }

    private void insertMeals(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LocalDateTime localDateTime = LocalDateTime.parse(req.getParameter("localDateTime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        Meal meal = new Meal(localDateTime, description, calories);
        int id = mealRepository.save(meal);
        log.debug("create new meal with id = " + id);
        resp.sendRedirect(req.getContextPath() + "/meals");
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Meal mealFromDB = mealRepository.findById(id);
        req.setAttribute("meal", mealFromDB);
        log.debug("redirect to form edit meal with id = " + id);
        req.getRequestDispatcher("/meal/meal-form.jsp").forward(req, resp);
    }

    private void updateMeal(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(req.getParameter("id"));
        LocalDateTime localDateTime = LocalDateTime.parse(req.getParameter("localDateTime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        Meal meal = new Meal(id, localDateTime, description, calories);
        mealRepository.save(meal);
        log.debug("update meal with id = " + id);
        resp.sendRedirect(req.getContextPath() + "/meals");
    }
}
