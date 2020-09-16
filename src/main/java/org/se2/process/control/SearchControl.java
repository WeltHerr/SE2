package org.se2.process.control;

import com.vaadin.ui.UI;
import org.se2.gui.ui.MyUI;
import org.se2.model.objects.dto.AutoDTO;
import org.se2.model.objects.dto.KundeDTO;
import org.se2.model.objects.dto.UserDTO;
import org.se2.model.objects.dto.VertrieblerDTO;
import org.se2.services.util.Roles;

import java.sql.SQLException;
import java.util.List;

public class SearchControl {
    private static SearchControl search = null;

    public static SearchControl getInstance() {
        if (search == null) {
            search = new SearchControl();
        }
        return search;
    }

    private SearchControl() {
    }

    public List<AutoDTO> getAutosForUser() throws SQLException {
        UserDTO userDTO = ( (MyUI)UI.getCurrent() ).getUserDTO();
        if (userDTO.hasRole(Roles.KUNDE)) {
            KundeDTO kundeDTO = new KundeDTO(userDTO);
        }
        VertrieblerDTO vertrieblerDTO = new VertrieblerDTO(userDTO);
        return AutoControl.getInstance().getAnzeigenForVertriebler(vertrieblerDTO);
    }

    public List<AutoDTO> getAutosForSearch(String suchtext, String filter) throws SQLException {
        if (filter == null) {
            filter = "marke";
        }
        return AutoControl.getInstance().getAutosForSearch(suchtext, filter);
    }
}
