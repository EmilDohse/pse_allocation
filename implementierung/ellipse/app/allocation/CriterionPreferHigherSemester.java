// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package allocation;

import exception.AllocationException;
import gurobi.GRBLinExpr;

/************************************************************/
/**
 * Das Kriterium sorgt dafür, dass Studierende höheren Semesters bevorzugt
 * werden.
 */
public class CriterionPreferHigherSemester implements GurobiCriterion {
    private String name;

    /**
     * Standard-Konstruktor, der den Namen eindeutig setzt
     */
    public CriterionPreferHigherSemester() {
        this.name = "PreferHigherSemester";
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
		// TODO hier weiter machen
		
	}
}