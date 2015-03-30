package run.regression;

import geneticProgramming.GpIndividual;
import geneticProgramming.GpTreeManager;

import java.util.List;

public class RegressionIndividual extends GpIndividual {
	private Double[] variables;

	/**
	 * Evaluates individuals by comparing individual's output and given correct data. CorrectData is like this
	 * -> list< [[variables],[result]]... > Fitness value is calculated as the
	 * average of error for each data.
	 * @param correctData
	 * @return
	 */
	public Object evaluate(List<Double[][]> correctData) {
		double error = 0;

		for (Double[][] data : correctData) {
			variables = data[0];
			Double value = (Double) _rootNode.evaluate(this);
			error += Math.abs(data[1][0] - value);
		}
		
		return error / correctData.size();
	}

	public Object getVariable(int i) {
		return variables[i];
	}
}
