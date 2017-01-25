// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package allocation;

import java.util.NoSuchElementException;

import gurobi.GRB;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBVar;

/************************************************************/
/**
 * Das Kriterium sorgt dafür, dass Teams möglichst die vom Admin gewünschte
 * Teamgröße haben.
 */
public class CriterionPreferredTeamSize implements GurobiCriterion {
	private String name;

	/**
	 * Standard-Konstruktor, der den Namen eindeutig setzt
	 */
	public CriterionPreferredTeamSize() {
		this.name = "PreferredTeamSize";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void useCriteria(Configuration configuration, GurobiAllocator allocator, double weight)
			throws GRBException, NoSuchElementException {
		GRBLinExpr bonus = new GRBLinExpr();
		double prefSize;

		// Finde die gewünschte Teamgröße

		prefSize = configuration.getParameters().stream().filter(parameter -> parameter.getName().equals("prefSize"))
				.findFirst().get().getValue();

		for (int j = 0; j < configuration.getTeams().size(); j++) {

			// Bestimme maximale Teamgröße
			double maxSize;
			if (configuration.getTeams().get(j).getProject().getMaxTeamSize() == -1) {
				maxSize = configuration.getParameters().stream()
						.filter(parameter -> parameter.getName().equals("maxSize")).findFirst().get().getValue();
			} else {
				maxSize = configuration.getTeams().get(j).getProject().getMaxTeamSize();
			}

			// benötigte Variablen
			GRBLinExpr varianceToPrefSize = new GRBLinExpr();
			GRBVar isPrefSize;
			GRBVar auxiliaryVariable;
			isPrefSize = allocator.getModel().addVar(0, 1, 0, GRB.BINARY, GurobiAllocator.NULL);
			auxiliaryVariable = allocator.getModel().addVar(0, 1, 0, GRB.BINARY, GurobiAllocator.NULL);

			// Initialisiere alle benötigten Teilterme
			GRBLinExpr negationAuxiliaryVariable = new GRBLinExpr();
			GRBLinExpr negationIsPrefSize = new GRBLinExpr();
			GRBLinExpr leftSideFirstConstraint = new GRBLinExpr();
			GRBLinExpr rightSideFirstConstraint = new GRBLinExpr();
			GRBLinExpr leftSideSecondConstraint = new GRBLinExpr();
			GRBLinExpr rightSideSecondConstraint = new GRBLinExpr();

			negationAuxiliaryVariable.addConstant(1);
			negationAuxiliaryVariable.addTerm(-1, auxiliaryVariable);

			negationIsPrefSize.addConstant(1);
			negationIsPrefSize.addTerm(-1, isPrefSize);

			varianceToPrefSize.addTerm(1, allocator.getTeamSizes()[j]);
			varianceToPrefSize.addConstant(-prefSize);

			leftSideFirstConstraint.multAdd(-maxSize, negationIsPrefSize);

			rightSideFirstConstraint.multAdd(maxSize, negationIsPrefSize);

			leftSideSecondConstraint.multAdd(0.1, negationIsPrefSize);
			leftSideSecondConstraint.multAdd(-(maxSize + 0.1), negationAuxiliaryVariable);

			rightSideSecondConstraint.multAdd(-0.1, negationIsPrefSize);
			rightSideSecondConstraint.multAdd((maxSize + 0.1), negationAuxiliaryVariable);

			allocator.getModel().addConstr(leftSideFirstConstraint, GRB.LESS_EQUAL, varianceToPrefSize,
					GurobiAllocator.NULL);
			allocator.getModel().addConstr(varianceToPrefSize, GRB.LESS_EQUAL, rightSideFirstConstraint,
					GurobiAllocator.NULL);

			allocator.getModel().addConstr(leftSideSecondConstraint, GRB.LESS_EQUAL, varianceToPrefSize,
					GurobiAllocator.NULL);
			allocator.getModel().addConstr(varianceToPrefSize, GRB.LESS_EQUAL, rightSideSecondConstraint,
					GurobiAllocator.NULL);

			bonus.addTerm(weight * 10, isPrefSize);
		}
		allocator.getOptimizationTerm().add(bonus);
	}
}