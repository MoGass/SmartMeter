package Database;

public class DataType {
    private int id;
    private String zeitstempel;
    private int verbrauch;
/*
Eigener Datentyp für die MesstellenDaten
Sicherheitsaspekt:
Attribute werden bei erstellen definiert und sind danach nicht mehr veränderlich.
Dadurch ist die Integrität der Daten sichergestellt.
 */
    public DataType(int id, String zeitstempel, int verbrauch) {
        this.id = id;
        this.zeitstempel = zeitstempel;
        this.verbrauch = verbrauch;
    }
    public int getId() {
        return id;
    }
    public String getZeitstempel() {
        return zeitstempel;
    }
    public int getVerbrauch() {
        return verbrauch;
    }
}
