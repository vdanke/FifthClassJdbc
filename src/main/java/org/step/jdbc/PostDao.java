package org.step.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PostDao {

    private final ConnectionPool connectionPool;

    public PostDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

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
            System.out.println(e.getLocalizedMessage());
        } finally {
            connectionPool.returnConnection(connection);
        }
    }
}
