package com.example.isa.library.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.example.isa.library.util.UrlPath.LOGIN;

@WebServlet("/locale")
public class LocaleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String language = req.getParameter("lang");

        req.getSession().setAttribute("lang", language);

        String prevPage = req.getHeader("referer");
        String page = prevPage != null ? prevPage : LOGIN;

        resp.sendRedirect(page + "?lang=" + language);
    }
}
