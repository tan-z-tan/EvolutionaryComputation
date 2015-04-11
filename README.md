## EvolutionaryComputation

Java implementation of Evolutionary Computation (EC).


## Sample usage

java -jar sample/SymbolicRegression.jar src/run/regression/problem1

# Implement your own GP solver
by defining *GPEvolutionModel* and *GPIndividual* classes.


### GPEvolutionaModel class
You should implement your model class which inherits *GPEvolutionModel*, override problem specific methods like *evaluateIndividual* and *initialize*.
These classes are supposed to use generics. Set your generic class type; model class and individual class.

```
public class RegressionEvolutionModel extends GPEvolutionModel<RegressionIndividual, GpEnvironment<RegressionIndividual>> {

...
  public void evaluateIndividual(RegressionIndividual individual) {

```

### Individual class

Define an GPIndividual class to describe your problem.
You may want to implement own GP symbol class.
e.g. For SymbolicRegression problem, an individual is evaluated by absolute error between individual's output and correct answer. Basic arithmetica operations are defined ().

```
public class RegressionIndividual extends GpIndividual

```

```
public class Plus extends SymbolType {
  @Override
  public Object evaluate(GpNode node, Object obj) {
    return (Double) node.getChild(0).evaluate(obj) + (Double) node.getChild(1).evaluate(obj);
  }
}

```

### Run

Set parameters related to GP, and let your model run.

```
Environment environment = env; // you can set parameters in this object
RegressionEvolutionModel model = new RegressionEvolutionModel(env, parameters);
model.run();
```

### License
Makoto Tanji, 2015
Any feedbacks are welcomed.
tanji.makoto@gmail.com
Distributed under GPL license.
