package org.step.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcExample {

    public static void main(String[] args) throws Exception {
        ConnectionPool connectionPool = new ConnectionPoolImpl();

        connectionPool.init();

        UserDao userDao = new UserDao(connectionPool);
        PostDao postDao = new PostDao(connectionPool);

        User user = new User(0, "Third user");

        Integer integer = userDao.saveUser(user);

        Post post = new Post(0, "name", "desc", integer);

        postDao.savePost(post);

        System.out.println("No exceptions");
    }

    public static void saveUser(User user, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO USERS(name) VALUES (?)");

        preparedStatement.setString(1, user.getName());

        preparedStatement.executeUpdate();


    }

    public static List<User> findAll(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT ID, NAME FROM USERS");

        List<User> users = new ArrayList<>();

        while(resultSet.next()) {
            users.add(
                    new User(
                            resultSet.getInt("ID"),
                            resultSet.getString("NAME")
                    )
            );
        }
        return users;
    }

    public void saveBatchUsers(List<User> users, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO USERS(name) VALUES(?)");

        for (int i = 0; i < users.size(); i++) {
            preparedStatement.setString(1, users.get(i).getName());
            preparedStatement.addBatch();
            if (i % 100 == 0) {
                preparedStatement.executeBatch();
            }
        }
    }
}
