package org.step.jdbc.pool;

import java.sql.Connection;

public interface ConnectionPool {

    Connection getConnection();

    void returnConnection(Connection connection) throws InterruptedException;
}
