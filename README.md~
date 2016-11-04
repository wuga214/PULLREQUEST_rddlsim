### Progress
1. Data Extraction from RDDL is functional
2. Domain Reservoir is function
3. Policy for Reservoir(Done)

### TODO
1. Domain Description for Inventory control and power gen

### New Helper Class
./run rddl.Help

### New DomainExplorer
./run rddl.sim.DomainExplorer

### New folder to accept data and label output for Neural Network training.

### RUN
Modified run to accept more than 13 parameters

RDDL Interface Improvement
===============================================

Updated parameter handling

```
*********************************************************
>>> Parameter Description
*********************************************************
[1]: -R: RDDL file or directory that contains RDDL file
[2]: -P: Policy name e.g. rddl.policy.RandomBoolPolicy
[3]: -I: Instance name e.g. elevators_inst_mdp__9
[4]: -V: Visualization Method e.g. rddl.viz.GenericScreenDisplay
[5]: -S: Random seed for simulator to sample output.
[6]: -X: Random seed for policy to take random actions.
[7]: -K: Number of rounds. Default:1
[8]: -D: Output file address for state-action pair
[9]: -L: Output file address for state label
*********************************************************
```

Examples:

1. ./run rddl.sim.Simulator -R files/Reservoir/Reservoir_det.rddl -P rddl.policy.domain.reservoir.StochasticReservoirPolicy -I is1 -V rddl.viz.GenericScreenDisplay

2. ./run rddl.sim.DomainExplorer -R files/Reservoir/Reservoir_det.rddl -P rddl.policy.domain.reservoir.StochasticReservoirPolicy -I is1 -V rddl.viz.ValueVectorDisplay
