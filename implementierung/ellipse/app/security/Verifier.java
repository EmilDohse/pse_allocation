// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package security;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

import data.Student;

/************************************************************/
/**
 * Der Verifier ist für die Verifikation von E-Mail-Adressen der Studenten
 * zuständig und managt das Erstellen von Verifikations-Codes und den Prozess
 * des Verifizierens an sich.
 * 
 * Die Theorie des Verifikations-Prozesses: Der Student bekommt an die
 * E-Mail-Adresse, welche verifiziert werden soll, einen Link zugeschickt, der
 * den Code beinhaltet, der vorher mit dem Verifier unter Angabe des Studenten,
 * wessen E-Mail-Adresse verifiziert werden soll, generiert wurde. Duch Klicken
 * diese Linkes, wird die E-Mail-Adresse verifiziert, wenn der Verifier
 * feststellt, dass der Code für die E-Mail-Adresse generiert wurde. Dabei läuft
 * ein Code nach 24h ab und über diesen Code lässt sich keine E-Mail mehr
 * verifizieren.
 * 
 * ACHTUNG: Die Codes werden in keiner Datenbank oder so gespeichert, ein
 * Programm-Neustart führt zu Verlust der Codes.
 */
public class Verifier {

    private final static Verifier     instance    = new Verifier();

    /**
     * Tag hat 24 Stunden.
     */
    private static final int          DAY_IN_H    = 24;

    /**
     * Length of the verification code.
     */
    private static final int          CODE_LENGTH = 20;

    private HashMap<Student, String>  savedCodes;
    /**
     * Stores the time, when the last code for an student was generated.
     */
    private HashMap<Student, Instant> timestamps;

    private Verifier() {
        savedCodes = new HashMap<>();
        timestamps = new HashMap<>();
    }

    /**
     * Diese Methode verifiziert eine E-Mail-Adresse. Dazu wird geschaut, ob der
     * Code auch wirklich dem Studenten zur Verifikation geschickt wurde.
     * ACHTUNG: Diese Methode funktioniert nur einmalig, falls der Code korrekt
     * war und true zurückgegeben wird. Daraufhin ist für den Verifikator der
     * Prozess für diesen Studenten abgeschlossen und alle seine
     * Verifikatordaten zerstört. ACHTUNG: Ändert auch bei Erfolg nicht den
     * Zustand des Studenten.
     * 
     * @param student
     *            Der Student, dessen E-Mail-Adresse verifiziert werden soll.
     * @param code
     *            Der Code, mit dem die E-Mail-Adresse verifiziert werden soll.
     * 
     * @return true wenn die Verifikation positiv war, false sonst.
     */
    public boolean verify(Student student, String code) {
        if (code == null) {
            return false;
        } else {
            if (savedCodes.get(student).equals(code)) {
                Instant codeGenInstant = timestamps.get(student);
                Instant currentInstant = Instant.now();
                Duration diff = Duration.between(codeGenInstant,
                        currentInstant);
                if (diff.toHours() < DAY_IN_H) {
                    savedCodes.remove(student);
                    timestamps.remove(student);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Diese Methode generiert einen Code, der dazu verwendet werden kann, um
     * die E-Mail-Adresse eines Studenten zu verifizieren. ACHTUNG: Falls diese
     * Methode zweifach mit einem Studenten aufgerufen wird, so wird der zuerst
     * generierte Code überschrieben. Falls der erste Code schon versendet
     * wurde, wird dieser invalide.
     * 
     * @param student
     *            Der Student, dessen E-Mail-Adresse mit dem zurückgegebenen
     *            Code verifiziert werden soll.
     * @return der Code, der zur Verifikation der E-Mail-Adresse benötigt wird.
     */
    public String getVerificationCode(Student student) {
        StringBuilder builder = new StringBuilder();
        SecureRandom random = new SecureRandom(); // Verwendet SecureRandom um
                                                  // byte Array zu füllen,
                                                  // welches in Int umgewandelt
                                                  // wird.
        for (int i = 0; i < CODE_LENGTH; i++) {
            byte bytes[] = new byte[4];
            random.nextBytes(bytes);
            builder.append(Math.abs(ByteBuffer.wrap(bytes).getInt()));
        }
        String result = builder.toString();
        // Speichert code und die aktuelle Zeit
        savedCodes.put(student, result);
        timestamps.put(student, Instant.now());
        return result;
    }

    /**
     * Diese Methode ist Teil des Singeltons. Sie gibt die einzige Instanz des
     * Verifiers zurück.
     * 
     * @return die einzige Verifier-Instanz.
     */
    public static Verifier getInstance() {
        return instance;
    }
}
