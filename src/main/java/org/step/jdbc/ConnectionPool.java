package org.step.jdbc;

import java.sql.Connection;

public interface ConnectionPool {

    void init() throws Exception;

    Connection getConnection();

    void returnConnection(Connection connection) throws InterruptedException;

    void closePool() throws Exception;
}
