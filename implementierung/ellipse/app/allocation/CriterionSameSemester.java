// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package allocation;

import data.GeneralData;
import data.Semester;
import gurobi.GRB;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBVar;

/************************************************************/
/**
 * Das Kriterium sorgt dafür, dass Studierende, des für das PSE vorgesehenen
 * Semesters, und Studierende höherer Semester nicht in das selbe Team kommen.
 */
public class CriterionSameSemester implements GurobiCriterion {
	private String name;

	/**
	 * Standard-Konstruktor, der den Namen eindeutig setzt
	 */
	public CriterionSameSemester() {
		this.name = "SameSemester";
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
	public void useCriteria(Configuration configuration, GurobiAllocator allocator, double weight) throws GRBException {
		GRBLinExpr bonus = new GRBLinExpr();

		int normalSemester = getNormalSemester(GeneralData.getCurrentSemester());
		for (int j = 0; j < configuration.getTeams().size(); j++) {

			// Erstelle Variablen; bis auf "AmountOfNormalSemesterStudents" alle
			// binär
			GRBVar AmountOfNormalSemesterStudents;
			GRBVar firstAuxiliaryVariable;
			GRBVar secondAuxiliaryVariable;
			GRBVar thirdAuxiliaryVariable;
			GRBVar orResult;
			GRBVar andResult;

			AmountOfNormalSemesterStudents = allocator.getModel().addVar(0, Double.MAX_VALUE, 0, GRB.INTEGER,
					GurobiAllocator.NULL);
			firstAuxiliaryVariable = allocator.getModel().addVar(0, 1, 0, GRB.BINARY, GurobiAllocator.NULL);
			secondAuxiliaryVariable = allocator.getModel().addVar(0, 1, 0, GRB.BINARY, GurobiAllocator.NULL);
			thirdAuxiliaryVariable = allocator.getModel().addVar(0, 1, 0, GRB.BINARY, GurobiAllocator.NULL);
			orResult = allocator.getModel().addVar(0, 1, 0, GRB.BINARY, GurobiAllocator.NULL);
			andResult = allocator.getModel().addVar(0, 1, 0, GRB.BINARY, GurobiAllocator.NULL);

			// Erstelle benötigte Teilterme
			GRBLinExpr normalSemesterCount = new GRBLinExpr();
			GRBLinExpr leftSideConstraintsOneAndTwo = new GRBLinExpr();
			GRBLinExpr rightSideConstraintOne = new GRBLinExpr();
			GRBLinExpr rightSideConstraintTwo = new GRBLinExpr();
			GRBLinExpr leftSideConstraintsThreeAndFour = new GRBLinExpr();
			GRBLinExpr rightSideConstraintFour = new GRBLinExpr();
			GRBLinExpr rightSideConstraintSix = new GRBLinExpr();

			// Summiere alle Matrixeinträge der Studenten im "normalen" Semester
			// im aktuellen Team
			for (int i = 0; i < configuration.getStudents().size(); i++) {
				if (configuration.getStudents().get(i).getSemester() == normalSemester) {
					normalSemesterCount.addTerm(1, allocator.getBasicMatrix()[i][j]);
				}
			}

			// Teilterme der Constraints aus Entwurfsdokument
			leftSideConstraintsOneAndTwo.addConstant(1);
			leftSideConstraintsOneAndTwo.addTerm(-1, firstAuxiliaryVariable);

			rightSideConstraintOne.addTerm(1, allocator.getTeamSizes()[j]);
			rightSideConstraintOne.addTerm(-1, AmountOfNormalSemesterStudents);

			rightSideConstraintTwo.addTerm(0.1, allocator.getTeamSizes()[j]);
			rightSideConstraintTwo.addTerm(-1, AmountOfNormalSemesterStudents);

			leftSideConstraintsThreeAndFour.addConstant(1);
			leftSideConstraintsThreeAndFour.addTerm(-1, secondAuxiliaryVariable);

			rightSideConstraintFour.addTerm(0.1, AmountOfNormalSemesterStudents);

			rightSideConstraintSix.addTerm(0.1, allocator.getTeamSizes()[j]);

			// Übergebe dem Allocator die Constraints

			allocator.getModel().addConstr(AmountOfNormalSemesterStudents, GRB.EQUAL, normalSemesterCount,
					GurobiAllocator.NULL);
			allocator.getModel().addConstr(leftSideConstraintsOneAndTwo, GRB.LESS_EQUAL, rightSideConstraintOne,
					GurobiAllocator.NULL);
			allocator.getModel().addConstr(leftSideConstraintsOneAndTwo, GRB.GREATER_EQUAL, rightSideConstraintTwo,
					GurobiAllocator.NULL);
			allocator.getModel().addConstr(leftSideConstraintsThreeAndFour, GRB.LESS_EQUAL,
					AmountOfNormalSemesterStudents, GurobiAllocator.NULL);
			allocator.getModel().addConstr(leftSideConstraintsThreeAndFour, GRB.GREATER_EQUAL, rightSideConstraintFour,
					GurobiAllocator.NULL);
			allocator.getModel().addConstr(thirdAuxiliaryVariable, GRB.LESS_EQUAL, allocator.getTeamSizes()[j],
					GurobiAllocator.NULL);
			allocator.getModel().addConstr(thirdAuxiliaryVariable, GRB.GREATER_EQUAL, rightSideConstraintSix,
					GurobiAllocator.NULL);
			orConstraint(firstAuxiliaryVariable, secondAuxiliaryVariable, orResult, allocator);
			andConstraint(orResult, thirdAuxiliaryVariable, andResult, allocator);

			bonus.addTerm(weight * 10, andResult);
		}
		allocator.getOptimizationTerm().add(bonus);
	}

	/**
	 * Bestimme "normales" Fachsemester für das PSE.
	 * 
	 * @param semester
	 *            Das Semester, das überprüft werden soll.
	 * @return 3 im WS, 4 im SS.
	 */
	private int getNormalSemester(Semester semester) {
		if (GeneralData.getCurrentSemester().isWintersemester()) {
			return 3;
		} else {
			return 4;
		}
	}

	/**
	 * Erstellt OR-Verknüpfung von zwei binären Variablen.
	 * 
	 * @param firstVar
	 *            Erste Variable.
	 * @param secondVar
	 *            Zweite Variable.
	 * @param resultVar
	 *            Ergebnisvariable.
	 * @param allocator
	 *            Der GurobiAllocator, dem die Verknüfung zugeordnet wird.
	 * @throws GRBException
	 *             Wird vom Kriterium behandelt.
	 */
	private void orConstraint(GRBVar firstVar, GRBVar secondVar, GRBVar resultVar, GurobiAllocator allocator)
			throws GRBException {
		GRBLinExpr firstConstraintLeftSide = new GRBLinExpr();
		GRBLinExpr secondConstraintLeftSide = new GRBLinExpr();

		firstConstraintLeftSide.addTerm(2, resultVar);
		firstConstraintLeftSide.addTerm(-1, firstVar);
		firstConstraintLeftSide.addTerm(-1, secondVar);

		secondConstraintLeftSide.addTerm(1, firstVar);
		secondConstraintLeftSide.addTerm(1, secondVar);
		secondConstraintLeftSide.addTerm(-2, resultVar);

		allocator.getModel().addConstr(firstConstraintLeftSide, GRB.LESS_EQUAL, 1, GurobiAllocator.NULL);
		allocator.getModel().addConstr(secondConstraintLeftSide, GRB.LESS_EQUAL, 0, GurobiAllocator.NULL);
	}

	/**
	 * Erstellt AND-Verknüpfung von zwei binären Variablen
	 * 
	 * @param firstVar
	 *            Erste Variable.
	 * @param secondVar
	 *            Zweite Variable.
	 * @param resultVar
	 *            Ergebnisvariable.
	 * @param allocator
	 *            Der GurobiAllocator, dem die Verknüfung zugeordnet wird.
	 * @throws GRBException
	 *             Wird vom Kriterium behandelt.
	 */
	private void andConstraint(GRBVar firstVar, GRBVar secondVar, GRBVar resultVar, GurobiAllocator allocator)
			throws GRBException {
		GRBLinExpr firstConstraintLeftSide = new GRBLinExpr();
		GRBLinExpr secondConstraintLeftSide = new GRBLinExpr();

		firstConstraintLeftSide.addTerm(1, firstVar);
		firstConstraintLeftSide.addTerm(1, secondVar);
		firstConstraintLeftSide.addTerm(-2, resultVar);

		secondConstraintLeftSide.addTerm(2, resultVar);
		secondConstraintLeftSide.addTerm(-1, firstVar);
		secondConstraintLeftSide.addTerm(-1, secondVar);

		allocator.getModel().addConstr(firstConstraintLeftSide, GRB.LESS_EQUAL, 1, GurobiAllocator.NULL);
		allocator.getModel().addConstr(secondConstraintLeftSide, GRB.LESS_EQUAL, 0, GurobiAllocator.NULL);
	}
}