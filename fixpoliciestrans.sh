#!/bin/sh

./run rddl.sim.DomainExplorer -R files/Reservoir/Reservoir_4/Reservoir.rddl -P rddl.policy.domain.reservoir.FixReservoirPolicy -I is1 -V rddl.viz.ValueVectorDisplay -K 1 -D logs/Reservoir_4_Data.txt -L logs/Reservoir_4_Label.txt

./run rddl.sim.DomainExplorer -R files/Reservoir/Reservoir_3/Reservoir.rddl -P rddl.policy.domain.reservoir.FixReservoirPolicy -I is1 -V rddl.viz.ValueVectorDisplay -K 1 -D logs/Reservoir_3_Data.txt -L logs/Reservoir_3_Label.txt

./run rddl.sim.DomainExplorer -R files/HVAC/ROOM_6/HVAC_VAV.rddl2 -P rddl.policy.domain.HVAC.HVACPolicy_VAV_det -I inst_hvac_vav_fix -V rddl.viz.ValueVectorDisplay -K 1 -D logs/HVAC_6_Data.txt -L logs/HVAC_6_Label.txt

./run rddl.sim.DomainExplorer -R files/HVAC/ROOM_3/HVAC_VAV.rddl2 -P rddl.policy.domain.HVAC.HVACPolicy_VAV_det -I inst_hvac_vav_fix -V rddl.viz.ValueVectorDisplay -K 1 -D logs/HVAC_3_Data.txt -L logs/HVAC_3_Label.txt
