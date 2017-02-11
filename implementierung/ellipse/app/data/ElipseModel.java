package data;

import java.util.List;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;

import exception.DataException;

@MappedSuperclass
public abstract class ElipseModel extends Model {

    public static final String LIST_EMPTY_ERROR        = "general.error.noEmptyList";
    public static final String STRING_EMPTY_ERROR      = "general.error.noEmptyString";
    public static final String IS_NULL_ERROR           = "general.error.isNull";
    public static final int    MINIMAL_PASSWORD_LENGTH = 6;
    public static final String MINIMAL_PASSWORD_ERROR  = "general.error.minimalPasswordLegth";
    /**
     * Eindeutige ID des Objektes.
     */
    @Id
    private int                id;

    /**
     * Getter für die eindeutige ID des Objektes.
     * 
     * @return Die eindeutige ID des Objektes.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter für die eindeutige ID des Objektes.
     * 
     * @param id
     *            Die neue eindeutige ID des Objektes.
     * @throws DataException
     *             wenn die id negativ ist
     */
    public void setId(int id) throws DataException {
        if (id < 0 || id == -1) {
            throw new DataException("genaeral.error.negativNumber");
        }
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass().equals(this.getClass())) {
            return this.id == ((ElipseModel) obj).getId();
        } else {
            return false;
        }
    }

    /**
     * Gibt Liste aller Instanzen einer Unterklasse von ElipseModel zurück, die
     * in der Datenbank sind.
     * 
     * @param type
     *            Die konkrete Klasse der Instanzen, die geladen werden sollen.
     * @return Liste aller Instanzen der Klasse "type" in der Datenbank.
     */
    protected static <T extends ElipseModel> List<T> getAll(Class<T> type) {
        return Ebean.find(type).findList();
    }

    /**
     * Gibt Liste aller Instanzen einer Unterklasse von ElipseModel zurück, die
     * in der Datenbank sind.
     * 
     * @param type
     *            Die konkrete Klasse der Instanzen, die geladen werden sollen.
     * @return Liste aller Instanzen der Klasse "type" in der Datenbank.
     */
    public static <T extends ElipseModel> T getById(Class<T> type, int id) {
        return Ebean.find(type, id);
    }

    public void doTransaction(Transaction transaction) {
        // Ebean.beginTransaction();

        try {
            transaction.transact();
        } catch (DataException e) {
            // TODO Nicht hier catchen, sondern in Controllern behandeln.
        }
        this.save();
        // Ebean.endTransaction();
    }

}
