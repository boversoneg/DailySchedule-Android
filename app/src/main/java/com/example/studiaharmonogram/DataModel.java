package com.example.studiaharmonogram;

public class DataModel {
    String przedmiot;
    String sala;
    String godziny;

    public DataModel(String przedmiot, String sala, String godziny) {
        this.przedmiot = przedmiot;
        this.sala = sala;
        this.godziny = godziny;
    }

    public String getPrzedmiot() {
        return przedmiot;
    }

    public String getSala() {
        return sala;
    }

    public String getGodziny() {
        return godziny;
    }
}
