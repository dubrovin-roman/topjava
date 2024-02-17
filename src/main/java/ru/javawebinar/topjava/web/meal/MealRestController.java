package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class MealRestController {

    private static final Logger log = getLogger(MealRestController.class);

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        ValidationUtil.checkNew(meal);
        log.info("create {}", meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public Meal getNew() {
        log.info("getNew");
        return new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
    }

    public void update(Meal meal, int id) {
        ValidationUtil.assureIdConsistent(meal, id);
        log.info("update {} with id = {}", meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return service.getAll(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getAllFilterByDateAndTime(String startDateStr, String endDateStr, String startTimeStr, String endTimeStr) {
        log.info("getAll");
        List<MealTo> mealTos;
        if (startDateStr == null && endDateStr == null && startTimeStr == null && endTimeStr == null) {
            mealTos = service.getAll(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
        } else {
            startDateStr = startDateStr == null ? "" : startDateStr;
            LocalDate startDate = startDateStr.isEmpty() ? null : LocalDate.parse(startDateStr);
            endDateStr = endDateStr == null ? "" : endDateStr;
            LocalDate endDate = endDateStr.isEmpty() ? null : LocalDate.parse(endDateStr);
            startTimeStr = startTimeStr == null ? "" : startTimeStr;
            LocalTime startTime = startTimeStr.isEmpty() ? null : LocalTime.parse(startTimeStr);
            endTimeStr = endTimeStr == null ? "" : endTimeStr;
            LocalTime endTime = endTimeStr.isEmpty() ? null : LocalTime.parse(endTimeStr);
            mealTos = service.getAllFilterByDateAndTime(SecurityUtil.authUserId(), startDate, endDate, startTime, endTime, SecurityUtil.authUserCaloriesPerDay());
        }

        return mealTos;
    }
 }