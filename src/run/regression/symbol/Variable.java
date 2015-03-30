package run.regression.symbol;

import geneticProgramming.GpNode;
import geneticProgramming.SymbolType;
import run.regression.RegressionIndividual;

/** Terminal symbol. It represents 'variable' like 'x'. The value of variable is pulled from RegressionIndividual.
 * A little complicated implementation. */
public class Variable extends SymbolType {
	private int variableIndex = 0;

	public Variable(int variableIndex) {
		this.variableIndex = variableIndex;
	}

	@Override
	public Object evaluate(GpNode node, Object obj) {
		return ((RegressionIndividual) obj).getVariable(this.variableIndex);
	}
}
