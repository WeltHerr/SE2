package org.se2.gui.windows;

import com.vaadin.ui.*;

public class ConfirmationWindow extends Window {
    //Window zur Anmeldebestätigung
    public ConfirmationWindow(String text) {
        super("Bestätigung:");
        center();

        VerticalLayout content = new VerticalLayout();
        content.addComponent(new Label( text ));
        content.setMargin(true);
        setContent(content);

        Button okButton = new Button("Ok");
        okButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });
        okButton.setId("ok");

        content.addComponent(okButton);
        content.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
    }
}
