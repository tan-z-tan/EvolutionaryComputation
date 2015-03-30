package base;

import java.util.List;

/**
 * A prototype model of Evolutionary Computation. Both GA and GP algorithms are
 * executed in an EvolutionModel object. EvolutionModel.run is the most top
 * level method of executing EC. All subclasses are supposed to implement
 * sub-methods in run.
 * @author tanji
 */
public abstract class EvolutionModel<T extends Individual, E extends Environment<T>> implements Runnable {
	protected E _environment;
	protected T bestIndividual;
	protected boolean _parallel = false;
	protected int evaluationCount = 0;
	protected double averageFitness;
	protected int _id;
	protected int _size;
	protected long _takenTime;
	protected boolean _finished = false;
	protected int activeThreadNum = 0;

	public EvolutionModel() {
	}

	public EvolutionModel(E environment) {
		_environment = environment;
	}

	public abstract void initialize();

	@Override
	public void run() {
		System.out.println("*** Experiment Parameters ");
		System.out.println("*** " + _environment.getAttributes());
		long startTime = System.currentTimeMillis();
		long cummulativeEvaluationTime = 0;
		initialize();
		for (int i = 0; i < _environment.getRepetitionNumber(); i++) {
			System.out.println("***** Generation " + _environment.getGenerationCount() + " ");

			long start = System.currentTimeMillis();

			beforeEvaluation();
			evaluate();

			cummulativeEvaluationTime = System.currentTimeMillis() - start;
			System.out.println("### Evaluation time = " + cummulativeEvaluationTime);

			if (_finished || i == _environment.getRepetitionNumber() - 1) {
				break;
			}
			start = System.currentTimeMillis();
			updateGeneration();
			cummulativeEvaluationTime = System.currentTimeMillis() - start;
			_environment.setGenerationCount(_environment.getGenerationCount() + 1);
		}
		_takenTime = System.currentTimeMillis() - startTime;

		finish();
	}

	public void evaluate() {
		bestIndividual = _environment.getPopulation().get(0);
		double averageFitness = 0;

		for (T individual : _environment.getPopulation()) {
			evaluationCount++;
			evaluateIndividual(individual);
			averageFitness += individual.getFitnessValue();

			if (_environment.getSelectionOrder() && bestIndividual.getFitnessValue() < individual.getFitnessValue()) {
				bestIndividual = individual;
			} else if (!_environment.getSelectionOrder() && bestIndividual.getFitnessValue() > individual.getFitnessValue()) {
				bestIndividual = individual;
			}

			recordIndividual(individual);
		}

		averageFitness = averageFitness / _environment.getPopulationSize();
		System.out.println("# Best Fitness = " + bestIndividual._fitnessValue);
		System.out.println("# Average Fitness = " + averageFitness);
		endOfEvaluation();

		if (isTerminal(bestIndividual)) {
			System.out.println("success!");
			_finished = true;
		}
	}

	/**
	 * Override this method to record or measure statistics. e.g. Fitness
	 * average, GP tree depth dist.
	 * 
	 * @param individual
	 */
	public void recordIndividual(T individual) {
	}

	/**
	 * This method is called before evaluation of the population.
	 */
	protected void beforeEvaluation() {
	}

	/**
	 * This method is called after evaluation of the population.
	 */
	protected void endOfEvaluation() {
	}

	/** subclass must implement this method. */
	public boolean isTerminal(T bestIndividual) {
		return false;
	}

	/**
	 * Override this method to implements own GP system.
	 * 
	 * @param individual
	 */
	public abstract void evaluateIndividual(T individual);

	public abstract void updateGeneration();

	public void finish() {
		System.out.println("Time Taken by the Evolution = " + _takenTime);
	}

	/**
	 * returns current population.
	 * @return
	 */
	public List<T> getPopulation() {
		return _environment.getPopulation();
	}

	public E getEnvironment() {
		return _environment;
	}

	public void setEnvironment(E environment) {
		this._environment = environment;
	}
}
