package data;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;

@MappedSuperclass
public abstract class ElipseModel extends Model {

    protected static final String ID = "id";

    /**
     * Eindeutige ID des Objektes.
     */
    @Id
    @Column(name = ID)
    private int                   id;

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
     */

    public void setId(int id) {
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

    public synchronized void doTransaction(Transaction transaction) {
        Ebean.beginTransaction();
        transaction.transact();
        this.save();
        Ebean.commitTransaction();
        Ebean.endTransaction();
    }

}
