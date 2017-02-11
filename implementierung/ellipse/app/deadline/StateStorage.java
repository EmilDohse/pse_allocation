package deadline;

import java.util.HashMap;

import play.routing.Router;
import play.routing.RoutingDsl;

public class StateStorage {

    private static StateStorage instance;

    private State               currentState;

    private StateStorage() {
        this.currentState = State.BEFORE_REGISTRATION_PHASE;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public static StateStorage getInstance() {
        if (instance == null) {
            instance = new StateStorage();
        }
        return instance;
    }

    public enum State {
        BEFORE_REGISTRATION_PHASE, REGISTRATION_PHASE, AFTER_REGISTRATION_PHASE;
    }
}
