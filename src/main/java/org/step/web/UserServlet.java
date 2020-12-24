package org.step.web;

import org.step.model.User;
import org.step.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(urlPatterns = "/login")
public class UserServlet extends HttpServlet {

    private UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String username = req.getParameter("username");

        User user = userService.login(username);

        if (user != null) {
            Cookie cookie = new Cookie("Status", "Awesome");

            cookie.setDomain("localhost");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(3600);
            cookie.setSecure(true);
            cookie.setVersion(1);
            cookie.setComment("This is cookie for user who is awesome");

            session.setAttribute("user", user);

            resp.addCookie(cookie);
            resp.setStatus(200);
            resp.setContentType("application/json");
            resp.getOutputStream().write("Successfully logged in".getBytes(StandardCharsets.UTF_8));
        } else {
            resp.setStatus(401);
        }
    }
}
