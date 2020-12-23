package org.step.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserDao {

    public final ConnectionPool connectionPool;

    public UserDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public Integer saveUser(User user) throws InterruptedException {
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement st = connection
                    .prepareStatement("INSERT INTO USERS(name) VALUES(?) RETURNING id");

            st.setString(1, user.getName());

            ResultSet resultSet = st.executeQuery();

            return resultSet.next() ? resultSet.getInt("id") : null;
        } catch (Exception e) {
            System.out.printf("Exception: %s, message: %s%n", e.getClass(), e.getLocalizedMessage());
        } finally {
            connectionPool.returnConnection(connection);
        }
        return null;
    }

    public List<User> findAll() throws InterruptedException {
        Connection connection = connectionPool.getConnection();

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT ID, NAME FROM USERS");

            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                users.add(new User(resultSet.getInt("ID"), resultSet.getString("NAME")));
            }
            return users;
        } catch (Exception e) {
            System.out.printf("Exception: %s, message: %s%n", e.getClass(), e.getLocalizedMessage());
        } finally {
            connectionPool.returnConnection(connection);
        }
        return Collections.emptyList();
    }
}
