package geneticAlgorithm;

import java.util.ArrayList;
import java.util.List;

import base.EvolutionModel;

/**
 * @author tanji
 */
public class GAEvolutionModel<T extends GaIndividual<Number>, E extends GaEnvironment<T>> extends EvolutionModel<T, E> {
	protected Class<T> _individualClass;
	protected int generationForAuto = 10;

	protected List<Double> _offspringSizeList;

	public GAEvolutionModel(E environment) {
		this((Class<T>) GaIndividual.class, environment);
	}

	public GAEvolutionModel(Class<T> individualClass, E environment) {
		super(environment);
		_individualClass = individualClass;
		_environment = environment;
	}
	
	/**
	 * Override this method to implements own GP system.
	 * 
	 * @param individual
	 */
	public void evaluateIndividual(T individual) {

	}

	/** subclass must implement this method. */
	public boolean isTerminal(T bestIndividual) {
		return false;
	}

	@Override
	public void initialize() {
		_environment.setPopulation(new ArrayList<T>());
		int index = 0;
		
		List<T> population = new ArrayList<T>();
		for (int p = index; p < _environment.getPopulationSize(); p++) {
			T individual = createNewIndividual();
			List<Number> gene = new ArrayList<Number>();
			individual.setGene(gene);
			population.add(individual);
		}
		_environment.setPopulation(population);
	}

	/** creates a new individual using reflection */
	protected T createNewIndividual() {
		try {
			T newInstance = _individualClass.newInstance();
			return newInstance;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateGeneration() {
		long startTime = System.currentTimeMillis();
		if (_environment.getAttribute("method").equals("GA")) {
			// update
		}

		long endTime = System.currentTimeMillis();
		System.out.println("# update time = " + (endTime - startTime));
	}
}
