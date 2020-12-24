package org.step.service;

import org.step.jdbc.PostDao;
import org.step.model.Post;

import java.util.List;

public class PostService {

    private PostDao postDao = new PostDao();

    public List<Post> findAll() {
        try {
            return postDao.findAll();
        } catch (InterruptedException e) {
            return null;
        }
    }

    public boolean save(String name, String description, int userId) {
        try {
            postDao.savePost(new Post(0, name, description, userId));
            return true;
        } catch (Exception e) {
            System.out.println("Log: " + e.getLocalizedMessage());
            return false;
        }
    }
}
