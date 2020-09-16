package org.se2.process.control;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.se2.gui.ui.MyUI;
import org.se2.gui.windows.ConfirmationWindow;
import org.se2.model.dao.RegisterDAO;
import org.se2.model.dao.RoleDAO;
import org.se2.model.dao.UserDAO;
import org.se2.model.objects.dto.UserDTO;
import org.se2.process.exceptions.*;
import org.se2.services.db.JDBCConnection;
import org.se2.services.util.Roles;
import org.se2.services.util.Views;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationControl {
    private static RegistrationControl registration = null;

    private RegistrationControl(){
    }

    public static RegistrationControl getInstance(){
        if(registration == null){
            registration = new RegistrationControl();
        }
        return registration;
    }

    public void checkValid(String email, boolean emailBool, String password1, String password2, boolean password1Bool, boolean password2Bool, boolean checkBox, String regAs) throws NoEqualPasswordException, DatabaseException, EmailInUseException, EmptyFieldException, SQLException, NoCarlookMailException {

        //Eingabecheck
        if (!emailBool || !password1Bool  || !password2Bool || !checkBox) {
            throw new EmptyFieldException("Bitte ergänzen Sie Ihre Eingaben in den makierten Bereichen!");
        }

        //@carlook.de check
        if ( regAs.equals(Roles.VERTRIEBLER) & !email.endsWith("@carlook.de") ) {
            throw new NoCarlookMailException("Bitte geben Sie eine Emailadresse des Vertriebs ein!");
        }
        //Passwortcheck
        if ( !password1.equals(password2) ) {
            throw new NoEqualPasswordException("Passwörter stimmen nicht überein!");
        }

        //DB Zugriff Emailcheck
        String sql = "SELECT email " +
                     "FROM carlookltd.user " +
                     "WHERE email = ? ;";
        ResultSet rs = null;
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);

        try {
            statement.setString(1,email);
            rs = statement.executeQuery();
        } catch (SQLException throwables) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
        }

        try {
            assert rs != null;
            if (rs.next()) {
                throw new EmailInUseException("Die Email wird bereits benutzt!");
            }
        } catch (SQLException throwables) {
            throw new DatabaseException("Fehler bei set: Bitte den Programmierer informieren!");
        } finally {
            assert rs != null;
            rs.close();
        }
    }

    //User registrieren
    public void registerUser( String email, String password, String name , String regAs) throws DatabaseException, SQLException {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setName(name);
        boolean registerUser;
        RegisterDAO.getInstance().addUser(userDTO);
        userDTO.setId(UserDAO.getInstance().getMaxID());

        if (regAs.equals(Roles.KUNDE)) {
            //RegisterDAO.getInstance().addKunde(userDTO);
            registerUser = RoleDAO.getInstance().setRolesForKunde(userDTO);
        } else {
            //RegisterDAO.getInstance().addVertriebler(userDTO);
            registerUser = RoleDAO.getInstance().setRolesForVertriebler(userDTO);
        }

        if (registerUser) {
            UI.getCurrent().addWindow( new ConfirmationWindow("Registration erfolgreich!") );
            ( (MyUI)UI.getCurrent() ).setUserDTO(userDTO);
            UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
        } else {
            throw new DatabaseException("Fehler bei Abschluß der Registration");
        }

    }

}
