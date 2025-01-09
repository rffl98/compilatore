package ast;

public class NodeId {
	private String name;
	
	public NodeId(String name) {
		this.name = name;
	}
	public String getN() {
		return name;
	}

	
	@Override
	public String toString() {
		return "NodeId -> [names=" + name + "]; \t";
	}

	public TypeDescriptor calcResType(){

		return null;
	}

    public String calcCodice(){

		return null;
	}
}