package allocation;

import java.util.ArrayList;
import java.util.List;

import exception.AllocationException;

/**
 * klasse die den Controller benachrichtigt wenn in einem Thread eine Exception
 * geworfen wird
 * 
 * @author emil
 *
 */
public class ExceptionListener {
    /**
     * leerer konstruktor
     */
    public ExceptionListener() {
            //leer
    }

    /**
     * methode ruft controller auf eine allocation exception anzuzeigen
     * 
     * @param e
     *            die allocation exception die nach oben gereicht werden soll
     */
    public void notifyAllocationException(AllocationException e) {

        // TODO add code to notfy the controller about the exception and javadoc

    }

}
