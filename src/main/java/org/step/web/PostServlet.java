package org.step.web;

import org.step.model.Post;
import org.step.model.User;
import org.step.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/posts")
public class PostServlet extends HttpServlet {

    private PostService postService = new PostService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Post> posts = postService.findAll();
        if (posts != null) {
            String collect = posts.stream().map(Post::toString).collect(Collectors.joining("|"));
            resp.getWriter().println(collect);
        } else {
            resp.setContentType("text/html");
            resp.setStatus(404);
            resp.getOutputStream().write("Posts not found".getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String description = req.getParameter("description");

        HttpSession session = req.getSession();

        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.setStatus(401);
            resp.getOutputStream().write("User is not identified".getBytes(StandardCharsets.UTF_8));
        }

        int userId = user.getId();

        boolean isSaved = postService.save(name, description, userId);
        if (isSaved) {
            resp.setStatus(201);
            resp.setContentType("application/json");
            resp.getOutputStream().write("Successfully saved".getBytes(StandardCharsets.UTF_8));
        } else {
            /*
            req.getRequestDispatcher
            include
            forward
             */
            resp.setStatus(404);
            resp.setContentType("application/json");
            resp.sendRedirect("/error");
        }
    }
}
