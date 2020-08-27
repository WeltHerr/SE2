package org.se2.model.objects.dto;

import com.vaadin.ui.Notification;
import org.se2.model.dao.RoleDAO;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public class UserDTO extends AbstractDTO implements Serializable {
    private int id;
    private String name;
    private String email;
    private String password;
    private List<RoleDTO> roles = null;

    public UserDTO() {}
    public UserDTO(UserDTO userDTO) {
        this.id = userDTO.id;
        this.name = userDTO.name;
        this.email = userDTO.email;
        this.password = userDTO.password;
        this.roles = userDTO.roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String id) {
        this.email = id;
    }

    public boolean hasRole(String role){
        if (this.roles == null) {
            getRoles();

        }
        for(RoleDTO r : roles) {
            if (r.getBezeichnung().equals(role)) return true;
        }
        return false;

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private void getRoles() {
        try {
            this.roles = RoleDAO.getInstance().getRolesForUser(this);
        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
        }
    }
}
