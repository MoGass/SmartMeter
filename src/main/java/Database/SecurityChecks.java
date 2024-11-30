package Database;
/*
Prinzip der Sicheren Softwareentwicklung:
Check der Passwörter auf Einhalten eines vorher definierten Standards.
Dies stellt die Grundlagen eines Zugangsmanagementsystem da.
 */
public class SecurityChecks {
    private static int passwortLength = 8;
    private static boolean capsReq = true;
    private static boolean specialChars = true;
/*
Hiermit kann theoretisch die Sicherheitsstandards angepasst werden.
Für die Anwendung wurde die Attribute schon inialisiert
 */
    public static void setSecReq(int passwortLength, boolean capsReq, boolean specialChars) {
        SecurityChecks.passwortLength = passwortLength;
        SecurityChecks.capsReq = capsReq;
        SecurityChecks.specialChars = specialChars;

    }
    /*
    Überprüfung auf die Verwendung von Großbuchstaben
     */
    private static boolean checkForCaps(String password){
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }

        return false;

    }
/*
Überprüfung auf die Verwendung von Sonderzeichen
 */
    private static boolean checkForSpecialChars(String password){
        for (char c : password.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return true;
            }
        }
        return false;
    }
    /*
    Überprüfung auf alle Standards
     */
    public static boolean checkPassword(String password) {
        boolean test = password.length() >= passwortLength;
        System.out.println("Checking password: " + password + " " + test);
        System.out.println("Checking password caps: " + checkForCaps(password));
        System.out.println("Checking password special: " + checkForSpecialChars(password));
        return password.length() >= passwortLength && checkForCaps(password) && checkForSpecialChars(password);
    }
}
