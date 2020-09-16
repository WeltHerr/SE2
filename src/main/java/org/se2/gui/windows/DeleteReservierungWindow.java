package org.se2.gui.windows;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.se2.model.objects.dto.AutoDTO;
import org.se2.model.objects.dto.KundeDTO;
import org.se2.process.control.ReservierungControl;
import org.se2.process.exceptions.ReservierungException;
import org.se2.services.util.Views;

import java.sql.SQLException;

public class DeleteReservierungWindow extends DeleteWindow {

    public DeleteReservierungWindow(AutoDTO autoDTO, KundeDTO kundeDTO) {
        this.setText("Sind Sie sicher, dass Sie Ihre Reservierung auf diese Auto löschen wollen? <br>" +
                "Dieser Vorgang ist unumkehrbar!");
        this.setListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    ReservierungControl.getInstance().deleteReservierung(autoDTO, kundeDTO);
                } catch (ReservierungException e ) {
                    Notification.show("DB-Fehler", "Löschen war nicht erfolgreich!", Notification.Type.ERROR_MESSAGE);
                } catch (SQLException e2){
                    Notification.show("DB-Fehler", "Löschen war nicht erfolgreich!", Notification.Type.ERROR_MESSAGE);
                }
                Notification.show("Reservierung gelöscht!", Notification.Type.HUMANIZED_MESSAGE);
                UI.getCurrent().getNavigator().navigateTo(Views.RESERVIERUNG);
                for (Window w : UI.getCurrent().getWindows()) {
                    w.close();
                }
            }
        });
    }

}
