// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package einteilung;

import RootElement.Modell.Daten.Allocation;
import RootElement.Modell.Einteilung.AbstractAllocator;
import RootElement.Modell.Einteilung.Configuration;

/************************************************************/
/**
 * 
 */
public class ILPAllocator extends AbstractAllocator {
	/**
	 * 
	 */
	private undefined basicMatrix:GRBVar[][];
	/**
	 * 
	 */
	private undefined model:GRBModel;

	/**
	 * 
	 */
	public void getBasicMatrix() : GRBVar[][]() {
	}

	/**
	 * 
	 */
	public void getModel() :

	GRBModel() {
	}

	/**
	 * 
	 * @param configuration 
	 * @return  
	 */
	public Allocation calculate(Configuration configuration) {
	}

	/**
	 * 
	 */
	public void getOptimizationTerm() :

	GRBLinExpr() {
	}
};
