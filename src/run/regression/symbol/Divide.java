package run.regression.symbol;

import geneticProgramming.GpNode;
import geneticProgramming.SymbolType;

/** Operation of Division. It returns 1 when the denominator is 0. */
public class Divide extends SymbolType {
	@Override
	public Object evaluate(GpNode node, Object obj) {
		if ((Double) node.getChild(1).evaluate(obj) == 0)
			return new Double(1);
		return (Double) node.getChild(0).evaluate(obj) / (Double) node.getChild(1).evaluate(obj);
	}
}
