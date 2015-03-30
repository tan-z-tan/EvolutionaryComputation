package base.selector;

import java.util.List;

import base.Individual;

/**
 * The Selector class selects individuals using a certain way using fitness
 * values in Evolutionary Computation.
 * 
 * @author Tanji
 */
public abstract class Selector<T extends Individual> {
	/** the flag which means fitness value should be evaluated by normal order */
	public static final boolean NORMAL = true;
	/** the flag which means fitness value should be evaluated by reverse order */
	public static final boolean REVERSE = false;
	/** generation of PType */
	protected List<T> _population;

	/**
	 * Inverse flag which is true if low fitness value one is good one,
	 * otherwise false
	 */
	protected boolean _reverse;

	public void init(List<T> population, boolean reverse) {
		_population = population;
		_reverse = reverse;
	}

	/**
	 * returns a random PType in generation, selection way is depended to
	 * subclasses
	 */
	public abstract T getRandomPType();

	/**
	 * returns a List of random PType in generation, selection way is depended
	 * to subclasses
	 */
	public abstract List<T> getRandomPTypeList(int selectionSize);
}
