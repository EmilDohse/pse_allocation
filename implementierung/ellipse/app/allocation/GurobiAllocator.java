// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package allocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;

import com.avaje.ebean.Ebean;

import data.Allocation;
import data.AllocationParameter;
import data.Team;
import gurobi.GRB;
import gurobi.GRB.DoubleAttr;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

/************************************************************/
/**
 * Der Gurobi Allocator dient zur Berechnung einer Einteilung mit dem ILP Solver
 * Gurobi. Weiterhin stellt er ein Basismodell und einen Optimierungsterm zur
 * Verfügung, welche von den Kriterien verwendet werden.
 */
public class GurobiAllocator extends AbstractAllocator {

    /**
     * String-Konstante für Gurobi
     */
    public static final String NULL = new String();

    /**
     * Die Basismatrix (NxM), welche anzeigt, ob ein Student n in einem Team m
     * ist. Die Mte Spalte ist das Team der nicht Zugeteilten.
     */
    private GRBVar[][]         basicMatrix;
    /**
     * Über Constraints dynamisch bestimmte Teamgröße. (Hilfsvariablen)
     */
    private GRBVar[]           teamSizes;
    /**
     * Der Optimierungsterm der von Kriterien erweitert wird und zur Berechnung
     * der Einteilung verwendet wird.
     */
    private GRBLinExpr         optTerm;
    /**
     * Das zur Berechnung verwendete Gurobi Model.
     */
    private GRBModel           model;

    /**
     * Konstruktor, der das Basismodell initialisiert.
     */
    public GurobiAllocator() {

    }

    /**
     * Getter für die Basismatrix.
     * 
     * @return Die Basismatrix
     */
    public GRBVar[][] getBasicMatrix() {
        return this.basicMatrix;
    }

    /**
     * Getter für das Modell.
     * 
     * @return Das Modell
     */
    public GRBModel getModel() {
        return this.model;
    }

    /**
     * Getter für den Optimierungsterm.
     * 
     * @return Der Optimierungsterm
     */
    public GRBLinExpr getOptimizationTerm() {
        return optTerm;
    }

    /**
     * Getter für die, über Constraints bestimmten, Teamgrößen
     * 
     * @return Array von Teamgrößen
     */
    public GRBVar[] getTeamSizes() {
        return this.teamSizes;
    }

    /**
     * Startet die Berechnung einer Einteilung.
     * 
     * @param configuration
     *            Die Konfiguration, nach der die Einteilung berechnet werden
     *            soll.
     */
    @Override
    public void calculate(Configuration configuration) {

        // Hier wird die eigentliche Berechnung durchgeführt
        GRBEnv env = null;
        try {
            env = new GRBEnv();
            this.model = this.makeModel(configuration, env);
            this.model.optimize();
        } catch (GRBException e) {
            Allocation failure = nullObject("allocation.gurobiException");
            Ebean.save(failure);
            return;
        } catch (NoSuchElementException e) {
            Allocation failure = nullObject("allocation.parameterNotFound");
            Ebean.save(failure);
            return;
        }

        // erstelle Teams
        for (int i = 0; i < configuration.getTeams().size(); i++) {
            for (int j = 0; j < configuration.getStudents().size(); j++) {
                double result;
                try {
                    result = this.basicMatrix[j][i].get(DoubleAttr.X);
                } catch (GRBException e) {
                    Allocation failure = nullObject("allocation.gurobiException");
                    Ebean.save(failure);
                    return;
                }
                if (result == 1) {
                    configuration.getTeams().get(i).addMember(configuration.getStudents().get(j));
                }
            }
        }
        // Mache Environment und Model ungültig
        try {
            this.model.dispose();
            env.dispose();
        } catch (GRBException e) {
            Allocation failure = nullObject("allocation.gurobiException");
            Ebean.save(failure);
            return;
        }

        // Erstelle Einteilung
        Allocation allocation = new Allocation(configuration.getTeams(), configuration.getName(),
                configuration.getParameters());
        Ebean.save(allocation);

    }

    /**
     * bricht die berechnung ab
     */
    @Override
    public void cancel() {
        this.model.terminate();
    }

    /**
     * Lädt alle Implementierungen des GurobiCriteria Interfaces über einen
     * ServiceLoader.
     * 
     * @return Die Liste aller Gurobi Kriterien.
     */
    public static List<GurobiCriterion> getAllCriteria() {
        Iterator<GurobiCriterion> iter = ServiceLoader.load(GurobiCriterion.class).iterator();
        ArrayList<GurobiCriterion> criteria = new ArrayList<GurobiCriterion>();
        while (iter.hasNext()) {
            criteria.add(iter.next());
        }
        return criteria;
    }

    private GRBModel makeModel(Configuration configuration, GRBEnv env) throws GRBException, NoSuchElementException {
        GRBModel model = new GRBModel(env);

        // Erstelle Basismatrix B

        this.basicMatrix = new GRBVar[configuration.getStudents().size()][configuration.getTeams().size() + 1];
        for (int i = 0; i < configuration.getStudents().size(); i++) {
            for (int j = 0; i <= configuration.getTeams().size(); j++) {
                this.basicMatrix[i][j] = this.model.addVar(0, 1, 0, GRB.BINARY, NULL);
            }
        }

        // Erzeuge Basisconstraint
        // Genau 1 Team pro Student

        for (int i = 0; i < configuration.getStudents().size(); i++) {
            GRBLinExpr teamsPerStudent = new GRBLinExpr();
            for (int j = 0; j <= configuration.getTeams().size(); j++) {
                teamsPerStudent.addTerm(1, this.basicMatrix[i][j]);
            }
            this.model.addConstr(teamsPerStudent, GRB.EQUAL, 1, NULL);
        }

        // Erzeuge Teamgröße-Variablen

        this.teamSizes = new GRBVar[configuration.getTeams().size()];

        for (int i = 0; i < configuration.getTeams().size(); i++) {
            teamSizes[i] = this.model.addVar(0, Double.MAX_VALUE, 0, GRB.INTEGER, NULL);
            GRBLinExpr teamSum = new GRBLinExpr();
            for (int j = 0; j < configuration.getStudents().size(); j++) {
                teamSum.addTerm(1, this.basicMatrix[j][i]);
            }
            this.model.addConstr(teamSizes[i], GRB.EQUAL, teamSum, NULL);
        }

        // Bestimme die vom Admin eingestellte min- und max-Größe
        List<AllocationParameter> parameters = configuration.getParameters();
        double minAdminSize;
        double maxAdminSize;
        minAdminSize = parameters.stream().filter(parameter -> parameter.getName().equals("minSize")).findFirst().get()
                .getValue();
        maxAdminSize = parameters.stream().filter(parameter -> parameter.getName().equals("maxSize")).findFirst().get()
                .getValue();

        // Teamgröße zwischen min und max, oder 0
        for (int i = 0; i < configuration.getTeams().size(); i++) {
            GRBVar correctTeamSize = this.model.addVar(0, 1, 0, GRB.BINARY, NULL);

            GRBLinExpr secondConstraintRightSide = new GRBLinExpr();
            GRBLinExpr thirdConstraintRightSide = new GRBLinExpr();

            secondConstraintRightSide.addTerm(getMaxSize(configuration.getTeams().get(i), maxAdminSize),
                    correctTeamSize);

            thirdConstraintRightSide.addTerm(getMinSize(configuration.getTeams().get(i), minAdminSize),
                    correctTeamSize);

            this.model.addConstr(correctTeamSize, GRB.LESS_EQUAL, this.teamSizes[i], NULL);
            this.model.addConstr(this.teamSizes[i], GRB.LESS_EQUAL, secondConstraintRightSide, NULL);
            this.model.addConstr(this.teamSizes[i], GRB.GREATER_EQUAL, thirdConstraintRightSide, NULL);
        }

        // Initialisiere Optimierungsterm
        this.optTerm = new GRBLinExpr();

        // füge Kriterien hinzu
        List<GurobiCriterion> criteria = getAllCriteria();
        for (GurobiCriterion criterion : criteria) {

            // Finde den vom Admin eingegebenen Parameter
            double weight;
            weight = configuration.getParameters().stream()
                    .filter(parameter -> parameter.getName().equals(criterion.getName())).findFirst().get().getValue();
            if (weight != 0) {
                criterion.useCriteria(configuration, this, weight);
            }
        }

        // Stelle Modell auf Maximierung ein
        model.setObjective(this.optTerm, GRB.MAXIMIZE);

        return model;
    }

    /**
     * Berechne minimale Teamgröße
     * 
     * @param team
     *            Das Team
     * @param minAdminSize
     *            Die vom Admin eingestellte Größe
     * @return Die Teamgröße
     */
    private int getMinSize(Team team, double minAdminSize) {
        if (team.getProject().getMinTeamSize() == -1) {
            return (int) minAdminSize;
        } else {
            return team.getProject().getMinTeamSize();
        }
    }

    /**
     * Berechne maximale Teamgröße
     * 
     * @param team
     *            Das Team
     * @param minAdminSize
     *            Die vom Admin eingestellte Größe
     * @return Die Teamgröße
     */
    private int getMaxSize(Team team, double maxAdminSize) {
        if (team.getProject().getMaxTeamSize() == -1) {
            return (int) maxAdminSize;
        } else {
            return team.getProject().getMaxTeamSize();
        }
    }

    private Allocation nullObject(String errorMessage) {
        Allocation failedAllocation = new Allocation(new ArrayList<Team>(), errorMessage,
                new ArrayList<AllocationParameter>());
        return failedAllocation;
    }
}
