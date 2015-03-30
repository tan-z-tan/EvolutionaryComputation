package base.selector;

import java.util.ArrayList;
import java.util.List;

import base.Individual;
import util.RandomManager;
import geneticAlgorithm.GaIndividual;

/**
 * An implement class of Roulette Selection.
 * 
 * @author tanji
 * @param <T>
 *            T must be subclass of Individual.
 */
public class RouletteSelector<T extends Individual> extends Selector<T> {
	/** average of adaptation values in a generation */
	protected double _averageValue;
	/** sum of adaptation values in a generation */
	protected double _sumValue;
	/** max fitness value */
	protected double _max;

	public RouletteSelector(List<T> population) {
		init(population, true);
		calcAverageValue();
	}

	public RouletteSelector(List<T> population, boolean reverse) {
		init(population, reverse);
		calcAverageValue();
	}

	/** calculates average of fitness value in generation */
	protected void calcAverageValue() {
		if (_reverse == REVERSE) {
			_max = 0;
			for (Individual pType : _population)
				if (_max < pType.getFitnessValue())
					_max = pType.getFitnessValue();

			_sumValue = 0;
			for (Individual pType : _population)
				_sumValue += _max - pType.getFitnessValue();

			_averageValue = _sumValue / _population.size();
		} else
		// normal order
		{
			_sumValue = 0;
			for (Individual pType : _population) {
				_sumValue += pType.getFitnessValue();
			}
			_averageValue = _sumValue / _population.size();
		}
	}

	@Override
	public T getRandomPType() {
		double fitnessSum = 0;
		double rouletteValue = RandomManager.getRandom() * _sumValue;
		if (Double.isNaN(_averageValue) || _averageValue == 0)
			return _population.get((int) (RandomManager.getRandom() * _population.size()));

		for (int i = 0; i < _population.size(); i++) {
			if (_reverse == REVERSE) {
				fitnessSum += _max - _population.get(i).getFitnessValue();
			} else {
				fitnessSum += _population.get(i).getFitnessValue();
			}

			if (fitnessSum >= rouletteValue) // routelle stop
			{
				return _population.get(i);
			}
		}
		if (Double.isNaN(fitnessSum))
			return _population.get(0);
		System.out.println("okashiinnjane-: " + fitnessSum);
		return null;
	}

	@Override
	public List<T> getRandomPTypeList(int selectionSize) {
		List<T> resultList = new ArrayList<T>(selectionSize);
		for (int i = 0; i < selectionSize; i++) {
			resultList.add(getRandomPType());
		}
		return resultList;
	}

	// main for test
	public static void main(String args[]) {
		List<Individual> population = new ArrayList<Individual>();
		Individual individual1 = new GaIndividual<Integer>();
		Individual individual2 = new GaIndividual<Integer>();
		Individual individual3 = new GaIndividual<Integer>();
		Individual individual4 = new GaIndividual<Integer>();
		Individual individual5 = new GaIndividual<Integer>();
		individual1.setFitnessValue(1);
		individual2.setFitnessValue(2);
		individual3.setFitnessValue(3);
		individual4.setFitnessValue(4);
		individual5.setFitnessValue(5);
		population.add(individual1);
		population.add(individual2);
		population.add(individual3);
		population.add(individual4);
		population.add(individual5);
		Selector<Individual> selector = new RouletteSelector<Individual>(population, Selector.REVERSE);

		System.out.println(selector.getRandomPType());
		System.out.println(selector.getRandomPType());
		System.out.println(selector.getRandomPType());
		System.out.println(selector.getRandomPType());
	}
}
