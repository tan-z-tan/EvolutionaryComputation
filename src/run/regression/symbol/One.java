package run.regression.symbol;

import geneticProgramming.GpNode;
import geneticProgramming.SymbolType;

/** Terminal symbol, always returns 1. */
public class One extends SymbolType {
	@Override
	public Object evaluate(GpNode node, Object obj) {
		return (Double) 1.0;
	}
}
