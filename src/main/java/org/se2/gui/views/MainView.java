package org.se2.gui.views;

import com.vaadin.data.HasValue;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import org.se2.gui.components.TopPanel;
import org.se2.gui.ui.MyUI;
import org.se2.gui.windows.AutoWindow;
import org.se2.gui.windows.CreateAutoWindow;
import org.se2.model.objects.dto.AutoDTO;
import org.se2.model.objects.dto.UserDTO;
import org.se2.model.objects.dto.VertrieblerDTO;
import org.se2.process.control.SearchControl;
import org.se2.services.util.BuildGrid;
import org.se2.services.util.Roles;
import org.se2.services.util.Views;

import java.sql.SQLException;
import java.util.List;

public class MainView extends VerticalLayout implements View {

    private AutoDTO selektiert = null;
    private List<AutoDTO> list;
    private String suchtext;


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        UserDTO userDTO = ( (MyUI)UI.getCurrent() ).getUserDTO();
        if (userDTO == null) {
            UI.getCurrent().getNavigator().navigateTo(Views.LOGIN);
        }

        this.setUp(userDTO);
    }

    private void setUp(UserDTO userDTO) {
        //Top Layer
        this.addComponent( new TopPanel() );
        Label line = new Label("<hr>", ContentMode.HTML);
        this.addComponent(line);
        line.setSizeFull();

        //Tabelle
        final Grid<AutoDTO> grid = new Grid<>("Ihre Treffer");
        grid.setSizeFull();
        grid.setHeightMode(HeightMode.UNDEFINED);
        BuildGrid.buildGrid(grid);
        SingleSelect<AutoDTO> selection = grid.asSingleSelect();



        //DetailButton
        Button detailButton = new Button("Details", VaadinIcons.ENTER);
        detailButton.setEnabled(false);
        detailButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (selection.getValue() == null) {
                    detailButton.setEnabled(false);
                }
                selektiert = selection.getValue();
                UI.getCurrent().addWindow( new AutoWindow(selektiert, userDTO) );
            }
        });

        //SearchButton
        Button searchButton = new Button("Suchen", VaadinIcons.SEARCH);
        searchButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        //Combobox
        final ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPlaceholder("Filtern nach");
        comboBox.setItems("Marke", "Modell", "Baujahr");

        //SelectionListener Tabelle
        grid.addSelectionListener(new SelectionListener<AutoDTO>() {
            @Override
            public void selectionChange(SelectionEvent<AutoDTO> event) {
                if (selection.getValue() == null) {
                    detailButton.setEnabled(false);
                } else {
                    System.out.println("Zeile selektiert: " + selection.getValue());
                    selektiert = selection.getValue();
                    detailButton.setEnabled(true);
                }
            }
        });

        //Suchfeld
        final TextField search = new TextField();
        search.setWidth("300");
        search.addValueChangeListener(new HasValue.ValueChangeListener<String>() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent<String> valueChangeEvent) {
                search(search, comboBox, grid, detailButton);
            }
        });

        //SearchButton Config
        searchButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                suchtext = search.getValue().toUpperCase();
                if(suchtext.equals("")) {
                    try {
                        list = SearchControl.getInstance().getAutosForSearch(suchtext, comboBox.getValue());
                    } catch (SQLException e) {
                        Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
                    }
                    grid.setItems(list);
                    addComponent(grid);
                    addComponent(detailButton);
                    setComponentAlignment(detailButton, Alignment.MIDDLE_CENTER);
                } else {
                    search(search, comboBox, grid, detailButton);
                }
            }
        });



        //Horizontal Layout
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(comboBox);
        horizontalLayout.addComponent(search);
        horizontalLayout.addComponent(searchButton);
        horizontalLayout.setComponentAlignment(search, Alignment.MIDDLE_CENTER);
        horizontalLayout.setComponentAlignment(searchButton, Alignment.MIDDLE_CENTER);
        //Vertriebler Menü
        if ( userDTO.hasRole(Roles.VERTRIEBLER) ) {

            VertrieblerDTO vertrieblerDTO = new VertrieblerDTO(userDTO);

            //InserierenButton
            Button inserierenButton = new Button("Auto inserieren");
            inserierenButton.setEnabled(true);

            //ShowButton Config Bearbeiten
            inserierenButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    CreateAutoWindow window = new CreateAutoWindow(new AutoDTO(), grid, vertrieblerDTO);
                    UI.getCurrent().addWindow(window);
                }
            });

            horizontalLayout.addComponent(inserierenButton);

        }

        //Darstellen
        this.addComponent(horizontalLayout);
        this.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
    }

    private void search(TextField search, ComboBox<String> comboBox, Grid<AutoDTO> grid, Button detailButton) {
        if (search.getValue().length() > 1) {
            suchtext = search.getValue().toUpperCase();
            String filter = comboBox.getValue();
            try {
                list = SearchControl.getInstance().getAutosForSearch(suchtext, filter);
            } catch (SQLException e) {
                Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!");
            }
            grid.setItems();
            grid.setItems(list);
            addComponent(grid);
            addComponent(detailButton);
            setComponentAlignment(detailButton, Alignment.MIDDLE_CENTER);
        } else {
            removeComponent(grid);
            removeComponent(detailButton);
        }
    }


}
