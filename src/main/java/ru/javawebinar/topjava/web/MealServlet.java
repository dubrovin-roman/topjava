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
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final int CALORIES_PER_DAY = 2000;
    private static final Logger log = getLogger(MealServlet.class);
    private MealRepository mealRepository;

    @Override
    public void init() throws ServletException {
        mealRepository = new MealInMemoryRepository();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();

        switch (action) {
            case "/meals/insert":
                saveMeal(req, resp, true);
                break;
            case "/meals/update":
                saveMeal(req, resp, false);
                break;
        }
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
            case "/meals/edit":
                showEditForm(req, resp);
                break;
        }
    }

    private void displayMeals(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");
        req.setAttribute("meals", MealsUtil.filteredByStreams(mealRepository.findAll(),
                LocalTime.MIN,
                LocalTime.MAX,
                CALORIES_PER_DAY));
        req.getRequestDispatcher("/meal/meals.jsp").forward(req, resp);
    }

    private void deleteMeal(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        mealRepository.deleteById(id);
        log.debug("has been deleted meal with id = " + id);
        resp.sendRedirect(req.getContextPath() + "/meals");
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to form new meal");
        req.getRequestDispatcher("/meal/meal-form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Meal mealFromDB = mealRepository.findById(id);
        req.setAttribute("meal", mealFromDB);
        log.debug("redirect to form edit meal with id = " + id);
        req.getRequestDispatcher("/meal/meal-form.jsp").forward(req, resp);
    }

    private void saveMeal(HttpServletRequest req, HttpServletResponse resp, boolean isAddNew) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LocalDateTime localDateTime = LocalDateTime.parse(req.getParameter("localDateTime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        Meal meal;
        String messageLog;
        int id;
        if (isAddNew) {
            meal = new Meal(localDateTime, description, calories);
            messageLog = "has been created new meal with id = ";
        } else {
            id = Integer.parseInt(req.getParameter("id"));
            meal = new Meal(id, localDateTime, description, calories);
            messageLog = "has been updated meal with id = ";
        }
        id = mealRepository.save(meal);
        log.debug(messageLog + id);
        resp.sendRedirect(req.getContextPath() + "/meals");
    }
}
