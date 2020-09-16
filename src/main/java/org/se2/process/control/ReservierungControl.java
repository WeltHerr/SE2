package org.se2.process.control;

import com.vaadin.ui.Button;
import org.se2.model.dao.ReservierungDAO;
import org.se2.model.objects.dto.AutoDTO;
import org.se2.model.objects.dto.KundeDTO;
import org.se2.model.objects.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

public class ReservierungControl {
    private static ReservierungControl reservierungControl = null;

    private ReservierungControl(){
    }

    public static ReservierungControl getInstance(){
        if(reservierungControl == null){
            reservierungControl = new ReservierungControl();
        }
        return reservierungControl;
    }

    public void deleteReservierung(AutoDTO autoDTO, KundeDTO kundeDTO) throws SQLException {
        ReservierungDAO.getInstance().deleteReservierung(autoDTO, kundeDTO);
    }

    public void checkAllowed(AutoDTO auto, UserDTO userDTO, Button reservierenButton) {
        ReservierungDAO.getInstance().checkAllowed(auto,userDTO,reservierenButton);
    }


    public List<AutoDTO> getReservierungenForKunde(KundeDTO kundeDTO) throws SQLException {
        return ReservierungDAO.getInstance().getReservierungenForKunde(kundeDTO);
    }
}
