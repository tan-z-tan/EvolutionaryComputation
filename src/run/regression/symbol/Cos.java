package run.regression.symbol;

import geneticProgramming.GpNode;
import geneticProgramming.SymbolType;

/** Operation of cosine function */
public class Cos extends SymbolType {
	@Override
	public Object evaluate(GpNode node, Object obj) {
		return Math.cos((Double) node.getChild(0).evaluate(obj));
	}
}
