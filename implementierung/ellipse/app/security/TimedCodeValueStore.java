package security;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

import data.Student;

/**
 * Diese Klasse speichert ein Objekt zu einem generierten Code und gibt das
 * Objekt unter Vorlage des Codes wieder heraus. Der Code wird jedoch nach einer
 * festgelegten Zeit ungültig.
 * 
 * @author daniel
 *
 * @param <V>
 *            das zu speichernde Objekt
 */
public class TimedCodeValueStore<V> {

    private final int                 validCodeDuration;

    /**
     * Length of the verification code.
     */
    private final int                 codeLength;

    private final HashMap<String, V>  savedCodes;
    /**
     * Stores the time, when the last code for an student was generated.
     */
    private final HashMap<V, Instant> timestamps;

    public TimedCodeValueStore(int validCodeDuration, int codeLength) {
        this.validCodeDuration = validCodeDuration;
        this.codeLength = codeLength;
        this.savedCodes = new HashMap<>();
        this.timestamps = new HashMap<>();
    }

    /**
     * Diese Methode gibt das Objekt zum zugehörigen Code zurück. Falls der Code
     * invalide ist, wird null zurückgegeben.
     * 
     * @param code
     *            der Code
     * @return das Objekt
     */
    public V pop(String code) {
        if (code == null) {
            return null;
        } else {
            V object = savedCodes.get(code);
            if (object != null) {
                Instant codeGenInstant = timestamps.get(object);
                Instant currentInstant = Instant.now();
                Duration diff = Duration.between(codeGenInstant,
                        currentInstant);
                if (diff.toHours() < validCodeDuration) {
                    savedCodes.remove(code);
                    timestamps.remove(object);
                    return object;
                }
            }
        }
        return null;
    }

    /**
     * Diese Methode speichert ein Objekt und gibt dazugehörigen Code zurück,
     * mit dem das Objekt wieder geholt werden kann.
     * 
     * @param object
     *            das zu speichernde Objekt
     * @return den Code
     */
    public String store(V object) {
        StringBuilder builder = new StringBuilder();
        SecureRandom random = new SecureRandom(); // Verwendet SecureRandom um
                                                  // byte Array zu füllen,
                                                  // welches in Int umgewandelt
                                                  // wird.
        while (builder.length() < codeLength) {
            byte bytes[] = new byte[4];
            random.nextBytes(bytes);
            builder.append(Math.abs(ByteBuffer.wrap(bytes).getInt()));
        }
        builder.setLength(codeLength);
        String result = builder.toString();
        // Speichert code und die aktuelle Zeit
        savedCodes.put(result, object);
        timestamps.put(object, Instant.now());
        return result;
    }
}
