package base.selector;

import java.util.ArrayList;
import java.util.List;

import base.Individual;
import util.RandomManager;
import geneticAlgorithm.GaIndividual;

/**
 * An implement class of Tournament Selection.
 * 
 * @author tanji
 * 
 */
public class TournamentSelector<T extends Individual> extends Selector<T> {
	protected int _tournamentSize;

	public TournamentSelector(List<T> population, int tournamentSize) {
		init(population, true);
		_tournamentSize = tournamentSize;
	}

	public TournamentSelector(List<T> population, int tournamentSize, boolean reverse) {
		init(population, reverse);
		_tournamentSize = tournamentSize;
	}

	public void setTournamentSize(int size) {
		this._tournamentSize = size;
	}

	@Override
	public T getRandomPType() {
		List<T> competitiveGroup = new ArrayList<T>();
		for (int i = 0; i < _tournamentSize; i++) {
			competitiveGroup.add(_population.get((int) (RandomManager.getRandom() * _population.size())));
		}
		T bestOne = competitiveGroup.get(0);

		for (int i = 0; i < _tournamentSize; i++) {
			if (_reverse == NORMAL) {
				if (bestOne.getFitnessValue() < competitiveGroup.get(i).getFitnessValue()) {
					bestOne = competitiveGroup.get(i);
				}
			} else if (bestOne.getFitnessValue() > competitiveGroup.get(i).getFitnessValue()) {
				bestOne = competitiveGroup.get(i);
			}
		}
		return bestOne;
	}

	@Override
	public List<T> getRandomPTypeList(int selectionSize) {
		List<T> resultList = new ArrayList<T>(selectionSize);
		for (int i = 0; i < selectionSize; i++) {
			resultList.add(getRandomPType());
		}
		return resultList;
	}

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
		Selector<Individual> selector = new TournamentSelector<Individual>(population, 2, Selector.NORMAL);

		System.out.println(selector.getRandomPType());
		System.out.println(selector.getRandomPType());
		System.out.println(selector.getRandomPType());
		System.out.println(selector.getRandomPType());
	}
}
