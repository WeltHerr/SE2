package org.se2.process.control;

import com.vaadin.ui.Button;
import org.se2.model.dao.AutoDAO;
import org.se2.model.dao.ReservierungDAO;
import org.se2.model.objects.dto.AutoDTO;
import org.se2.model.objects.dto.KundeDTO;
import org.se2.model.objects.dto.ReservierungDTO;
import org.se2.model.objects.dto.UserDTO;
import org.se2.process.exceptions.DatabaseException;
import org.se2.process.exceptions.ReservierungException;

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


    public void deleteReservierung(AutoDTO autoDTO, KundeDTO kundeDTO) throws ReservierungException, SQLException {
        ReservierungDAO.getInstance().deleteReservierung(autoDTO, kundeDTO);
    }

    public void checkAllowed(AutoDTO auto, UserDTO userDTO, Button reservierenButton) {
        ReservierungDAO.getInstance().checkAllowed(auto,userDTO,reservierenButton);
    }

    public void reserveCar(AutoDTO auto, UserDTO userDTO) throws ReservierungException, SQLException {
        boolean result = ReservierungDAO.getInstance().createReservierung(auto, userDTO);
        if(result){
            return;
        }
        throw new ReservierungException();
    }

    public List<AutoDTO> getReservierungenForKunde(KundeDTO kundeDTO) throws SQLException {
        return ReservierungDAO.getInstance().getReservierungenForKunde(kundeDTO);
    }
}
