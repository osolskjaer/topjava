package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.repository.mock.InMemoryUserRepositoryImpl;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceImpl;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.service.UserServiceImpl;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
    AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
    MealRestController mealRestController = appCtx.getBean(MealRestController.class);

    public void destroy()
    {
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                AuthorizedUser.id(),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew()) mealRestController.create(meal);
        else mealRestController.update(meal, Integer.valueOf(id));
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String userIdParameter = request.getParameter("userId");
        if (userIdParameter != null) AuthorizedUser.setId(Integer.parseInt(userIdParameter));
        int userId = AuthorizedUser.id();
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                if (mealRestController.get(id).getUserId() == userId)
                {
                    log.info("Delete {}", id);
                    mealRestController.delete(id);
                }
                else throw new NotFoundException("not allowed");
                response.sendRedirect("meals");
                break;
            case "create":
                Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "update":
                id = getId(request);
                if (mealRestController.get(id).getUserId() == userId) {
                    meal = mealRestController.get(id);
                    request.setAttribute("meal", meal);
                    request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                }
                else
                    throw new NotFoundException("not allowed");
                break;
            case "all":
            default:
                log.info("getAll");
                List<MealWithExceed> mealWithExceedList = mealRestController.getFilteredWithExceed(getDateFromParameter(request, "dateFrom"), getTimeFromParameter(request, "timeFrom"), getDateFromParameter(request, "dateTo"), getTimeFromParameter(request, "timeTo"));
                request.setAttribute("meals",
                        mealWithExceedList);
                request.setAttribute("username", adminUserController.get(userId).getName());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }

    private LocalDate getDateFromParameter(HttpServletRequest request, String parameterName)
    {
        String parameter = request.getParameter(parameterName);
        if (parameterName.contains("From")) // lul
        {
            return parameter == null || !parameter.matches("^[0-3].*") ? LocalDate.MIN : LocalDate.parse(parameter, DateTimeUtil.DATE_FORMATTER);
        }
        else return parameter == null || !parameter.matches("^[0-3].*") ? LocalDate.MAX : LocalDate.parse(parameter, DateTimeUtil.DATE_FORMATTER);
    }

    private LocalTime getTimeFromParameter(HttpServletRequest request, String parameterName)
    {
        String parameter = request.getParameter(parameterName);
        if (parameterName.contains("From")) // lul
        {
            return parameter == null || !parameter.matches("^[0-2].*") ? LocalTime.MIN : LocalTime.parse(parameter, DateTimeUtil.TIME_FORMATTER);
        }
        else return parameter == null || !parameter.matches("^[0-2].*") ? LocalTime.MAX : LocalTime.parse(parameter, DateTimeUtil.TIME_FORMATTER);
    }


}
