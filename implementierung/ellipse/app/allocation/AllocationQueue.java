// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package allocation;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/************************************************************/
/**
 * Die AllocationQueue dient dazu, die Berechnung von Einteilungen als
 * FIFO-Warteschlange zu realisieren. Wenn eine berechnung fertig ist wird die
 * nächste Berechnung angestoßen. Die Queue ist als Singelton implementiert.
 */
public class AllocationQueue {

	private static final int QUEUE_SIZE = 10;
	private ExecutorService executer;
	/**
	 * Calculator ist der Thread der die berechnung anstößt er verwendet das
	 * Runnable runnable
	 */
	private Thread calculator;
	private Runnable runnable;
	/**
	 * Die intern verwendete queue.
	 */
	private Queue<allocation.Configuration> configurationQueue;
	/**
	 * Der Singelton der Allocation queue.
	 */
	private static AllocationQueue instance;
	/**
	 * Der Einteilungsberechner, der zur Berechnung verwendet wird.
	 */
	private AbstractAllocator allocator;
	/**
	 * Die Konfiguration, die aktuell zur Berechnung verwendet wird.
	 */
	private allocation.Configuration currentlyCalculatedConfiguration;

	/**
	 * Privater Konstruktor, der zur Instanziierung des Singletons verwendet
	 * wird.
	 */
	private AllocationQueue() {
		this.configurationQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
		setAllocator(new GurobiAllocator());

		runnable = new Runnable() {

			public void run() {
				while (true) {
					synchronized (this) { // syncronized da ansonsten probleme
											// mit dem cancel() kommen könnten
						while (configurationQueue.isEmpty()) {
							try {
								wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						currentlyCalculatedConfiguration = configurationQueue.poll();
					}
					allocator.calculate(currentlyCalculatedConfiguration);
					currentlyCalculatedConfiguration = null;
				}
			}

		};
		calculate(); // hier wird der thread gestartet der überpüft ob die liste
						// leer ist und sie gegebenenfalls abarbeitet
	}

	/**
	 * Gibt die eine existierende Instanz der AllocationQueue (Singleton)
	 * zurück. *
	 * 
	 * @return Die Instanz der AllocationQueue.
	 */
	public static AllocationQueue getInstance() {
		if (instance == null) { // ganz normaler Singelton
			instance = new AllocationQueue();
		}
		return instance;
	}

	/**
	 * Fügt der Berechnungsqueue eine Konfiguration hinzu, die zur Berechnung
	 * verwendet werden soll. Es können maximal 10 Berechnungen in die Queue
	 * aufgenommen werden.
	 * 
	 * @param configuration
	 *            Die Konfiguration, die zur Berechnungsqueue hinzugefügt wird.
	 */
	public void addToQueue(allocation.Configuration configuration) {
		configurationQueue.add(configuration);
		notifyAll(); // der calculator thread wird geweckt
	}

	/**
	 * Nimmt eine Konfiguration aus der Berechnungsqueue heraus. Falls diese
	 * Konfiguration bereits berechnet wird, wird die Berechnung abgebrochen.
	 * 
	 * @param configuration
	 *            Die Konfiguration, die entfernt werden soll.
	 */
	public void cancelAllocation(allocation.Configuration configuration) {
		synchronized (this) {
			if (configuration == currentlyCalculatedConfiguration) {
				allocator.cancel();
			} else {
				configurationQueue.remove(configuration);
			}
		}

	}

	/**
	 * Gibt die Queue der Berechnungen zurück, inklusive der Konfiguration die
	 * aktuell berechnet wird.
	 * 
	 * @return Liste der Konfigurationen als FIFO-Queue angeordnet.
	 */
	public List<allocation.Configuration> getQueue() {
		ArrayList<allocation.Configuration> list = new ArrayList<>(4);
		// hier könnte QUEUE_SIZE verwendet werden da die queue jedoch meist
		// nicht voll sein wird hier nur 4
		// hier könnte QUEUE_SIZE verwendet werden da die queue jedoch meist
		// nicht voll sein wird hier nur 4
		synchronized (this) {
			if (currentlyCalculatedConfiguration != null) {
				list.add(currentlyCalculatedConfiguration);
			}
			for (allocation.Configuration i : configurationQueue) {
				list.add(i);
			}
		}
		return list;
	}

	private void setAllocator(AbstractAllocator allocator) {
		this.allocator = allocator;
	}

	/**
	 * hier wird der thread gestartet der überpüft ob die liste leer ist und sie
	 * gegebenenfalls abarbeitet
	 */
	private void calculate() {
		executer = Executors.newFixedThreadPool(1);
		calculator = new Thread(runnable);
		executer.execute(calculator);
	}

}
