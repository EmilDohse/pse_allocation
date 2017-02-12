package deadline;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import data.GeneralData;

public class StateStorage {

    private static StateStorage            instance;

    private State                          currentState;

    private final ScheduledExecutorService scheduler;

    private StateStorage() {
        this.currentState = State.BEFORE_REGISTRATION_PHASE;
        this.scheduler = Executors.newScheduledThreadPool(2);
        Date start = GeneralData.loadInstance().getCurrentSemester()
                .getRegistrationStart();
        Date end = GeneralData.loadInstance().getCurrentSemester()
                .getRegistrationEnd();
        if (start != null && end != null) {
            initStateChanging(start, end);
        }
    }

    /**
     * Diese Methode initialisiert oder aktualisiert das Zustandwechseln auf
     * neue Daten. Diese Methode ist dafür verantwortlich, das zu den
     * Zustandsänderungsdaten der Zustand auch wirklich geändert wird.
     * 
     * @param start
     *            das Startdatum für die Registrierung
     * @param end
     *            das Enddatum für die Registrierung
     */
    public final void initStateChanging(Date start, Date end) {
        scheduler.shutdownNow();
        Date now = new Date();
        long delayUntilRegistration = start.getTime() - now.getTime();
        long delayUntilAfterRegistration = end.getTime() - now.getTime();
        scheduler.schedule(new Runnable() {

            @Override
            public void run() {
                setCurrentState(State.REGISTRATION_PHASE);
            }
        }, delayUntilRegistration, TimeUnit.MILLISECONDS);
        scheduler.schedule(new Runnable() {

            @Override
            public void run() {
                setCurrentState(State.AFTER_REGISTRATION_PHASE);
            }
        }, delayUntilAfterRegistration, TimeUnit.MILLISECONDS);
    }

    /**
     * Diese Methode gibt den aktuellen Zustand zurück.
     * 
     * @return den aktuellen Zustand.
     */
    public State getCurrentState() {
        return currentState;
    }

    private void setCurrentState(State newState) {
        this.currentState = newState;
    }

    /**
     * Diese Methode gibt die einzige Instanz des Objektes zurück.
     * 
     * @return die einzige Instanz
     */
    public static StateStorage getInstance() {
        if (instance == null) {
            instance = new StateStorage();
        }
        return instance;
    }

    /**
     * Aufzählungen aller Zustände.
     * 
     * @author daniel
     *
     */
    public enum State {
        BEFORE_REGISTRATION_PHASE, REGISTRATION_PHASE, AFTER_REGISTRATION_PHASE;
    }
}
