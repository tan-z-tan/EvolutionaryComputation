package run.regression.symbol;

import geneticProgramming.GpNode;
import geneticProgramming.SymbolType;

/** Operation of subtruct function */
public class Minus extends SymbolType {
	@Override
	public Object evaluate(GpNode node, Object obj) {
		return (Double) node.getChild(0).evaluate(obj) - (Double) node.getChild(1).evaluate(obj);
	}
}
