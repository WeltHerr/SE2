package org.se2.gui.components;

import com.vaadin.event.MouseEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.se2.gui.ui.MyUI;
import org.se2.model.objects.dto.UserDTO;
import org.se2.process.control.LoginControl;
import org.se2.services.util.Roles;
import org.se2.services.util.Views;

public class  TopPanel extends HorizontalLayout {

    public TopPanel() {
        this.setSizeFull();

        //Logo links oben in der Ecke
        Panel logo = new Panel("CarLook Ltd.");
        logo.setSizeUndefined();
        logo.addClickListener(new MouseEvents.ClickListener() {

            @Override
            public void click(MouseEvents.ClickEvent clickEvent) {
                UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
            }
        });
        this.addComponent(logo);
        this.setComponentAlignment(logo, Alignment.TOP_LEFT);

        //Willkommenstext oben rechts
        HorizontalLayout hlayout = new HorizontalLayout();
        UserDTO userDTO = ( (MyUI) MyUI.getCurrent() ).getUserDTO();
        Label welcome = new Label("Willkommen bei CarLook Ltd.!");
        hlayout.addComponent(welcome);
        hlayout.setComponentAlignment(welcome, Alignment.MIDDLE_CENTER);

        //Menü rechts oben
        MenuBar bar = new MenuBar();
        MenuBar.MenuItem item1 = bar.addItem("Menu", VaadinIcons.MENU,null);

        if (userDTO != null) {

            //Vertriebler Menü
            if ( userDTO.hasRole(Roles.VERTRIEBLER) ) {
                item1.addItem("Meine Autos", VaadinIcons.FILE_TEXT_O, new MenuBar.Command() {
                    @Override
                    public void menuSelected(MenuBar.MenuItem menuItem) {
                        UI.getCurrent().getNavigator().navigateTo(Views.AUTO);
                    }
                });
            }

            //Kunde Menü
            if ( userDTO.hasRole(Roles.KUNDE) ) {
                item1.addItem("Meine Reservierungen", VaadinIcons.FILE_TEXT_O, new MenuBar.Command() {
                    @Override
                    public void menuSelected(MenuBar.MenuItem menuItem) {
                        UI.getCurrent().getNavigator().navigateTo(Views.RESERVIERUNG);
                    }
                });
            }

            item1.addItem("Logout", VaadinIcons.SIGN_OUT, new MenuBar.Command() {
                @Override
                public void menuSelected(MenuBar.MenuItem menuItem) {
                    LoginControl.getInstance().logoutUser();
                }
            });
        }

        //Einfügen
        hlayout.addComponent(bar);
        this.addComponent(hlayout);
        this.setComponentAlignment(hlayout, Alignment.BOTTOM_RIGHT);
    }

}
