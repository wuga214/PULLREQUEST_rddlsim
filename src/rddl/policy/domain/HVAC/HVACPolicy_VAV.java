package rddl.policy.domain.HVAC;

import java.util.ArrayList;
import java.util.Random;

import rddl.EvalException;
import rddl.RDDL;
import rddl.RDDL.LCONST;
import rddl.RDDL.PVARIABLE_DEF;
import rddl.RDDL.PVAR_INST_DEF;
import rddl.RDDL.PVAR_NAME;
import rddl.RDDL.TYPE_NAME;
import rddl.State;
import rddl.policy.Policy;
import util.Permutation;

public class HVACPolicy_VAV extends Policy{
	public int MAX_CONCURRENT_ACTIONS = 60;
	public HVACPolicy_VAV () { 
		super();
	}
	
	public HVACPolicy_VAV(String instance_name) {
		super(instance_name);
	}

	@Override
	public ArrayList<PVAR_INST_DEF> getActions(State s) throws EvalException {
		
		ArrayList<PVAR_INST_DEF> actions = new ArrayList<PVAR_INST_DEF>();
		if (s == null) return actions; 		
		int num_concurrent_actions = Math.min(MAX_CONCURRENT_ACTIONS, s._nMaxNondefActions);
		ArrayList<PVAR_NAME> action_types = s._hmTypeMap.get("action-fluent");	//Get the type of the action fluent
		boolean passed_constraints = false;
		
		for(int j =0; j< s._alActionNames.size(); j++){
			
			// Get a random action
			PVAR_NAME p = s._alActionNames.get(j);
			PVARIABLE_DEF pvar_def = s._hmPVariables.get(p);
			
			// Get term instantations for that action and select *one*
			ArrayList<ArrayList<LCONST>> inst = s.generateAtoms(p);
			//System.out.println("Legal instances for " + p + ": " + inst);
			int[] index_permutation = Permutation.permute(inst.size(), _random);
			
			for (int i = 0; i < index_permutation.length; i++) {
				ArrayList<LCONST> terms = inst.get(index_permutation[i]);
				
				RDDL.PVARIABLE_ACTION_DEF action_def = (RDDL.PVARIABLE_ACTION_DEF)pvar_def;
				
				Object value = null;
				//if (terms.get(0).toString().equals("$o")||terms.get(0).toString().equals("$h")) {
				if (!(Boolean)s.getPVariableAssign(new PVAR_NAME("IS_ROOM"), terms)) {
					
					continue;
				}  //I will change it to judge based on isroom();
				
				value = getReasonableValue(s, action_def._typeRange, terms);
				actions.add(new PVAR_INST_DEF(p._sPVarName, value, terms));
				try {
					s.checkStateActionConstraints(actions);
				} catch (EvalException e) { 

					actions.remove(actions.size()-1); 
				} catch (Exception e) { 
					System.out.println("\nERROR evaluating constraint on action set: " + 
							actions + /*"\nConstraint: " +*/ e + "\n");
					e.printStackTrace();
					throw new EvalException(e.toString());
				}
				if (actions.size() == num_concurrent_actions)
					break;
			}
			if(actions.size() == num_concurrent_actions)
				break;
		}

		if (actions.size() == 0) {
			actions.clear();
			try {
				s.checkStateActionConstraints(actions);
			} catch (EvalException e) {
				System.out.println(actions + " : " + e);
				throw new EvalException("No actions satisfied state constraints, not even noop!");
			}
		}
				
		// Return the action list
		System.out.println("**Action: " + actions);
		
		return actions;
	}
	
	private Object getReasonableValue(State s, TYPE_NAME _typeRange, ArrayList<LCONST> terms) throws EvalException {
		double temp_current = (Double) s.getPVariableAssign(new PVAR_NAME("TEMP"), terms);
		double temp_up = (Double) s.getPVariableAssign(new PVAR_NAME("TEMP_UP"), terms);
		double temp_low = (Double) s.getPVariableAssign(new PVAR_NAME("TEMP_LOW"), terms);
		double air_max =(Double) s.getPVariableAssign(new PVAR_NAME("AIR_MAX"), terms); 
		Double value = 0.0;
		Random random = new Random();
		double epsilon=0.3;
		if(temp_current<=temp_low){
			if(random.nextDouble()>epsilon){
				value = air_max;
			}else{
				value = 0.5*air_max+Math.random()*0.5*air_max;
			}
		}else if(temp_current>=temp_up){
			if(random.nextDouble()>epsilon){
				value = 0.0;
			}else{
				value = Math.random()*0.5*air_max;
			}
		} else
			value = Math.random()*air_max;
		return value;
	}
}
