package run.regression.symbol;

import geneticProgramming.GpNode;
import geneticProgramming.SymbolType;

/** Terminal symbol. Random real number (0 - 1.0). */
public class Random extends SymbolType {
	@Override
	public Object evaluate(GpNode node, Object obj) {
		return node.getExtraValue();
	}

	@Override
	public Object initialValue() {
		return (Double) (Math.random() * 1.0);
	}
}
