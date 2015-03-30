package geneticProgramming;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import base.EvolutionModel;
import base.selector.Selector;

/**
 * GP進化のプロトタイプモデル，個々の問題はこのクラスを継承することで簡潔に書ける． 自動でやってくれるところは以下のメソッド．
 * "initialise":　個体の生成・初期化．[initialisation=rampedHalfAndHalf]でramped half &
 * half, 他に[initialisation=full][initialization=grow]など．
 * 
 * @author tanji
 */
public class GPEvolutionModel<T extends GpIndividual, E extends GpEnvironment<T>> extends EvolutionModel<T, E> {
	protected Class<T> _individualClass;
	protected int generationForAuto = 10;

	protected List<Double> _offspringSizeList;
	protected double _averageTreeSize = 0;
	protected double _averageTransitionCount;
	protected double averageDepth = 0;
	
	public GPEvolutionModel() {
	}
	
	public GPEvolutionModel(Class<T> individualClass) {
		_individualClass = individualClass;
	}

	public GPEvolutionModel(Class<T> individualClass, E environment) {
		super(environment);
		initGP(environment, individualClass);
	}
	
	public void initGP(E environment, Class<T> individualClass) {
		_individualClass = individualClass;
		_environment = environment;
	}
	
	public void recordIndividual(T individual) {
		averageDepth += individual.getRootNode().getDepthFromHere();
	}
	
	protected void beforeEvaluation() {
		averageDepth = 0;
	}
	
	protected void endOfEvaluation() {
		System.out.println("# Average Depth: " + averageDepth / _environment.getPopulationSize());
		System.out.println("# Best Individual: " + GpTreeManager.getS_Expression(bestIndividual.getRootNode()));
	}

	/**
	 * Override this method to implements own GP system.
	 * @param individual
	 */
	public void evaluateIndividual(T individual) {
	}

	/** this can be implemented in a subclass */
	public boolean isTerminal(T bestIndividual) {
		return false;
	}

	@Override
	public void initialize() {
		_environment.setPopulation(new ArrayList<T>());

		int index = 0;
		// read the first population if it is specified.
		if (_environment.getAttribute("initialPopulation") != null) {
			System.out.println("initial population");
			File file = new File(_environment.getAttribute("initialPopulation"));
			System.out.println("File = " + file);
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				while (reader.ready() && _environment.getPopulation().size() < _environment.getPopulationSize()) {
					String line = reader.readLine();
					_environment.getPopulation().add(createNewIndividual());
					GpNode root = GpTreeManager.constructGpNodeFromString(line, _environment.getSymbolSet());
					GpTreeManager.calculateDepth(root, 1);
					_environment.getPopulation().get(index++).setRootNode(root);
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// ramped half & half
		if (_environment.getAttribute("initialization").equals("rampedHalfAndHalf")) {
			List<GpNode> genotypes = GpTreeManager.rampedHalfAndHalf(_environment);
			if (genotypes.size() != _environment.getPopulationSize()) {
				System.out.println(genotypes.size());
				System.out.println(_environment.getPopulationSize());
				System.out.println("サイズが違いますよ．Ramped-half-and-half");
				System.exit(0);
			}
			for (int i = index; i < _environment.getPopulationSize(); i++) {
				_environment.getPopulation().add(createNewIndividual());
				_environment.getPopulation().get(i).setRootNode(genotypes.get(i));
				// System.out.println("i = " + i);
			}
		}
		// full
		else if (_environment.getAttribute("initialization").equals("full")) {
			List<T> population = new ArrayList<T>();
			for (int p = index; p < _environment.getPopulationSize(); p++) {
				T individual = createNewIndividual();
				GpNode individualRoot = GpTreeManager.full(_environment);

				individual.setRootNode(individualRoot);
				population.add(individual);
			}
			_environment.setPopulation(population);
		} else
		// grow
		{
			List<T> population = new ArrayList<T>();
			for (int i = index; i < _environment.getPopulationSize(); i++) {
				T individual = createNewIndividual();
				GpNode individualRoot;
				individualRoot = GpTreeManager.grow(_environment);

				individual.setRootNode(individualRoot);
				population.add(individual);
			}
			_environment.setPopulation(population);
		}
	}

	/** creates a new individual using reflection */
	protected T createNewIndividual() {
		try {
			T newInstance = _individualClass.newInstance();
			return newInstance;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateGeneration() {
		long startTime = System.currentTimeMillis();
		updateGeneration_SGP();
		
		long endTime = System.currentTimeMillis();
		System.out.println("# update time = " + (endTime - startTime));
	}

	public void updateGeneration_SGP() {
		// reproduction
		Selector<T> selector = _environment.getSelector();
		selector.init(_environment.getPopulation(), _environment.getSelectionOrder());

		int phenotypeNum = _environment.getPopulationSize();
		List<T> nextPopulation = new ArrayList<T>(phenotypeNum);
		for (int i = 0; i < _environment.getEliteSize(); i++) {
			T elite = createNewIndividual();
			elite.setRootNode(bestIndividual.getRootNode());
			nextPopulation.add(elite);
		}
		for (; nextPopulation.size() < phenotypeNum;) {
			if (nextPopulation.size() < (phenotypeNum) * _environment.getCrossoverRatio()) {
				GpIndividual parentA = selector.getRandomPType();
				GpIndividual parentB = selector.getRandomPType();
				while (parentB == parentA) {
					parentB = (GpIndividual) selector.getRandomPType();
				}

				GpNode[] childrenTree;
				if (_environment.getAttribute("crossover") == null) {
					childrenTree = GpTreeManager.crossover(parentA.getRootNode(), parentB.getRootNode(), _environment);
				} else if (_environment.getAttribute("crossover").equals("normal")) {
					childrenTree = GpTreeManager.crossover(parentA.getRootNode(), parentB.getRootNode(), _environment);
				} else if (_environment.getAttribute("crossover").equals("depth-dependent")) {
					childrenTree = GpTreeManager.crossoverDepthDependent(parentA.getRootNode(), parentB.getRootNode(),
							_environment);
				} else if (_environment.getAttribute("crossover").equals("90/10")) {
					childrenTree = GpTreeManager.crossover90_10(parentA.getRootNode(), parentB.getRootNode(), _environment);
				} else if (_environment.getAttribute("crossover").equals("depth-fair")) {
					childrenTree = GpTreeManager.crossover(parentA.getRootNode(), parentB.getRootNode(), _environment);
				} else {
					childrenTree = GpTreeManager.crossover(parentA.getRootNode(), parentB.getRootNode(), _environment);
				}

				T childA = createNewIndividual();
				T childB = createNewIndividual();
				childA.setRootNode(childrenTree[0]);
				childB.setRootNode(childrenTree[1]);
				nextPopulation.add(childA);
				if (nextPopulation.size() + 1 < phenotypeNum) {
					nextPopulation.add(childB);
				}
			} else {
				T child = createNewIndividual();
				T parent = selector.getRandomPType();
				child.setRootNode(GpTreeManager.mutation(parent.getRootNode(), _environment));

				nextPopulation.add(child);
			}
		}
		_environment.setPopulation(nextPopulation);
	}

	public void finish() {
		super.finish();
	}
}
