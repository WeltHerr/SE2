package org.se2.gui.windows;

import com.vaadin.ui.*;
import org.se2.model.objects.dto.AutoDTO;
import org.se2.model.objects.dto.VertrieblerDTO;
import org.se2.process.control.AutoControl;
import org.se2.process.exceptions.AutoException;

import java.sql.SQLException;
import java.util.List;

public class CreateAutoWindow extends Window {

    public CreateAutoWindow(AutoDTO auto, Grid<AutoDTO> grid, VertrieblerDTO vertrieblerDTO) {
        super("Ihr Auto");
        center();

        //Marke
        TextField marke = new TextField("Marke");
        marke.setValue(auto.getMarke());

        //Modell
        TextField modell = new TextField("Modell");
        modell.setValue(auto.getModell());

        //Baujahr
        TextField baujahr = new TextField("Baujahr");
        baujahr.setValue(auto.getBaujahr());

        //Beschreibung
        TextArea beschreibung = new TextArea("Beschreibung");
        beschreibung.setValue(auto.getBeschreibung());

        //saveButton Config
        Button saveButton = new Button("Speichern");
        saveButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                auto.setMarke(marke.getValue().toUpperCase());
                auto.setModell(modell.getValue().toUpperCase());
                auto.setBaujahr(baujahr.getValue());
                auto.setBeschreibung(beschreibung.getValue());

                try {
                    AutoControl.getInstance().createAuto(auto);
                } catch (AutoException e) {
                    Notification.show("Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
                }
                UI.getCurrent().addWindow(new ConfirmationWindow("Auto erfolgreich gespeichert"));
                List<AutoDTO> list = null;
                try {
                    list = AutoControl.getInstance().getAnzeigenForVertriebler(vertrieblerDTO);
                } catch (SQLException e) {
                    Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
                }
                grid.setItems();
                grid.setItems(list);
                close();
            }
        });

        //abortButton Config
        Button abortButton = new Button("Abbrechen");
        abortButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });

        //Horizontal
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(saveButton);
        horizontalLayout.addComponent(abortButton);

        //Vertikal
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(marke);
        verticalLayout.addComponent(baujahr);
        verticalLayout.addComponent(beschreibung);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

        setContent(verticalLayout);
    }
}
