package org.se2.gui.windows;

import com.vaadin.ui.*;
import org.se2.model.dao.ReservierungDAO;
import org.se2.model.objects.dto.AutoDTO;
import org.se2.model.objects.dto.UserDTO;
import org.se2.model.objects.dto.VertrieblerDTO;
import org.se2.process.control.AutoControl;
import org.se2.process.control.ReservierungControl;
import org.se2.process.exceptions.AutoException;
import org.se2.process.exceptions.ReservierungException;
import org.se2.services.util.Roles;

import java.sql.SQLException;
import java.util.List;

public class AutoWindow extends Window {
    private TextField marke;
    private TextField modell;
    private TextField baujahr;
    private TextArea beschreibung;

    public AutoWindow(AutoDTO auto, UserDTO userDTO) {
        super(auto.getMarke());
        center();

        //Name
        marke = new TextField("Marke");
        marke.setValue(auto.getMarke());
        marke.setReadOnly(true);

        //Modell
        modell = new TextField("Modell");
        modell.setValue(auto.getModell());
        modell.setReadOnly(true);

        //Baujahr
        baujahr = new TextField("Baujahr");
        baujahr.setValue(auto.getBaujahr());
        baujahr.setReadOnly(true);

        //Beschreibung
        beschreibung = new TextArea("Beschreibung");
        beschreibung.setValue(auto.getBeschreibung());
        beschreibung.setReadOnly(true);

        //OkButton
        Button okButton = new Button("Ok");
        okButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });

        //reservierenButton
        Button reservierenButton = new Button("Reservieren");
        ReservierungControl.getInstance().checkAllowed(auto, userDTO, reservierenButton);
        reservierenButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    ReservierungDAO.getInstance().createReservierung(auto, userDTO);

                } catch (Exception e){

                    Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);

                }
                UI.getCurrent().addWindow(new ConfirmationWindow("Ihre Reservierung war erfolgreich!"));
                close();
            }
        });

        //Horizontal
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(okButton);
        if ( userDTO.hasRole(Roles.KUNDE) ) {
            horizontalLayout.addComponent(reservierenButton);
        }

        //Vertikal
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout = this.buildVerticalLayout(verticalLayout, marke,modell, baujahr, beschreibung, horizontalLayout);
        setContent(verticalLayout);
    }

    public AutoWindow(AutoDTO Auto, Grid<AutoDTO> grid, VertrieblerDTO vertrieblerDTO) {
        super(Auto.getMarke());
        center();

        //Marke
        marke = new TextField("Marke");
        marke.setValue(Auto.getMarke());
        
        //Modell
        modell = new TextField("Modell");
        modell.setValue(Auto.getModell());

        //Baujahr
        baujahr = new TextField("Baujahr");
        baujahr.setValue(Auto.getBaujahr());

        //Beschreibung
        beschreibung = new TextArea("Beschreibung");
        beschreibung.setValue(Auto.getBeschreibung());

        //SaveButton
        Button saveButton = new Button("Speichern");
        saveButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Auto.setMarke(marke.getValue());
                Auto.setModell(modell.getValue());
                Auto.setBaujahr(baujahr.getValue());
                Auto.setBeschreibung(beschreibung.getValue());

                try {
                    AutoControl.getInstance().updateAuto(Auto);
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
        verticalLayout = this.buildVerticalLayout(verticalLayout, marke,modell, baujahr, beschreibung, horizontalLayout);
        setContent(verticalLayout);
    }
    public VerticalLayout buildVerticalLayout(VerticalLayout verticalLayout, TextField marke,TextField modell, TextField baujahr, TextArea beschreibung, HorizontalLayout horizontalLayout ){
        verticalLayout.addComponent(marke);
        verticalLayout.addComponent(modell);
        verticalLayout.addComponent(baujahr);
        verticalLayout.addComponent(beschreibung);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
        return verticalLayout;
    }
}
