package org.se2.process.control;

import com.vaadin.ui.UI;
import org.se2.gui.ui.MyUI;
import org.se2.model.dao.AutoDAO;
import org.se2.model.objects.dto.AutoDTO;
import org.se2.model.objects.dto.KundeDTO;
import org.se2.model.objects.dto.UserDTO;
import org.se2.model.objects.dto.VertrieblerDTO;
import org.se2.process.exceptions.AutoException;

import java.sql.SQLException;
import java.util.List;

public class AutoControl {
    private static AutoControl autoControl = null;

    private AutoControl() {
    }

    public static AutoControl getInstance() {
        if (autoControl == null) {
            autoControl = new AutoControl();
        }
        return autoControl;
    }

    public List<AutoDTO> getAnzeigenForKunde(KundeDTO kundeDTO) throws SQLException {
        return null;
    }

    public List<AutoDTO> getAnzeigenForVertriebler(VertrieblerDTO vertrieblerDTO) throws SQLException {
        return AutoDAO.getInstance().getAutoForVertriebler(vertrieblerDTO);
    }

    public List<AutoDTO> getAutosForSearch(String suchtext, String filter) throws SQLException {
        return AutoDAO.getInstance().getAutosForSearch(suchtext, filter);
    }

    public void deleteAuto(AutoDTO autoDTO) throws AutoException {
        boolean result = AutoDAO.getInstance().deleteAuto(autoDTO);
        if (result) {
            return;
        }
        throw new AutoException();
    }

    public void updateAuto(AutoDTO autoDTO) throws AutoException {
        boolean result = AutoDAO.getInstance().updateAuto(autoDTO);
        if (result) {
            return;
        }
        throw new AutoException();
    }

    public void createAuto(AutoDTO autoDTO) throws AutoException {
        UserDTO userDTO = ((MyUI) UI.getCurrent()).getUserDTO();
        boolean result = AutoDAO.getInstance().createAuto(autoDTO, userDTO);
        if (result) {
            return;
        }
        throw new AutoException();
    }
}
