package run.regression;

import geneticProgramming.GpEnvironment;
import geneticProgramming.GpSymbolSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import base.selector.Selector;
import base.selector.RouletteSelector;
import base.selector.TournamentSelector;
import run.regression.symbol.Cos;
import run.regression.symbol.Divide;
import run.regression.symbol.Exp;
import run.regression.symbol.IfLessThan;
import run.regression.symbol.Log;
import run.regression.symbol.Multiple;
import run.regression.symbol.One;
import run.regression.symbol.Plus;
import run.regression.symbol.Random;
import run.regression.symbol.Sin;
import run.regression.symbol.Minus;
import run.regression.symbol.Variable;

public class ExecutionManager {
	public static void main(String[] args) {
		if (args.length == 1) {

			File inputFile = new File(args[0]);
			if (inputFile.exists()) {
				Map<String, Object> config = readConfig(inputFile);
				execute(config);
			} else {
				System.out.println(args[0] + " not found");
			}
			return;
		}
		System.out.println("Input file must be specified to execute.");
		return;
	}

	public static void execute(Map<String, Object> config) {
		// creates environment
		System.out.println(config);
		if (!config.containsKey("#VARIABLES")) {
			System.out.println("#VARIABLES must be set.");
			return;
		}

		GpSymbolSet symbolSet = new GpSymbolSet();
		int count = 0;
		for (String variable : ((String) config.get("#VARIABLES")).split(",")) {
			symbolSet.addSymbol(new Variable(count++), variable, 0);
		}

		for (String function : ((String) config.get("#FUNCTIONS")).split(",")) {
			function = function.toUpperCase();

			if (function.equals("PLUS")) {
				symbolSet.addSymbol(new Plus(), "+", 2);
			} else if (function.equals("MINUS")) {
				symbolSet.addSymbol(new Minus(), "-", 2);
			} else if (function.equals("MULTIPLE")) {
				symbolSet.addSymbol(new Multiple(), "*", 2);
			} else if (function.equals("DIVIDE")) {
				symbolSet.addSymbol(new Divide(), "/", 2);
			} else if (function.equals("EXP")) {
				symbolSet.addSymbol(new Exp(), "exp", 1);
			} else if (function.equals("LOG")) {
				symbolSet.addSymbol(new Log(), "log", 1);
			} else if (function.equals("SIN")) {
				symbolSet.addSymbol(new Sin(), "sin", 1);
			} else if (function.equals("COS")) {
				symbolSet.addSymbol(new Cos(), "cos", 1);
			} else if (function.equals("IFLT")) {
				symbolSet.addSymbol(new IfLessThan(), "lf_less_then", 4);
			} else if (function.equals("RANDOM")) {
				symbolSet.addSymbol(new Random(), "R", 0);
			} else if (function.equals("ONE")) {
				symbolSet.addSymbol(new One(), "1", 0);
			}
		}

		GpEnvironment<RegressionIndividual> environment = new GpEnvironment<RegressionIndividual>();
		// set default parameters
		environment.setRepetitionNumber(30);
		environment.setPopulationSize(1000);
		environment.setCrossoverRatio(0.9);
		environment.setMutationRatio(0.1);
		environment.setSelector(new TournamentSelector<>(environment.getPopulation(), 4));
		environment.setSelectionOrder(Selector.NORMAL);
		environment.setEliteSize(1);
		environment.setNumberOfMaxInitialDepth(3);
		environment.setNumberOfMaxDepth(10);

		double sizePenalty = 0.0;
		for (Entry<String, Object> entry : config.entrySet()) {
			String key = entry.getKey().toUpperCase();

			if (key.equals("#POPULATION_SIZE")) {
				environment.setPopulationSize(Integer.valueOf((String) entry.getValue()));
			} else if (key.equals("#GENERATION")) {
				environment.setRepetitionNumber(Integer.valueOf((String) entry.getValue()));
			} else if (key.equals("#ELITE")) {
				environment.setEliteSize(Integer.valueOf((String) entry.getValue()));
			} else if (key.equals("#INITIAL_DEPTH")) {
				environment.setNumberOfMaxInitialDepth(Integer.valueOf((String) entry.getValue()));
			} else if (key.equals("#DEPTH")) {
				environment.setNumberOfMaxDepth(Integer.valueOf((String) entry.getValue()));
			} else if (key.equals("#SELECTION_ORDER")) {
				environment.setSelectionOrder(((String) entry.getValue()).toUpperCase().equals("NORMAL"));
			} else if (key.equals("#SELECTION")) {
				String selection = ((String) entry.getValue()).toUpperCase();
				if (selection.equals("ROULETTE")) {
					environment.setSelector(new RouletteSelector<>(environment.getPopulation()));
				} else if (selection.equals("TRUNCATE")) {
					environment.setSelector(new RouletteSelector<>(environment.getPopulation()));
				} else {
					environment.setSelector(new TournamentSelector<>(environment.getPopulation(), 2));
				}
			} else if (key.equals("#TOURNAMENT_SIZE")) {
				if (environment.getSelector() instanceof TournamentSelector) {
					((TournamentSelector<RegressionIndividual>) environment.getSelector()).setTournamentSize(Integer
							.valueOf((String) entry.getValue()));
				}
			} else if (key.equals("#SIZE_PENALTY")) {
				sizePenalty = Double.valueOf((String) entry.getValue());
			}
		}
		environment.setSymbolSet(symbolSet);

		RegressionEvolutionModel model = new RegressionEvolutionModel();
		model.initGP(environment, RegressionIndividual.class);
		model.setCorrectData((List<Double[][]>) config.get("#TRAINING_DATA"));
		model.setSizePenalty(sizePenalty);
		model.run();
	}

	public static Map<String, Object> readConfig(File inputFile) {
		Map<String, Object> config = new HashMap<>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			while (reader.ready()) {
				String line = reader.readLine();

				if (line.startsWith("#") && line.contains("=")) {
					String key = line.split("=")[0].trim().toUpperCase();
					Object value = line.split("=")[1].trim();

					config.put(key, value);
				}

				if (line.toUpperCase().startsWith("#TRAINING_DATA")) {
					config.put("#TRAINING_DATA", readTrainingData(reader));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}

	public static List<Double[][]> readTrainingData(BufferedReader reader) throws IOException {
		List<Double[][]> data = new ArrayList<>();
		while (reader.ready()) {
			String line = reader.readLine(); // [x:4, y:10 = 423]
			if (line.startsWith("#END")) {
				break;
			}

			List<Double> conditions = new ArrayList<>();
			for (String condition : line.split("=")[0].replace("(", "").replace(")", "").split(",")) {
				conditions.add(Double.valueOf(condition));
			}

			String result = line.split("=")[1]; // 423

			Double[][] row = new Double[2][];
			row[0] = conditions.toArray(new Double[0]);
			row[1] = new Double[] { Double.valueOf(result) };
			data.add(row);
		}

		return data;
	}
}
