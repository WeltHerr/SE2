package org.se2.model.dao;

import org.se2.model.objects.dto.UserDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public class RegisterDAO extends AbstractDAO {
    private static RegisterDAO dao = null;

    private RegisterDAO() {

    }

    public static RegisterDAO getInstance() {
        if (dao == null) {
            dao = new RegisterDAO();
        }
        return dao;
    }

    //User hinzufügen
    public boolean addUser(UserDTO userDTO) {
        String sql = "INSERT INTO carlookltd.user VALUES (default,?,?,?)";
        PreparedStatement statement = this.getPreparedStatement(sql);

        try {
            statement.setString(1, userDTO.getEmail());
            statement.setString(2, userDTO.getPassword());
            statement.setString(3, userDTO.getName());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
}
