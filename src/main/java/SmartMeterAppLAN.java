import Database.*;
import UI.*;

public class SmartMeterAppLAN {

    public static void main(String[] args) {
        //Datenbank wird erzeugt und Dummydaten eingepflegt
        Database.getReady();

        //LoginUI wird erzeugt
        LoginUI loginUI = new LoginUI();

    }
}
