package org.se2.model.dao;

import com.vaadin.ui.Notification;
import org.se2.model.objects.dto.AutoDTO;
import org.se2.model.objects.dto.UserDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AutoDAO extends AbstractDAO {
    private static AutoDAO autoDAO = null;

    public static AutoDAO getInstance() {
        if (autoDAO == null) {
            autoDAO = new AutoDAO();
        }
        return autoDAO;
    }

    public List<AutoDTO> getAutosForSearch(String suchtext, String filter) throws SQLException {
        filter = filter.toLowerCase();
        PreparedStatement statement;
        ResultSet rs = null;
        if (suchtext.equals("")) {
            String sql = "SELECT id, marke, modell, baujahr, beschreibung " +
                    "FROM carlookltd.car;";
            statement = this.getPreparedStatement(sql);
            try {
                rs = statement.executeQuery();
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
            }
        } else {
            String sql = "SELECT id, marke, modell, baujahr, beschreibung " +
                    "FROM carlookltd.car " +
                    "WHERE " + filter + " like ? ;";
            statement = this.getPreparedStatement(sql);


            try {
                statement.setString(1, "%" + suchtext + "%");
                rs = statement.executeQuery();
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
            }
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

    //Erstellt ein neues Auto in der Datenbank
    public boolean createAuto(AutoDTO autoDTO, UserDTO userDTO) {
        String sql = "INSERT INTO carlookltd.car(id, marke, modell, baujahr, beschreibung)" +
                "VALUES (default, ?, ?, ?, ?)";

        PreparedStatement statement = this.getPreparedStatement(sql);

        try {
            statement.setString(1, autoDTO.getMarke());
            statement.setString(2, autoDTO.getModell());
            statement.setString(3, autoDTO.getBaujahr());
            statement.setObject(4, autoDTO.getBeschreibung());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }

    }

    //Gibt alle inserierten Autos eines Unternehmer zurück
    public List<AutoDTO> getAutoForVertriebler(UserDTO userDTO) throws SQLException {
        String sql = "SELECT id, marke, modell, baujahr, beschreibung " +
                "FROM carlookltd.car " +
                "WHERE vertriebler_id = ? ;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        ResultSet rs = null;
        try {
            statement.setInt(1, userDTO.getId());
            rs = statement.executeQuery();
        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
        }
        List<AutoDTO> list = new ArrayList<>();
        assert rs != null;
        buildList(rs, list);
        return list;
    }

    //Löscht ein Auto aus der Datenbank
    public boolean deleteAuto(AutoDTO autoDTO) {
        String sql = "DELETE " +
                "FROM carlookltd.car " +
                "WHERE carlookltd.car.id = ? ;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setInt(1, autoDTO.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    //Verändert die Daten eines Autos in der DB
    public boolean updateAuto(AutoDTO autoDTO) {
        String sql = "UPDATE carlookltd.car " +
                "SET marke = ?, modell = ?,  baujahr = ?, beschreibung = ?  " +
                "WHERE carlookltd.car.id = ? ;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        try {
            statement.setString(1, autoDTO.getMarke());
            statement.setString(2, autoDTO.getModell());
            statement.setString(3, autoDTO.getBaujahr());
            statement.setString(4, autoDTO.getBeschreibung());
            statement.setInt(5, autoDTO.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
}
