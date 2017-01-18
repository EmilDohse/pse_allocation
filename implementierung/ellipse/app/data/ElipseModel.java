package data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.avaje.ebean.Model;

@MappedSuperclass
public class ElipseModel extends Model {

    /**
     * Eindeutige ID des Objektes.
     */
    @Id
    private int id;

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
        if (obj.getClass().equals(this.getClass())) {
            return this.id == ((ElipseModel) obj).getId();
        } else {
            return false;
        }
    }

}
