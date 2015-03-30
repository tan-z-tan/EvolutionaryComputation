package geneticAlgorithm;

import java.util.List;

import base.Individual;

/**
 * Individual class of GP.
 * 
 * @author tanji
 */
public class GaIndividual<T> extends Individual {
	protected List<T> _genom;

	public GaIndividual() {
		_genom = null;
		_fitnessValue = 0;
	}

	public List<T> getGene() {
		return _genom;
	}

	public void setGene(List<T> gene) {
		_genom = gene;
	}
}