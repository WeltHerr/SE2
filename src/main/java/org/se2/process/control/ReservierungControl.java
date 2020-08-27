package org.se2.process.control;

import com.vaadin.ui.Button;
import org.se2.model.objects.dto.AutoDTO;
import org.se2.model.objects.dto.KundeDTO;
import org.se2.model.objects.dto.ReservierungDTO;
import org.se2.model.objects.dto.UserDTO;
import org.se2.process.exceptions.DatabaseException;
import org.se2.process.exceptions.ReservierungException;

import java.sql.SQLException;

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

    public ReservierungDTO getReservierungForAuto(AutoDTO selektiert, KundeDTO kundeDTO) throws SQLException, DatabaseException {
        return null;
    }

    public void deleteReservierung(ReservierungDTO reservierungDTO) throws ReservierungException {
    }

    public void checkAllowed(AutoDTO auto, UserDTO userDTO, Button bewerbenButton) {
    }
}
