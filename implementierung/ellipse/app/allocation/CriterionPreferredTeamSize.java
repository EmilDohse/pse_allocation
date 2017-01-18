// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package allocation;

import java.util.NoSuchElementException;

import exception.AllocationException;
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
			throws AllocationException {
		GRBLinExpr bonus = new GRBLinExpr();
		double prefSize;
		
		//Finde die gewünschte Teamgröße
		try {
			prefSize = configuration.getParameters().stream().filter(parameter -> parameter.getName().equals("prefSize")).findFirst().get().getValue();
		} catch (NoSuchElementException e) {
			throw new AllocationException();
		}
		
		for (int i = 0; i < configuration.getStudents().size(); i++) {
			for (int j = 0; j < configuration.getTeams().size(); j++) {
				
				// Bestimme maximale Teamgröße
				double maxSize;
				if (configuration.getTeams().get(j).getProject().getMaxTeamSize() == -1) {
					try {
					maxSize = configuration.getParameters().stream().filter(parameter -> parameter.getName().equals("maxSize")).findFirst()
							.get().getValue();
					} catch (NoSuchElementException e) {
						throw new AllocationException();
					}
				} else {
					maxSize = configuration.getTeams().get(j).getProject().getMaxTeamSize();
				}
				/*
				GRBLinExpr varianceToPrefSize = new GRBLinExpr();
				GRBVar isPrefSize = allocator.getModel().addVar(0, 1, 0, GRB.BINARY, GurobiAllocator.NULL);
				GRBVar negatedIsPrefSize = allocator.getModel().addVar(0, 1, 0, GRB.BINARY, GurobiAllocator.NULL);
				GRBVar helpingVariable = allocator.getModel().addVar(0, 1, 0, GRB.BINARY, GurobiAllocator.NULL);
				GRBVar negateHelpingVariable = allocator.getModel().addVar(0, 1, 0, GRB.BINARY, GurobiAllocator.NULL);
				
				GRBLinExpr negationHelpingVariable = new GRBLinExpr();
				GRBLinExpr leftSideFirstConstraint = new GRBLinExpr();
				GRBLinExpr rightSideFirstConstraint = new GRBLinExpr();
				GRBLinExpr leftSideSecondConstraint = new GRBLinExpr();
				GRBLinExpr rightSideSecondConstraint = new GRBLinExpr();
				
				negationHelpingVariable.addConstant(1);
				negationHelpingVariable.addTerm(-1, helpingVariable);
				
				
				varianceToPrefSize.addTerm(1, allocator.getTeamSizes()[j]);
				varianceToPrefSize.addConstant(-prefSize);
				
				leftSideFirstConstraint.addTerm(-maxSize, negateHelpingVariable);
				rightSideFirstConstraint.addTerm(maxSize, negateHelpingVariable);
				leftSideSecondConstraint.addTerm(0.1, negateHelpingVariable);
				leftSideSecondConstraint.addTerm(-(maxSize + 0.1), helpingVariable);
				rightSideSecondConstraint.addTerm(-0.1, negateHelpingVariable);
				rightSideSecondConstraint.addTerm(maxSize + 0.1, negateHelpingVariable);
				*/
				
			}
		}
		
	}
}