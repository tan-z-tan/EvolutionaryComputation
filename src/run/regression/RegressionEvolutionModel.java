package run.regression;

import geneticProgramming.GPEvolutionModel;
import geneticProgramming.GpEnvironment;
import geneticProgramming.GpTreeManager;

import java.util.List;

public class RegressionEvolutionModel extends GPEvolutionModel<RegressionIndividual, GpEnvironment<RegressionIndividual>> {
	private List<Double[][]> correctData;
	private double sizePenalty = 0.0;
	
	public void setCorrectData(List<Double[][]> correctData) {
		this.correctData = correctData;	
	}
	
	public void setSizePenalty(double penalty) {
		this.sizePenalty = penalty;
	}
	
	@Override
	public void evaluateIndividual(RegressionIndividual individual) {
		double error = (Double) individual.evaluate(correctData);
		
		if( sizePenalty != 0 ) {
			int size = GpTreeManager.getNodeSize(individual.getRootNode());
			error += size * sizePenalty * (_environment.getSelectionOrder() ? -1:1);
		}
		individual.setFitnessValue(error);
	}
}
