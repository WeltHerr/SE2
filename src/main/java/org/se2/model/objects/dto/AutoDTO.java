package org.se2.model.objects.dto;

public class AutoDTO extends AbstractDTO {
    private int id;
    private String marke;
    private String modell;
    private String baujahr;
    private String beschreibung;

    public AutoDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarke() {
        return marke;
    }

    public void setMarke(String marke) {
        this.marke = marke;
    }

    public String getModell() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getBaujahr() {
        return baujahr;
    }

    public void setBaujahr(String baujahr) {
        this.baujahr = baujahr;
    }
}
