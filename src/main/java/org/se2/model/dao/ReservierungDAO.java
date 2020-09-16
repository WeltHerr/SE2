package org.se2.model.dao;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import org.se2.model.objects.dto.AutoDTO;
import org.se2.model.objects.dto.KundeDTO;
import org.se2.model.objects.dto.UserDTO;
import org.se2.process.exceptions.DatabaseException;
import org.se2.process.exceptions.ReservierungException;
import org.se2.services.db.JDBCConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservierungDAO extends AbstractDAO{

    private static ReservierungDAO reservierungDAO = null;

    public static ReservierungDAO getInstance() {
        if (reservierungDAO == null) {
            reservierungDAO = new ReservierungDAO();
        }
        return reservierungDAO;
    }

    //Erstellt eine Reservierung
    public boolean createReservierung(AutoDTO autoDTO, UserDTO userDTO) throws SQLException{
        String sql = "INSERT INTO carlookltd.user_reserve_car (user_id, car_id) " +
                "VALUES (?, ?); ";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setInt(1, userDTO.getId());
            statement.setInt(2, autoDTO.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    //Löscht ein Auto aus der Datenbank
    public void deleteReservierung(AutoDTO autoDTO, KundeDTO kundeDTO) throws SQLException {
        String sql = "DELETE " +
                "FROM carlookltd.user_reserve_car " +
                "WHERE user_reserve_car.user_id = ? AND user_reserve_car.car_id = ?; ";
        PreparedStatement statement = this.getPreparedStatement(sql);
            statement.setInt(1, kundeDTO.getId());
            statement.setInt(2,autoDTO.getId());
            statement.executeUpdate();

    }

    //Gibt alle reservierten Autos eines Kunden zurück
    public List<AutoDTO> getReservierungenForKunde(KundeDTO kundeDTO) throws SQLException {
        String sql = "SELECT carlookltd.car.id, carlookltd.car.marke, carlookltd.car.modell, carlookltd.car.baujahr, carlookltd.car.beschreibung " +
                "FROM carlookltd.car, carlookltd.user, carlookltd.user_reserve_car " +
                "WHERE carlookltd.user.id = ? AND carlookltd.user.id = carlookltd.user_reserve_car.user_id AND carlookltd.user_reserve_car.car_id = carlookltd.car.id ;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        ResultSet rs = null;
        try {
            statement.setInt(1, kundeDTO.getId());
            rs = statement.executeQuery();
        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        }
        List<AutoDTO> list = new ArrayList<>();
        assert rs != null;
        buildList(rs, list);
        return list;
    }

    private void buildList(ResultSet rs, List<AutoDTO> listauto) throws SQLException {
        AutoDTO autoDTO;
        try {
            while (rs.next()) {

                autoDTO = new AutoDTO();
                autoDTO.setId(rs.getInt(1));
                autoDTO.setMarke(rs.getString(2));
                autoDTO.setModell(rs.getString(3));
                autoDTO.setBaujahr(rs.getString(4));
                autoDTO.setBeschreibung(rs.getString(5));
                listauto.add(autoDTO);
            }
        } catch (SQLException e) {
            Notification.show("Es ist ein schwerer SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        } finally {
            assert rs != null;
            rs.close();
        }
    }

    public void checkAlreadyApplied(AutoDTO autoDTO, UserDTO userDTO) throws SQLException, ReservierungException, DatabaseException {
        KundeDTO kundeDTO = new KundeDTO(userDTO);
        List<AutoDTO> list = ReservierungDAO.getInstance().getReservierungenForKunde(kundeDTO);
        String sql = "SELECT car_id " +
                "FROM carlookltd.user_reserve_car " +
                "WHERE user_id = ? " +
                "AND car_id = ?";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        ResultSet rs = null;
        for (AutoDTO autoDTO1 : list) {
            int id_car = autoDTO1.getId();
            try {
                statement.setInt(1, userDTO.getId());
                statement.setInt(2, id_car);
                rs = statement.executeQuery();
                if (rs.next()) {
                    throw new ReservierungException();
                }
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte kontaktieren Sie den Administrator!", Notification.Type.ERROR_MESSAGE);
            } finally {
                assert rs != null;
                rs.close();
            }
        }

    }
    public void checkAllowed(AutoDTO autoDTO, UserDTO userDTO, Button reservierenButton) {
        try {
            checkAlreadyApplied(autoDTO, userDTO);
        } catch (DatabaseException e) {
            Notification.show("Es ist ein Datenbankfehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
        } catch (ReservierungException e) {
            reservierenButton.setVisible(false);
        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte kontaktieren Sie den Administrator!", Notification.Type.ERROR_MESSAGE);
        }
    }
}
