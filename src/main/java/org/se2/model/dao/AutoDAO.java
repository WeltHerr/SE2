package org.se2.model.dao;

import com.vaadin.ui.Notification;
import org.se2.model.objects.dto.AutoDTO;

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
}
