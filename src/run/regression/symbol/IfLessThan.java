package run.regression.symbol;

import geneticProgramming.GpNode;
import geneticProgramming.SymbolType;

/** Operation of If less than. It returns child(2) if child(0) < child(1) otherwise returns child(3). */
public class IfLessThan extends SymbolType {
	@Override
	public Object evaluate(GpNode node, Object obj) {
		if ((Double) node.getChild(0).evaluate(obj) < (Double) node.getChild(1).evaluate(obj)) {
			return node.getChild(2).evaluate(obj);
		} else
			return node.getChild(3).evaluate(obj);
	}
}
