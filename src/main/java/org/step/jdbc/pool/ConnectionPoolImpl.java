package org.step.jdbc.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPoolImpl implements ConnectionPool {

    private static final String dbUrl;
    private static final String dbUser;
    private static final String dbPassword;
    private static final int dbPoolSize;
    private static final ConnectionPoolImpl CONNECTION_POOL = new ConnectionPoolImpl();
    private static final ArrayBlockingQueue<Connection> connections;
    private static final Lock lock = new ReentrantLock();

    static {
        lock.lock();
        ResourceBundle db = ResourceBundle.getBundle("db");

        dbUrl = db.getString("db.url");
        dbUser = db.getString("db.user");
        dbPassword = db.getString("db.password");
        dbPoolSize = Integer.parseInt(db.getString("db.poolsize"));
        connections = new ArrayBlockingQueue<>(dbPoolSize);

        init();
        lock.unlock();
    }

    private static void init() {
        try {
            Class.forName("org.postgresql.Driver");

            Connection connection = DriverManager
                    .getConnection(dbUrl, dbUser, dbPassword);

            for (int i = 0; i < dbPoolSize; i++) {
                connections.add(connection);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static ConnectionPool getInstance() {
        return CONNECTION_POOL;
    }

    @Override
    public Connection getConnection() {
        return connections.poll();
    }

    @Override
    public void returnConnection(Connection connection) throws InterruptedException {
        connections.offer(connection);
    }
}
