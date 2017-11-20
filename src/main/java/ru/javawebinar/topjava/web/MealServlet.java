package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static String INSERT_OR_EDIT = "/edit.jsp";
    private static String LIST_MEAL = "/meals.jsp";

    private MealDao dao;
    public MealServlet()
    {
        super();
        dao = new MealDao();
    }
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("delete")){
            int id = Integer.parseInt(request.getParameter("id"));
            dao.deleteMeal(id);
            forward = LIST_MEAL;
            List<MealWithExceed> mealList = MealsUtil.getFilteredWithExceededByCycle(dao.getAllMeals(), LocalTime.MIN, LocalTime.MAX, 2000);
            request.setAttribute("meals", mealList);
            log.debug("Meal was deleted");
        }
        else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            int id = Integer.parseInt(request.getParameter("id"));
            Meal meal = dao.getMealById(id);
            request.setAttribute("meal", meal);
            request.setAttribute("wrongFields", request.getParameter("wrongFields"));
            log.debug("redirect to edit meal");
        }
        else if (action.equalsIgnoreCase("mealList")){
            forward = LIST_MEAL;
            List<MealWithExceed> mealList = MealsUtil.getFilteredWithExceededByCycle(dao.getAllMeals(), LocalTime.MIN, LocalTime.MAX, 2000);
            request.setAttribute("meals", mealList);
            log.debug("redirect to mealsList");
        }
        else if (action.equalsIgnoreCase("insert")){
            forward = INSERT_OR_EDIT;
            List<MealWithExceed> mealList = MealsUtil.getFilteredWithExceededByCycle(dao.getAllMeals(), LocalTime.MIN, LocalTime.MAX, 2000);
            request.setAttribute("meals", mealList);
            request.setAttribute("wrongFields", request.getParameter("wrongFields"));
            log.debug("redirect to mealsList");
        }
        else {
            forward = LIST_MEAL;
            List<MealWithExceed> mealList = MealsUtil.getFilteredWithExceededByCycle(dao.getAllMeals(), LocalTime.MIN, LocalTime.MAX, 2000);
            request.setAttribute("meals", mealList);
            log.debug("!from else!");
        }
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDate localDate;
        LocalTime localTime;
        int calories;
        try {
            localDate = LocalDate.parse(request.getParameter("date"), DateTimeFormatter.ofPattern(request.getParameter("dateFormat")));
            localTime = LocalTime.parse(request.getParameter("time"));
            calories = Integer.parseInt(request.getParameter("calories"));
        } catch (Exception e) {
            log.warn("Fields wrong input");
            if (request.getParameter("id").isEmpty())
                response.sendRedirect("meals?action=insert&wrongFields=true");
            else
                response.sendRedirect("meals?action=edit&id=" + request.getParameter("id") + "&wrongFields=true"); //for edit
            return; // sendRedirect != return :( ?
        }
        String description = request.getParameter("description");
        String id = request.getParameter("id");
        if(id == null || id.isEmpty())
        {
            dao.addMeal(new Meal(LocalDateTime.of(localDate, localTime), description, calories));
        }
        else
        {
            int mealId = Integer.parseInt(id);
            Meal meal = new Meal(LocalDateTime.of(localDate, localTime), description, calories);
            meal.setId(mealId);
            dao.updateMeal(meal);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
        request.setAttribute("meals", MealsUtil.getFilteredWithExceededByCycle(dao.getAllMeals(), LocalTime.MIN, LocalTime.MAX, 2000));
        view.forward(request, response);

    }
}
