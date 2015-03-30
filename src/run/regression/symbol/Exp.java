package run.regression.symbol;

import geneticProgramming.GpNode;
import geneticProgramming.SymbolType;

/**
 * Operation of exponential function
 */
public class Exp extends SymbolType {
	@Override
	public Object evaluate(GpNode node, Object obj) {
		return Math.exp((Double) node.getChild(0).evaluate(obj));
	}
}
