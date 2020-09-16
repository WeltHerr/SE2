package org.se2.model.dao;

import org.se2.process.exceptions.DatabaseException;
import org.se2.services.db.JDBCConnection;

import java.sql.PreparedStatement;

public class AbstractDAO {

    protected PreparedStatement getPreparedStatement(String sql) {
        PreparedStatement statement = null;

        try {
            statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        return statement;
    }
}

