### Progress
1. Data Extraction from RDDL is functional(Done)
2. Domain Reservoir description(Done)
3. Policy for Reservoir(Done)
4. Domain Determinization(Done)
5. store PVAR+TERM to output file(Done)
6. Bug free testing(Done)

### TODO
1. Domain Description for Inventory control and power gen

### New folder to accept data and label output for Neural Network training.

### RUN
Modified run to accept more than 13 parameters

RDDL Interface Improvement
===============================================

## Updated parameter handling

Note: Anytime running code without proper parameters, a helper message will show.
```
Mac-mini:rddlsim Wuga$ ./run rddl.viz.RDDL2Graph

*********************************************************
>>> Parameter Description
*********************************************************
[1]: -R: RDDL file or directory that contains RDDL file
[2]: -P: Policy name e.g. rddl.policy.RandomBoolPolicy
[3]: -D: Directed, 'true' or 'false'
[4]: -L: Strict-levels, 'true' or 'false'
[5]: -G: Strict-grouping, 'true' or 'false'
*********************************************************
```

## Examples

### Simulator and DomainExplorer

1. ./run rddl.sim.Simulator -R files/Reservoir/Reservoir_det.rddl -P rddl.policy.domain.reservoir.StochasticReservoirPolicy -I is1 -V rddl.viz.GenericScreenDisplay
2. ./run rddl.sim.DomainExplorer -R files/Reservoir/Reservoir_det.rddl -P rddl.policy.domain.reservoir.StochasticReservoirPolicy -I is1 -V rddl.viz.ValueVectorDisplay

### RDDL to Bayesian Net Graph
1. ./run rddl.viz.RDDL2Graph -R files/Reservoir/Reservoir_det.rddl -I is1
2. ./run rddl.viz.RDDL2Graph -R files/rddl/examples/dbn_prop.rddl -I inst_dbn

## Added Functions
1. State.getPossibleTerms(...): Used to find most possible terms given partial fluents known or true value known.
2. Help class: Used to store all command helping information. This will be extended to give instruction for public interfaces later.
3. DotFileFixer: Used to fix *.dot files so that old package like grappa can work with new version of Graphviz

RDDL Error Fixing
===============================================
1. Fixed a bug that cause Bayesian Network Graph couldn't be properly shown in Java.


