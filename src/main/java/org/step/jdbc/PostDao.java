package org.step.jdbc;

import org.step.jdbc.pool.ConnectionPool;
import org.step.jdbc.pool.ConnectionPoolImpl;
import org.step.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostDao {

    private final ConnectionPool connectionPool = ConnectionPoolImpl.getInstance();

    public void savePost(Post post) throws InterruptedException {
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO POSTS(NAME, DESCRIPTION, USER_ID) VALUES(?, ?, ?)");

            preparedStatement.setString(1, post.getName());
            preparedStatement.setString(2, post.getDescription());
            preparedStatement.setInt(3, post.getUserId());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException();
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    public List<Post> findAll() throws InterruptedException {
        Connection connection = connectionPool.getConnection();

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT ID, NAME, DESCRIPTION FROM POSTS");

            List<Post> postList = new ArrayList<>();

            while (resultSet.next()) {
                Post post = new Post(resultSet.getInt("ID"), resultSet.getString("NAME"), resultSet.getString("DESCRIPTION"));
                postList.add(post);
            }
            return postList;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getLocalizedMessage());
        } finally {
            connectionPool.returnConnection(connection);
        }
        return null;
    }
}
