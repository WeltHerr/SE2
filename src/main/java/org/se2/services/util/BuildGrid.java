package org.se2.services.util;

import com.vaadin.ui.Grid;
import org.se2.model.objects.dto.AutoDTO;

public class BuildGrid {
    public static void buildGrid(Grid<AutoDTO> grid) {
        grid.removeAllColumns();
        grid.addColumn(AutoDTO::getMarke).setCaption("Marke");
        grid.addColumn(AutoDTO::getModell).setCaption("Modell");
        grid.addColumn(AutoDTO::getBaujahr).setCaption("Baujahr");
        grid.addColumn(AutoDTO::getBeschreibung).setCaption("Beschreibung");
    }
}
