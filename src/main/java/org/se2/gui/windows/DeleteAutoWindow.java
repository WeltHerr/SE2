package org.se2.gui.windows;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.se2.model.objects.dto.AutoDTO;
import org.se2.process.control.AutoControl;
import org.se2.process.exceptions.AutoException;
import org.se2.services.util.Views;

public class DeleteAutoWindow extends DeleteWindow{
    //Window zum Löschen von Autos

    public DeleteAutoWindow(AutoDTO AutoDTO) {
        this.setText("Sind Sie sicher, dass Sie die Auto löschen wollen? <br>" +
                "Dieser Vorgang ist unumkehrbar!");
        this.setDto(AutoDTO);
        this.setListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    AutoControl.getInstance().deleteAuto(AutoDTO);
                } catch (AutoException e) {
                    Notification.show("Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
                }
                UI.getCurrent().getNavigator().navigateTo(Views.AUTO);
                for (Window w : UI.getCurrent().getWindows()) {
                    w.close();
                }
            }
        });
    }
}

