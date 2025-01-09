package ast;

import java.util.ArrayList;

public class NodeProgram {
	private ArrayList<NodeDecSt> decSts ;
	
	public NodeProgram(ArrayList<NodeDecSt> decSts) {
		this.decSts = decSts;
	}
	
	public ArrayList<NodeDecSt> getDecSts() {
		return decSts;
	}
	
	@Override
	public String toString() {
		return "NodeProgramm -> [DecSts=" + decSts + "];\t";
	}
	
	
}
