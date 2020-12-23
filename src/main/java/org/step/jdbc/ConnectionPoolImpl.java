package org.step.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;

public class ConnectionPoolImpl implements ConnectionPool {

    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private int dbPoolsize;

    {
        ResourceBundle db = ResourceBundle.getBundle("db");

        this.dbUrl = db.getString("db.url");
        this.dbUser = db.getString("db.user");
        this.dbPassword = db.getString("db.password");
        this.dbPoolsize = Integer.parseInt(db.getString("db.poolsize"));
    }

    private ArrayBlockingQueue<Connection> connections = new ArrayBlockingQueue<>(dbPoolsize);

    @Override
    public void init() throws Exception {
        Class.forName("org.postgresql.Driver");

        Connection connection = DriverManager
                .getConnection(dbUrl, dbUser, dbPassword);

        for (int i = 0; i < dbPoolsize; i++) {
            connections.add(connection);
        }
    }

    @Override
    public Connection getConnection() {
        return connections.poll();
    }

    @Override
    public void returnConnection(Connection connection) throws InterruptedException {
        connections.offer(connection);
    }

    @Override
    public void closePool() throws Exception {
        this.connections = new ArrayBlockingQueue<>(5);
    }
}
