package ast;


import symbolTable.IVisitor;

public class NodeBinOp extends NodeExpr {
	
	private LangOper op;
	private NodeExpr left;
	private NodeExpr right;
	
	public NodeBinOp(LangOper op, NodeExpr left, NodeExpr right) {
		this.op = op;
		this.left = left;
		this.right = right;
	}

	public LangOper getOp() {
		return op;
	}

	public NodeExpr getLeft() {
		return left;
	}

	public NodeExpr getRight() {
		return right;
	}

	@Override
	public String toString() {
		return "NodeBinOp -> [op=" + op + ", left=" + left + ", right=" + right + "];\t";
	}

	@Override
	public TypeDescriptor calcResType() {
		
		TypeDescriptor leftTD = left.calcResType();//descrittore di tipo della espressione sinistra
		TypeDescriptor rightTD = right.calcResType();//descrittore di tipo della espressione destra
		
		if ( leftTD == TypeDescriptor.INT && rightTD == TypeDescriptor.INT ) //controlli opportuni su leftTD e rightTD
			return TypeDescriptor.INT;
		if ( (leftTD == TypeDescriptor.INT && rightTD == TypeDescriptor.FLOAT) ||  (leftTD == TypeDescriptor.FLOAT && rightTD == TypeDescriptor.FLOAT)  || (leftTD == TypeDescriptor.FLOAT && rightTD == TypeDescriptor.INT) )
			return TypeDescriptor.FLOAT;
		if ( (leftTD == TypeDescriptor.ERROR || rightTD == TypeDescriptor.ERROR)    ){
			StringBuilder errorMessage = new StringBuilder() ;
				if (leftTD == TypeDescriptor.ERROR) {
					errorMessage.append("Errore da leftTD") ;
				}
				if (rightTD == TypeDescriptor.ERROR) {
					errorMessage.append(" - Errore da rightTD") ;
				}
				//throw new LexicalException(errorMessage);
				return TypeDescriptor.ERROR;
			}
		return TypeDescriptor.ERROR;
		//ritorna il TypeDescriptor appropriato (se entrambi errori concatena msg)}
}

	public void accept(IVisitor visitor){
		
	}

	@Override
	public String calcCodice() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'calcCodice'");
	}

}

