package org.se2.gui.views;

import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import org.se2.gui.components.TopPanel;
import org.se2.model.objects.dto.UserDTO;
import org.se2.process.control.RegistrationControl;
import org.se2.process.exceptions.*;
import org.se2.services.util.Views;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class RegistrationView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.setUp();
    }

    private void setUp() {
        this.addComponent( new TopPanel() );
        Label line = new Label("<hr>", ContentMode.HTML);
        this.addComponent(line);
        line.setSizeFull();

        //Eingabefelder

        //Name
        final Binder<UserDTO> nameBinder = new Binder<>();
        final TextField fieldName = new TextField("Name:");
        fieldName.focus();
        fieldName.setPlaceholder("Mustermann");
        fieldName.setRequiredIndicatorVisible(true);
        nameBinder.forField(fieldName)
                .withValidator(str -> Pattern.matches("[a-zA-Z]+",str), "Bitte geben Sie einen gülgigen Namen ein!")
                .asRequired("Bitte geben Sie einen gültigen Namen ein!")
                .bind(UserDTO::getName, UserDTO::setName);

        //Email
        final Binder<UserDTO> emailBinder = new Binder<>();
        final TextField fieldEmail = new TextField("Email:");
        fieldEmail.setPlaceholder("Ihre Email");
        fieldEmail.setRequiredIndicatorVisible(true);
        emailBinder.forField(fieldEmail)
                .withValidator(new EmailValidator("Biite geben Sie eine korrekte Emailadresse ein!"))
                .bind(UserDTO::getEmail, UserDTO::setEmail);

        //Passwort setzen und Counter Label darunter
        final Binder<UserDTO> password1Binder = new Binder<>();
        final PasswordField fieldPassword1 = new PasswordField("Passwort:");
        fieldPassword1.setPlaceholder("Passwort");
        fieldPassword1.setMaxLength(20);
        fieldPassword1.setRequiredIndicatorVisible(true);
        password1Binder.forField(fieldPassword1)
                .withValidator(str -> str.length() > 2, "Ihr Passwort muss mindestens 3 Zeichen enthalten!")
                .withValidator(str -> str.length() < 21, "Ihr Passwort darf nicht mehr als 20 Zeichen enthalten!")
                .asRequired("Bitte gegen Sie ein Passwort ein!")
                .bind(UserDTO::getPassword, UserDTO::setPassword);
        Label counter1 = new Label();
        counter1.setValue(fieldPassword1.getValue().length() + " of " + fieldPassword1.getMaxLength());
        fieldPassword1.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                counter1.setValue(fieldPassword1.getValue().length() + " of " + fieldPassword1.getMaxLength());

            }
        });
        fieldPassword1.setValueChangeMode(ValueChangeMode.EAGER);

        //Passwort wiederholen
        final Binder<UserDTO> password2Binder = new Binder<>();
        final PasswordField fieldPassword2 = new PasswordField("Passwort wiederholen:");
        fieldPassword2.setPlaceholder("Passwort");
        fieldPassword2.setMaxLength(20);
        fieldPassword2.setRequiredIndicatorVisible(true);
        password2Binder.forField(fieldPassword2)
                .asRequired("Bitte wiederholen Sie Ihr Passwort!")
                .bind(UserDTO::getPassword, UserDTO::setPassword);
        Label counter2 = new Label();
        counter2.setValue(fieldPassword2.getValue().length() + " of " + fieldPassword2.getMaxLength());
        fieldPassword2.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                counter2.setValue(fieldPassword2.getValue().length() + " of " + fieldPassword2.getMaxLength());

            }
        });
        fieldPassword1.setValueChangeMode(ValueChangeMode.EAGER);

        //Checkbox
        final Binder<UserDTO> checkboxBinder = new Binder<>();
        RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>("Registrieren als:");
        radioButtonGroup.setItems("Kunde", "Vertriebler");
        radioButtonGroup.setRequiredIndicatorVisible(true);
        radioButtonGroup.isSelected("Kunde");
        checkboxBinder.forField(radioButtonGroup)
                .asRequired("Bitte wählen Sie!")
                .bind(UserDTO::getPassword, UserDTO::setPassword);

        //Register Button
        Button registerButton = new Button("Registrieren");
        registerButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    nameBinder.validate();
                    emailBinder.validate();
                    password1Binder.validate();
                    password2Binder.validate();
                    checkboxBinder.validate();
                    String email = fieldEmail.getValue();
                    String password1 = fieldPassword1.getValue();
                    String password2 = fieldPassword2.getValue();
                    String regAs = radioButtonGroup.getValue();
                    String name = fieldName.getValue();
                    RegistrationControl.getInstance().checkValid( email, emailBinder.isValid(), password1, password2 , password1Binder.isValid(), password2Binder.isValid(), checkboxBinder.isValid(), regAs);
                    RegistrationControl.getInstance().registerUser( email, password1, name, regAs );
                } catch (NoEqualPasswordException e) {
                    Notification.show("Passwort-Fehler!", e.getReason(), Notification.Type.WARNING_MESSAGE);
                } catch (DatabaseException e) {
                    Notification.show("DB-Fehler!", e.getReason(), Notification.Type.ERROR_MESSAGE);
                } catch (EmailInUseException e) {
                    Notification.show("Email-Fehler!", e.getReason(), Notification.Type.ERROR_MESSAGE);
                } catch (NoCarlookMailException e) {
                    Notification.show("Email-Fehler!", e.getReason(), Notification.Type.ERROR_MESSAGE);
                } catch (EmptyFieldException e) {
                    Notification.show("Es sind ein oder mehrere Eingabefehler aufgetreten!", e.getReason(), Notification.Type.ERROR_MESSAGE);
                } catch (SQLException e) {
                    Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
                }
            }
        });

        //LoginButton
        Button loginButton = new Button("Zum Login");
        loginButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                UI.getCurrent().getNavigator().navigateTo(Views.LOGIN);
            }
        });

        //Horizontal
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(registerButton);
        horizontalLayout.addComponent(loginButton);

        //Vertical Layout
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(fieldName);
        verticalLayout.addComponent(fieldEmail);
        verticalLayout.addComponent(fieldPassword1);
        verticalLayout.addComponent(counter1);
        verticalLayout.addComponent(fieldPassword2);
        verticalLayout.addComponent(counter2);
        verticalLayout.addComponent(radioButtonGroup);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

        //Panel
        Panel panel = new Panel( "Bitte geben Sie ihre Daten ein:");
        panel.setContent(verticalLayout);
        panel.setSizeUndefined();

        //Einfügen
        this.addComponent(panel);
        this.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
    }

}