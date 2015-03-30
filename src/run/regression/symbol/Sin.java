package run.regression.symbol;

import geneticProgramming.GpNode;
import geneticProgramming.SymbolType;

/** Operation of sine function */
public class Sin extends SymbolType {
	@Override
	public Object evaluate(GpNode node, Object obj) {
		return Math.sin((Double) node.getChild(0).evaluate(obj));
	}
}
