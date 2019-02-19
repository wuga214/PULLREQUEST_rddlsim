package rddl.policy.domain.navigation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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

public class TensorflowPolicy extends Policy{
	public int Horizon = 20;
	public int MAX_CONCURRENT_ACTIONS = 20;
	public String Python_Repo = "/media/wuga/Storage/JAIR-18/";
	public TensorflowPolicy () { 
		super();
	}
	
	public TensorflowPolicy(String instance_name) {
		super(instance_name);
	}

	@Override
	public ArrayList<PVAR_INST_DEF> getActions(State s) throws EvalException {
		// TODO Auto-generated method stub		ArrayList<PVAR_INST_DEF> actions = new ArrayList<PVAR_INST_DEF>();
		ArrayList<PVAR_INST_DEF> actions = new ArrayList<PVAR_INST_DEF>();
		if (s == null) return actions; 		
		int num_concurrent_actions = Math.min(MAX_CONCURRENT_ACTIONS, s._nMaxNondefActions);
		ArrayList<PVAR_NAME> action_types = s._hmTypeMap.get("action-fluent");
		boolean passed_constraints = false;
		for(int j =0; j< s._alActionNames.size(); j++){
			
			// Get a random action
			PVAR_NAME p = s._alActionNames.get(j);
			PVARIABLE_DEF pvar_def = s._hmPVariables.get(p);
			
			// Get term instantations for that action and select *one*
			ArrayList<ArrayList<LCONST>> inst = s.generateAtoms(p);
			System.out.println("Legal instances for " + p + ": " + inst);
			String[] current_state = new String[inst.size()];
			
			for (int i = 0; i < inst.size(); i++) {
				ArrayList<LCONST> terms = inst.get(i);
				current_state[i] = String.valueOf((Double) s.getPVariableAssign(new PVAR_NAME("location"), terms));
			}
			
			String outFile = Python_Repo + "temp/state.csv";
			String inFile = Python_Repo + "temp/action.csv";
			
			double[] action_values = getActionFromPython(current_state, outFile, inFile);
			System.out.println(action_values);
			
			for (int i=0; i < inst.size(); i++) {
				ArrayList<LCONST> terms = inst.get(i);
				actions.add(new PVAR_INST_DEF(p._sPVarName, action_values[i], terms));
			}
		}
		Horizon = Horizon - 1;
		return actions;
	}
	
	public double[] getActionFromPython(final String[] current_state, final String outFile, final String inFile) {
		try (BufferedWriter writer = new BufferedWriter(
				new PrintWriter(outFile, "UTF-8"))) {
			writer.write(String.join(",", current_state));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long start = System.currentTimeMillis();
		runPythonCommand();
		long end = System.currentTimeMillis();

		System.out.println("Took : " + ((end - start) / 1000.0));
		
		try (BufferedReader reader = new BufferedReader(
				new FileReader(inFile))) {
			String line = reader.readLine();
			if(line!=null) {
				String[] split = line.split(",");
				double[] values = new double[split.length];
				for (int i = 0; i < split.length; i++) {
					values[i] = Double.parseDouble(split[i]);
				}
				return values;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void runPythonCommand() {

		Process process = null;
		try {
//			10x10
//			String command = String.format(
//					"python %s -w %s -l %s -d %s -i %s -hz %s -a %s -s %s -e %s --constraint %s %s --get_state %s --send_action %s",
//					Python_Repo + "plan.py",
//					Python_Repo + "weights/nav/10x10",
//					"2",
//					"Navigation",
//					"Navigation10",
//					Horizon,
//					"2",
//					"2",
//					"300",
//					"-1",
//					"1",
//					Python_Repo + "temp/state",
//					Python_Repo + "temp/action");
			
			String command = String.format(
			"python %s -w %s -l %s -d %s -i %s -hz %s -a %s -s %s -e %s --constraint %s %s --get_state %s --send_action %s",
			Python_Repo + "plan.py",
			Python_Repo + "weights/nav/10x10",
			"2",
			"Navigation",
			"Navigation10",
			Horizon,
			"2",
			"2",
			"400",
			"-0.5",
			"0.5",
			Python_Repo + "temp/state",
			Python_Repo + "temp/action");
			
//			8x8
//			String command = String.format(
//					"python %s -w %s -l %s -d %s -i %s -hz %s -a %s -s %s --constraint %s %s --get_state %s --send_action %s",
//					Python_Repo + "plan.py",
//					Python_Repo + "weights/nav/8x8",
//					"2",
//					"Navigation",
//					"Navigation8",
//					Horizon,
//					"2",
//					"2",
//					"-1",
//					"1",
//					Python_Repo + "temp/state",
//					Python_Repo + "temp/action");
				
			System.out.println(command);

			process = Runtime.getRuntime()
					.exec(new String[] { "bash", "-c", command });
			process.waitFor();
			
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (process != null) {
				process.destroy();
			}
		}
	}

}