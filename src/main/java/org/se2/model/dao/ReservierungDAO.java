package org.se2.model.dao;

import com.vaadin.ui.Notification;
import org.se2.model.objects.dto.AutoDTO;
import org.se2.model.objects.dto.KundeDTO;
import org.se2.model.objects.dto.UserDTO;

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

}
