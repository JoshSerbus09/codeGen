import java.io.*;
import java.util.*;

// **********************************************************************
// The ASTnode class defines the nodes of the abstract-syntax tree that
// represents a Mini program.
//
// Internal nodes of the tree contain pointers to children, organized
// either in a list (for nodes that may have a variable number of 
// children) or as a fixed set of fields.
//
// The nodes for literals and ids contain line and character number
// information; for string literals and identifiers, they also contain a
// string; for integer literals, they also contain an integer value.
//
// Here are all the different kinds of AST nodes and what kinds of children
// they have.  All of these kinds of AST nodes are subclasses of "ASTnode".
// Indentation indicates further subclassing:
//
//     Subclass            Kids
//     --------            ----
//     ProgramNode         DeclListNode
//     DeclListNode        linked list of DeclNode
//     DeclNode:
//       VarDeclNode       TypeNode, IdNode, int
//       FnDeclNode        TypeNode, IdNode, FormalsListNode, FnBodyNode
//       FormalDeclNode    TypeNode, IdNode
//
//     FormalsListNode     linked list of FormalDeclNode
//     FnBodyNode          DeclListNode, StmtListNode
//     StmtListNode        linked list of StmtNode
//     ExpListNode         linked list of ExpNode
//
//     TypeNode:
//       IntNode           -- none --
//       BoolNode          -- none --
//       VoidNode          -- none --
//
//     StmtNode:
//       ReadStmtNode        ExpNode
//       WriteStmtNode       ExpNode
//       AssignStmtNode      ExpNode, ExpNode
//       IfStmtNode          ExpNode, DeclListNode, StmtListNode
//       IfElseStmtNode      ExpNode, DeclListNode, StmtListNode,
//                                    DeclListNode, StmtListNode
//       WhileStmtNode       ExpNode, DeclListNode, StmtListNode
//       CallStmtNode        CallExpNode
//       ReturnStmtNode      ExpNode
//
//     ExpNode:
//       IntLitNode          -- none --
//       StrLitNode          -- none --
//       TrueNode            -- none --
//       FalseNode           -- none --
//       IdNode              -- none --
//       ArrayExpNode        IdNode, ExpNode
//       CallExpNode         IdNode, ExpListNode
//       UnaryExpNode        ExpNode
//         UnaryMinusNode
//         NotNode
//       BinaryExpNode       ExpNode ExpNode
//         PlusNode     
//         MinusNode
//         TimesNode
//         DivideNode
//         AndNode
//         OrNode
//         EqualsNode
//         NotEqualsNode
//         LessNode
//         GreaterNode
//         LessEqNode
//         GreaterEqNode
//
// Here are the different kinds of AST nodes again, organized according to
// whether they are leaves, internal nodes with linked lists of kids, or 
// internal nodes with a fixed number of kids:
//
// (1) Leaf nodes:
//        IntNode,   BoolNode,  VoidNode,  IntLitNode,  StrLitNode,
//        TrueNode,  FalseNode, IdNode
//
// (2) Internal nodes with (possibly empty) linked lists of children:
//        DeclListNode, FormalsListNode, StmtListNode, ExpListNode
//
// (3) Internal nodes with fixed numbers of kids:
//        ProgramNode,    VarDeclNode,    FnDeclNode,     FormalDeclNode,
//        FnBodyNode,     TypeNode,       ReadStmtNode,   WriteStmtNode
//        AssignStmtNode, IfStmtNode,     IfElseStmtNode, WhileStmtNode,
//        CallStmtNode,   ReturnStmtNode, ArrayExpNode,   CallExpNode,
//        UnaryExpNode,   BinaryExpNode,  UnaryMinusNode, NotNode,
//        PlusNode,       MinusNode,      TimesNode,      DivideNode,
//        AndNode,        OrNode,         EqualsNode,     NotEqualsNode,
//        LessNode,       GreaterNode,    LessEqNode,     GreaterEqNode  
//
// **********************************************************************

// **********************************************************************
// ASTnode class (base class for all other kinds of nodes)
// **********************************************************************

abstract class ASTnode {
	public static final boolean DEBUG = true;

	// every subclass must provide an unparse operation
	abstract public void unparse(PrintWriter p, int indent);
	public static boolean foundMain = false;
	public static RegPool pool = null;
	public static int lastSeenParamsCount = 0;
	public static int currLexLv = 0;
	public static String lastSeenFnExit;

	// this method can be used by the unparse methods to do indenting
	protected void doIndent(PrintWriter p, int indent) {
		for (int k=0; k<indent; k++) p.print(" ");
	}
}

// **********************************************************************
// ProgramNode,  DeclListNode, FormalsListNode, FnBodyNode,
// StmtListNode, ExpListNode
// **********************************************************************

class ProgramNode extends ASTnode {
	public ProgramNode(DeclListNode L) {
		myDeclList = L;
	}

	/** processNames
	 *
	 * create an empty symbol table for the outermost scope, then
	 * process all of the globals and functions in the program
	 **/
	public void processNames() {
		SymbolTable S = new SymbolTable();
		myDeclList.processNames(S);
	}

	/** typeCheck **/
	public void typeCheck() {
		myDeclList.typeCheck();
	}

	public void unparse(PrintWriter p, int indent) {
		myDeclList.unparse(p, indent);
	}

	public void codeGen(){
		myDeclList.codeGen();
	}

	// 1 kid
	private DeclListNode myDeclList;
}

class DeclListNode extends ASTnode {
	public DeclListNode(List<DeclNode> S) {
		myDecls = S;
	}

	/** processNames
	 *
	 * given: a symbol table S
	 * do:    process all of the decls in the list
	 **/
	public void processNames(SymbolTable S) {
		try {
			for (DeclNode node : myDecls) {
				node.processNames(S);
			}
		} catch (NoSuchElementException ex) {
			System.err.println("unexpected NoSuchElementException in DeclListNode.processNames");
			System.exit(-1);
		}

		for(int i = 0; i < myDecls.size(); i++){
			try{	
				VarDeclNode node = (VarDeclNode)myDecls.get(i);			
				node.setOffset(-4 * (lastSeenParamsCount + 2 + i ));

			} catch (ClassCastException ce) {
				// This means the DeclNode is a FnDeclNode, we don't want to calculate offset for functions
			}
		}


	}

	/** typeCheck **/
	public void typeCheck() {
		try {
			for (DeclNode node : myDecls) {
				node.typeCheck();
			}
		} catch (NoSuchElementException ex) {
			System.err.println("unexpected NoSuchElementException in DeclListNode.typeCheck");
			System.exit(-1);
		}
	}

	public void unparse(PrintWriter p, int indent) {
		Iterator it = myDecls.iterator();
		try {
			while (it.hasNext()) {
				((DeclNode)it.next()).unparse(p, indent);
			}
		} catch (NoSuchElementException ex) {
			System.err.println("unexpected NoSuchElementException in DeclListNode.unparse");
			System.exit(-1);
		}
	}

	/** get number of variables declared **/
	public int count(){
		return myDecls.size();
	}

	public void codeGen(){
		for(DeclNode node : myDecls){
			if(DEBUG){Codegen.genText("# This is one child in DeclList Node#220");}
			node.codeGen();
			if(DEBUG){Codegen.genText("# This is THE END of one child in DeclList Node#220");}
		}
	}

	// list of kids (DeclNodes)
	private List<DeclNode> myDecls;
}

class FormalsListNode extends ASTnode {
	public FormalsListNode(List<FormalDeclNode> S) {
		myFormals = S;
	}

	/** processNames
	 *
	 * given: a symbol table S
	 * do:    process all of the formals in the list
	 **/
	public List<Type> processNames(SymbolTable S) {
		List<Type> L = new LinkedList<Type>();
		try {
			for (FormalDeclNode node : myFormals) {
				Sym sym = node.processNames(S);
				if (sym != null) L.add(sym.type());
			}
		} catch (NoSuchElementException ex) {
			System.err.println("unexpected NoSuchElementException in FormalsListNode.processNames");
			System.exit(-1);
		}
		
		try{
			for(int i = 0; i < myFormals.size(); i++){
				FormalDeclNode node = myFormals.get(i);
				
				node.setOffset(-4*i);
			}
			
		} catch (Exception e){
			
		}
		return L;
	}

	/** length **/
	public int length() {
		return myFormals.size();
	}

	public void unparse(PrintWriter p, int indent) {
		Iterator it = myFormals.iterator();
		try {
			while (it.hasNext()) {
				((FormalDeclNode)it.next()).unparse(p, indent);
				if (it.hasNext()) {
					p.print(", ");
				}
			}
		} catch (NoSuchElementException ex) {
			System.err.println("unexpected NoSuchElementException in FormalsListNode.unparse");
			System.exit(-1);
		}
	}

	// list of kids (FormalDeclNodes)
	private List<FormalDeclNode> myFormals;
}

class FnBodyNode extends ASTnode {
	public FnBodyNode(DeclListNode declList, StmtListNode stmtList) {
		myDeclList = declList;
		myStmtList = stmtList;
	}

	/** processNames **/
	public void processNames(SymbolTable S) {
		myDeclList.processNames(S);
		myStmtList.processNames(S);
	}

	/** typeCheck **/
	public void typeCheck(Type T) {
		myStmtList.typeCheck(T);
	}

	public void unparse(PrintWriter p, int indent) {
		if (myDeclList != null) myDeclList.unparse(p, indent+2);
		if (myStmtList != null) myStmtList.unparse(p, indent+2);
	}

	public int numLocals(){
		return myDeclList.count();
	}

	public void codeGen(){
		myStmtList.codeGen();
	}

	// 2 kids
	private DeclListNode myDeclList;
	private StmtListNode myStmtList;
}

class StmtListNode extends ASTnode {
	public StmtListNode(List<StmtNode> S) {
		myStmts = S;
	}

	/** processNames **/
	public void processNames(SymbolTable S) {
		try {
			for (StmtNode node : myStmts) {
				node.processNames(S);
			}
		} catch (NoSuchElementException ex) {
			System.err.println("unexpected NoSuchElementException in StmtListNode.processNames");
			System.exit(-1);
		}
	}

	/** typeCheck **/
	public void typeCheck(Type T) {
		try {
			for (StmtNode node : myStmts) {
				node.typeCheck(T);
			}
		} catch (NoSuchElementException ex) {
			System.err.println("unexpected NoSuchElementException in StmtListNode.processNames");
			System.exit(-1);
		}
	}

	public void unparse(PrintWriter p, int indent) {
		// indent for each stmt is done here
		// each stmt is expected to end with a newline
		Iterator it = myStmts.iterator();
		try {
			while (it.hasNext()) {
				doIndent(p, indent);
				((StmtNode)it.next()).unparse(p, indent);
			}
		} catch (NoSuchElementException ex) {
			System.err.println("unexpected NoSuchElementException in StmtListNode.unparse");
			System.exit(-1);
		}
	}

	public void codeGen(){
		for(StmtNode stmt : myStmts){
			stmt.codeGen();
		}
	}

	// list of kids (StmtNodes)
	private List<StmtNode> myStmts;
}

class ExpListNode extends ASTnode {
	public ExpListNode(List<ExpNode> S) {
		myExps = S;
	}

	/** processNames **/
	public void processNames(SymbolTable S) {
		try {
			for (ExpNode node : myExps) {
				node.processNames(S);
			}
		} catch (NoSuchElementException ex) {
			System.err.println("unexpected NoSuchElementException in ExpListNode.processNames");
			System.exit(-1);
		}
	}

	/** length **/
	public int length() {
		return myExps.size();
	}

	/** typeCheck **/
	public void typeCheck(List<Type> L) {
		int k=0;
		try {
			for (ExpNode node : myExps) {
				Type actualT = node.typeCheck();
				if (!actualT.isErrorType()) {
					Type paramT = L.get(k);
					if (!paramT.equals(actualT)) {
						Errors.fatal(node.linenum(), node.charnum(),
						"Type of actual does not match type of formal");
					}
				}
				k++;
			}
		} catch (NoSuchElementException ex) {
			System.err.println("unexpected NoSuchElementException in ExpListNode.processNames");
			System.exit(-1);
		}
	}

	public void unparse(PrintWriter p, int indent) {
		Iterator it = myExps.iterator();
		try {
			while (it.hasNext()) {
				((ExpNode)it.next()).unparse(p, 0);
				if (it.hasNext()) {
					p.print(", ");
				}
			}
		} catch (NoSuchElementException ex) {
			System.err.println("unexpected NoSuchElementException in ExpListNode.unparse");
			System.exit(-1);
		}
	}

	public void codeGen(){
		for(ExpNode exp: myExps){
			exp.codeGen();
		}
	}

	// list of kids (ExpNodes)
	private List<ExpNode> myExps;
}

// **********************************************************************
// DeclNode and its subclasses
// **********************************************************************

abstract class DeclNode extends ASTnode {
	// note: only a formal decl needs to return a Sym
	//       but since we must declare the method here,
	//       we make all decl nodes return something
	//       (for non formal decls, the returned value
	//       is simply ignored)
	abstract public Sym processNames(SymbolTable S);
	abstract public void codeGen();

	// default version of typeCheck for var and formal decls
	public void typeCheck() {
	}
}

class VarDeclNode extends DeclNode {
	public VarDeclNode(TypeNode type, IdNode id, int size) {
		myType = type;
		myId = id;
		mySize = size;
	}

	/** processNames
	 *
	 * given: a symbol table
	 * do:    if this name is declared void, error!
	 *        else if this name has already been declared in this scope
	 *             then error
	 *             else add name to local symbol table
	 **/
	public Sym processNames(SymbolTable S) {
		String name = myId.name();
		boolean badDecl = false;
		if (myType.type(NOT_ARRAY).isVoidType()) {
			Errors.fatal(myId.linenum(), myId.charnum(),
			"Non-function declared void");
			badDecl = true;
		}

		if (S.localLookup(name) != null) {
			Errors.fatal(myId.linenum(), myId.charnum(),
			"Multiply declared identifier");
			badDecl = true;
		}

		if (! badDecl) {
			try {
				Sym sym = new Sym(myType.type(mySize), S.size() - 1);
				S.insert(name, sym);
				myId.link(sym);
			} catch (DuplicateException ex) {
				System.err.println("unexpected DuplicateException in VarDeclNode.processNames");
				System.exit(-1);
			} catch (EmptySymbolTableException ex) {
				System.err.println("unexpected EmptySymbolTableException in VarDeclNode.processNames");
				System.exit(-1);
			}
		}

		return null;  // return value ignored
	}

	public void unparse(PrintWriter p, int indent) {
		doIndent(p, indent);
		myType.unparse(p, 0);
		p.print(" ");
		p.print(myId.name());
		//myId.unparse(p, 0);
		if (mySize != NOT_ARRAY) {
			p.print("[" + mySize + "]");
		}
		p.println(";");
	}

	public void setOffset(int offset){
		Sym sym = myId.sym();
		sym.offset = offset;
	}

	public void codeGen(){
		if(currLexLv == 0){

			if(DEBUG){Codegen.genText("# <GlobalVariable>");}
			Codegen.generate(".data");
			Codegen.generate(".align 2");
			Codegen.genLabel("_" + myId.name());
			Codegen.generate(".space 4");
			if(DEBUG){Codegen.genText("# </GlobalVariable>\n");}

		}
	}

	// 3 kids
	private TypeNode myType;
	private IdNode myId;
	private int mySize;  // use value NOT_ARRAY if this is not an array type

	public static int NOT_ARRAY = -1;
}

class FnDeclNode extends DeclNode {
	public FnDeclNode(TypeNode type,
			IdNode id,
			FormalsListNode formalList,
			FnBodyNode body) {
		if(id.name().equals("main")){
			foundMain = true;
		}
		myType = type;
		myId = id;
		myFormalsList = formalList;
		myBody = body;
	}

	/** processNames
	 *
	 * given: a symbol table S
	 * do:    If this name has already been declared in this scope
	 *        then error
	 *        else add name to local symbol table.
	 *        In any case:
	 *             enter new scope
	 *             process formals
	 *             if this fn not multiply decld
	 *                update SymbolTable entry with types of formals
	 *             process body
	 *             exit scope
	 **/
	public Sym processNames(SymbolTable S) {
		String name = myId.name();
		FnSym sym = null;

		// get number of local variables
		int lSize = myBody.numLocals();
		
		lastSeenParamsCount = myFormalsList.length();

		if (S.localLookup(name) != null) {
			Errors.fatal(myId.linenum(), myId.charnum(),
			"Multiply declared identifier");
		}

		else {
			try {
				// create new symbol containing name, numParams, numLocalVariables, current nesting level 
				sym = new FnSym(myType.type(VarDeclNode.NOT_ARRAY),
						myFormalsList.length(), lSize, S.size());
				S.insert(name, sym);
				myId.link(sym);

			} catch (DuplicateException ex) {
				System.err.println("unexpected DuplicateException in FnDeclNode.processNames");
				System.exit(-1);

			} catch (EmptySymbolTableException ex) {
				System.err.println("unexpected EmptySymbolTableException in FnDeclNode.processNames");
				System.exit(-1);
			}
		}

		S.addMap();
		List<Type> L = myFormalsList.processNames(S);
		if (sym != null) sym.addFormals(L);

		lastSeenParamsCount = myFormalsList.length();
		myBody.processNames(S);
		try {
			S.removeMap();
		} catch (EmptySymbolTableException ex) {
			System.err.println("unexpected EmptySymbolTableException in FnDeclNode.processNames");
			System.exit(-1);
		}
		return null;
	}

	/** typeCheck **/
	public void typeCheck() {
		myBody.typeCheck(myType.type(VarDeclNode.NOT_ARRAY));
	}

	public void unparse(PrintWriter p, int indent) {
		p.println();
		doIndent(p, indent);
		myType.unparse(p, 0);
		p.print(" ");
		p.print(myId.name());
		//myId.unparse(p, 0);
		p.print("(");
		if (myFormalsList != null) myFormalsList.unparse(p, 0);
		p.println(") {");
		if (myBody != null) myBody.unparse(p, indent);
		doIndent(p, indent);
		p.println("}");
	}


	public void codeGen(){
		if(DEBUG){Codegen.genText("# <FnDeclNode>");}
		int paramsCount = ((FnSym)myId.sym()).numparams();
		int localsCount = myBody.numLocals();
		lastSeenFnExit = Codegen.nextLabel();


		/**************** Function Preamble **************/
		// Print ".text"
		Codegen.genText(".text");

		// Print labels
		if(myId.name().equals("main")){
			Codegen.genText(".globl main");
			Codegen.genLabel("main");
			Codegen.genLabel("__start");
		}
		else{
			Codegen.genLabel("_" + myId.name());
		}

		// incr lexical scope level
		currLexLv++;

		// push return address
		if(DEBUG){Codegen.genText("#644 Push return address");}
		Codegen.genPush("$ra");

		// back up the old $fp into control link
		if(DEBUG){Codegen.genText("#648 back up the old $fp into control link");}
		Codegen.genPush("$fp");

		// calculate thew new $fp
		if(DEBUG){Codegen.genText("#652 calculate thew new $fp");}
		Codegen.generate("addu", "$fp", "$sp", 8 + paramsCount*4);

		// push space for locals
		if(DEBUG){Codegen.genText("#656 push space for locals");}
		Codegen.generate("sub", "$sp", "$sp", localsCount*4);

		// back up registers, and create RegisterPool
		if(DEBUG){Codegen.genText("#660 back up registers, and create RegisterPool");}
		ASTnode.pool = new RegPool();
		pool.saveAll();

		/**************** Function Body **************/

		// Generate code for function body
		if(DEBUG){Codegen.genText("#667 Generate code for function body");}
		myBody.codeGen();



		/**************** Function Exit **************/


		if(DEBUG){Codegen.genText("#679 This label is for function's return statement to jump to");}
		Codegen.genLabel(lastSeenFnExit);

		//Grab result from top of stack, put it into $v0
		if(DEBUG){Codegen.genText("#671 before return Grab result from top of stack, put it into $v0");}
		if(!myType.type(VarDeclNode.NOT_ARRAY).isVoidType()){
			Codegen.genPop("$v0");
		}

		// FUNCTION EXIT !!!!!!
		if(DEBUG){Codegen.genText("#681 function exit");}
		// load return address
		if(DEBUG){Codegen.genText("#683 load return address");}
		Codegen.generateIndexed("lw","$ra","$fp",-4 * paramsCount);
		// save control link
		if(DEBUG){Codegen.genText("#686 save control link");}
		Codegen.generate("move", "$t0", "$fp");
		// restore FP
		if(DEBUG){Codegen.genText("#689 restore FP");}
		Codegen.generateIndexed("lw", "$fp", "$fp",((-4*paramsCount) - 4));
		// restore SP
		if(DEBUG){Codegen.genText("#692 restore SP");}
		Codegen.generate("move","$sp", "$t0");
		// restore all saved registers
		if(DEBUG){Codegen.genText("#697 restore all saved Registers");}
		pool.restoreAll();
		//return
		if(DEBUG){Codegen.genText("#695 function return");}
		if(myId.name().equals("main")){
			Codegen.generate("li","$v0",10);
			Codegen.generate("syscall");
		}
		else{
			Codegen.generate("jr", "$ra");
		}

		// Decrement current lexical level
		currLexLv--;

		if(DEBUG){Codegen.genText("# </FnDeclNode>\n");}
	}


	// 4 kids
	private TypeNode myType;
	private IdNode myId;
	private FormalsListNode myFormalsList;
	private FnBodyNode myBody;
}

class FormalDeclNode extends DeclNode {
	public FormalDeclNode(TypeNode type, IdNode id) {
		myType = type;
		myId = id;
	}

	/** processNames
	 *
	 * given: a symbol table S
	 * do:    if this formal is declared void, error!
	 *        else if this formal is multiply declared
	 *        then give an error msg and return null
	 *        else add a new entry to S and also return that Sym
	 **/
	public Sym processNames(SymbolTable S) {
		String name = myId.name();
		boolean badDecl = false;
		Sym sym = null;

		if (myType.type(VarDeclNode.NOT_ARRAY).isVoidType()) {
			Errors.fatal(myId.linenum(), myId.charnum(),
			"Non-function declared void");
			badDecl = true;
		}

		if (S.localLookup(name) != null) {
			Errors.fatal(myId.linenum(), myId.charnum(),
			"Multiply declared identifier");
			badDecl = true;
		}

		if (! badDecl) {
			try {
				sym = new Sym(myType.type(VarDeclNode.NOT_ARRAY), S.size());
				S.insert(name, sym);
				myId.link(sym);
			} catch (DuplicateException ex) {
				System.err.println("unexpected DuplicateException in FormalDeclNode.processNames");
				System.exit(-1);
			} catch (EmptySymbolTableException ex) {
				System.err.println("unexpected EmptySymbolTableException in FormalDeclNode.processNames");
				System.exit(-1);
			}
		}
		return sym;
	}

	public void unparse(PrintWriter p, int indent) {
		doIndent(p, indent);
		myType.unparse(p, indent);
		p.print(" ");
		p.print(myId.name());
		//myId.unparse(p, indent);
	}

	public void codeGen(){
		// do nothing
	}
	
	public void setOffset(int offset){
		myId.sym().offset = offset;
	}

	// 2 kids
	private TypeNode myType;
	private IdNode myId;
}

// **********************************************************************
// TypeNode and its Subclasses
// **********************************************************************

abstract class TypeNode extends ASTnode {
	/* all subclasses must provide a type method */
	abstract public Type type(int size);
}

class IntNode extends TypeNode {
	public IntNode() {
	}

	/** type **/
	public Type type(int size) {
		if (size == VarDeclNode.NOT_ARRAY) return new IntType();
		else return new IntArrayType(size);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("int");
	}
}

class BoolNode extends TypeNode {
	public BoolNode() {
	}

	/** type **/
	public Type type(int size) {
		if (size == VarDeclNode.NOT_ARRAY) return new BoolType();
		else return new BoolArrayType(size);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("bool");
	}
}

class VoidNode extends TypeNode {
	public VoidNode() {
	}

	/** type **/
	public Type type(int size) {
		return new VoidType();
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("void");
	}
}

// **********************************************************************
// StmtNode and its subclasses
// **********************************************************************

abstract class StmtNode extends ASTnode {
	abstract public void processNames(SymbolTable S);
	abstract public void typeCheck(Type T);
	abstract public void codeGen();
}

class ReadStmtNode extends StmtNode {
	public ReadStmtNode(ExpNode e) {
		myExp = e;
	}

	/** processNames **/
	public void processNames(SymbolTable S) {
		myExp.processNames(S);
	}

	/** typeCheck **/
	public void typeCheck(Type retType) {
		Type T = myExp.typeCheck();
		if (T.isFnType()) {
			Errors.fatal(myExp.linenum(), myExp.charnum(),
			"Attempt to read a function");
		}
		if (T.isArrayType()) {
			Errors.fatal(myExp.linenum(), myExp.charnum(),
			"Attempt to read an array");
		}
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("cin >> ");
		myExp.unparse(p,0);
		p.println(";");
	}

	public void codeGen() {
		if(DEBUG){Codegen.genText("# <ReadStmt>");}
		String reg0 = pool.next();

		try{
			IdNode currId = ((IdNode)myExp);


			// Step 1: get type, then read the appropriate type into $v0
			if(currId.sym().type().isIntType() || currId.sym().type().isBoolType()){
				if(DEBUG){Codegen.genText("# If we are reading an int or bool");}
				Codegen.generate("li", "$v0", 5);
				Codegen.generate("syscall");
			}

			if(currId.sym().type().isStringType()){
				if(DEBUG){Codegen.genText("# If we are reading a string");}
				Codegen.generate("li", "$v0", 8);
				Codegen.generate("add","$a0","$sp","0");
				Codegen.generate("li","$a1","1024");
				Codegen.generate("syscall");

				if(DEBUG){Codegen.genText("# Update $SP after pushing the string onto stack");}
				//First back up current $sp as address of the string into REG0.
				pool.next();
				Codegen.generate("addi", reg0, "$sp", 0);

				//Then keep moving $sp until it pass a newline character
				String reg1 = pool.next();
				String moreChar = Codegen.nextLabel();
				Codegen.genLabel(moreChar);
				Codegen.generate("subu", "$sp", "$sp", 1);
				Codegen.generateIndexed("lbu",reg1,"$sp",0);
				//Check if the newly read char is a NULL
				Codegen.generate("bne",reg1,"$0",moreChar);

				//Jump here if its NULL
				//Shift $sp one more time before we return
				Codegen.generate("subu", "$sp", "$sp", 1);
				Codegen.generate(".align", "2");
				//Now we have a NULL terminated string starting at location reg0
			}

			// Step 2: load address of variable onto stack
			if(DEBUG){Codegen.genText("# Load the address of the variable we store this to");}
			currId.codeGen();

		} catch (ClassCastException e){
			// do nothing, should only be and IdNode
		}

		// Step 3: Pop address of IdNode into reg0
		//    	reg0 = pool.next();
		//    	Codegen.genPop(reg0);

		// Step 4: Copy input into reg0
		Codegen.generate("sw","$v0",reg0,0);

		// Step 5: Push onto stack
		Codegen.genPush(reg0);

		pool.release(reg0);

		if(DEBUG){Codegen.genText("# </ReadStmt>\n");}
	}

	// 1 kid (actually can only be an IdNode or an ArrayExpNode)
	private ExpNode myExp;
}

class WriteStmtNode extends StmtNode {
	public WriteStmtNode(ExpNode exp) {
		myExp = exp;
	}

	/** processNames **/
	public void processNames(SymbolTable S) {
		myExp.processNames(S);
	}

	/** typeCheck **/
	public void typeCheck(Type retType) {
		Type T = myExp.typeCheck();
		if (T.isFnType()) {
			Errors.fatal(myExp.linenum(), myExp.charnum(),
			"Attempt to write a function");
		}
		if (T.isArrayType()) {
			Errors.fatal(myExp.linenum(), myExp.charnum(),
			"Attempt to write an array");
		}
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("cout << ");
		myExp.unparse(p,0);
		p.println(";");
	}

	public void codeGen() {
		if(DEBUG){Codegen.genText("# <WriteStmt>");}
		try{
			if(IdNode.class.isInstance(myExp)){
				IdNode currId = ((IdNode)myExp);

				if(currId.sym().type().isIntType()){
					if(DEBUG){Codegen.genText("# Write int");}
					currId.codeGenByVal();
					Codegen.generate("li","$v0", 1);
					Codegen.genPop("$a0");
					Codegen.generate("syscall");
				}
				else if(currId.sym().type().isBoolType()){
					if(DEBUG){Codegen.genText("# Write BOOL");}
					currId.codeGenByVal();
					String REG0 = pool.next();
					Codegen.genPop(REG0);

					// Default value is 0
					Codegen.generate("li", "$a0", 0);
					String falseLabel = Codegen.nextLabel();
					Codegen.generate("beqz",REG0,falseLabel);
					Codegen.generate("addi","$a0","$a0", 1);

					Codegen.genLabel(falseLabel);
					pool.release(REG0);

					//Print it.
					Codegen.generate("li","$v0", 1);
					Codegen.generate("syscall");


					// string write code
				}
				else if(currId.sym().type().isStringType()){
					if(DEBUG){Codegen.genText("# Write String");}
					Codegen.generate("li","$v0", 4);
					currId.codeGenByVal();
					Codegen.genPop("$a0");
					Codegen.generate("syscall");
				}
			} else {

				myExp.codeGen();
				Codegen.genPop("$a0");
				Codegen.generate("li","$v0", 1);
				Codegen.generate("syscall");





			}




		} catch (ClassCastException e){
			// do nothing, should be IdNode
		}

		if(DEBUG){Codegen.genText("# </WriteStmt>\n");}
	}

	// 1 kid
	private ExpNode myExp;
}

class AssignStmtNode extends StmtNode {
	public AssignStmtNode(ExpNode lhs, ExpNode exp) {
		myLhs = lhs;
		myExp = exp;
	}

	/** processNames **/
	public void processNames(SymbolTable S) {
		myLhs.processNames(S);
		myExp.processNames(S);
	}

	/** typeCheck **/
	public void typeCheck(Type retType) {
		Type T1 = myLhs.typeCheck();
		Type T2 = myExp.typeCheck();
		if (T1.isFnType() && T2.isFnType()) {
			Errors.fatal(myLhs.linenum(), myLhs.charnum(), "Function assignment");
		}
		if (T1.isArrayType() && T2.isArrayType()) {
			Errors.fatal(myLhs.linenum(), myLhs.charnum(), "Array assignment");
		}
		if (! T1.equals(T2) && ! T1.isErrorType() && ! T2.isErrorType()) {
			Errors.fatal(myLhs.linenum(), myLhs.charnum(), "Type mismatch");
		}
	}

	public void unparse(PrintWriter p, int indent) {
		myLhs.unparse(p, 0);
		p.print(" = ");
		myExp.unparse(p,0);
		p.println(";");
	}

	public void codeGen(){
		if(DEBUG){Codegen.genText("# <Assignment>");}
		// Right hand side
		if(DEBUG){Codegen.genText("# Evaluate RHS of assign");}
		myExp.codeGen();

		// Left hand side
		if(DEBUG){Codegen.genText("# Evaluate LHS of assign");}
		try{
			((IdNode)myLhs).codeGenByRef();
		}catch(ClassCastException e){
			System.out.println("ast.java#4196857:This shouldn't happen.");
			System.exit(-1);
		}

		String reg0 = pool.next();
		String reg1 = pool.next();

		if(DEBUG){Codegen.genText("# Pop the two operand for assign");}
		Codegen.genPop(reg0);// This is the address
		Codegen.genPop(reg1);// This is the operand

		// Store the operand into target address
		if(DEBUG){Codegen.genText("# Save operand to the address");}
		Codegen.generateIndexed("sw", reg1, reg0, 0);
		Codegen.genPush(reg1);

		pool.release(reg0);
		pool.release(reg1);

		if(DEBUG){Codegen.genText("# </Assignment>\n");}
	}

	// 2 kids
	private ExpNode myLhs;
	private ExpNode myExp;
}

class IfStmtNode extends StmtNode {
	public IfStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
		myDeclList = dlist;
		myExp = exp;
		myStmtList = slist;
	}

	/** processNames
	 *  
	 *  process the condition, then enter scope; process decls & stmts;
	 *  exit scope
	 *
	 **/
	public void processNames(SymbolTable S) {
		myExp.processNames(S);
		S.addMap();
		myDeclList.processNames(S);
		myStmtList.processNames(S);
		try {
			S.removeMap();
		} catch (EmptySymbolTableException ex) {
			System.err.println("unexpected EmptySymbolTableException in IfStmtNode.processNames");
			System.exit(-1);
		}
	}

	/** typeCheck **/
	public void typeCheck(Type retType) {
		Type T = myExp.typeCheck();
		if (! T.isBoolType() && ! T.isErrorType()) {
			Errors.fatal(myExp.linenum(), myExp.charnum(),
			"Non-bool expression used as an if condition");
		}
		myStmtList.typeCheck(retType);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("if (");
		myExp.unparse(p,0);
		p.println(") {");
		if (myDeclList != null) myDeclList.unparse(p,indent+2);
		if (myStmtList != null) myStmtList.unparse(p,indent+2);
		doIndent(p, indent);
		p.println("}");
	}

	public void codeGen(){
		if(DEBUG){Codegen.genText("# <IfStmtNode>");}
		String falseLabel = Codegen.nextLabel();

		// Step 1: evaluate the condition
		if(DEBUG){Codegen.genText("# evaluate the conditon");}
		myExp.codeGen();
		String reg0 = pool.next();
		Codegen.generate("beqz", reg0, falseLabel);
		pool.release(reg0);
		if(DEBUG){Codegen.genText("# THEN portion");}
		// Step 2: generate for TRUE body code
		myDeclList.codeGen();
		myStmtList.codeGen();	

		// Step 3: place falseLabel
		if(DEBUG){Codegen.genText("# ELSE portion");}
		Codegen.genLabel(falseLabel);

		
		if(DEBUG){Codegen.genText("# </IfStmtNode>");}

	}

	// 3 kids
	private ExpNode myExp;
	private DeclListNode myDeclList;
	private StmtListNode myStmtList;
}

class IfElseStmtNode extends StmtNode {
	public IfElseStmtNode(ExpNode exp, DeclListNode dlist1,
			StmtListNode slist1, DeclListNode dlist2,
			StmtListNode slist2) {
		myExp = exp;
		myThenDeclList = dlist1;
		myThenStmtList = slist1;
		myElseDeclList = dlist2;
		myElseStmtList = slist2;
	}

	/** processNames
	 *  
	 *  process the condition, then enter scope; process decls & stmts
	 *  in "then" part; then exit scope; enter scope; process decls &
	 *  stmts in "else" part; exit scope
	 *
	 **/
	public void processNames(SymbolTable S) {
		myExp.processNames(S);

		S.addMap();
		myThenDeclList.processNames(S);
		myThenStmtList.processNames(S);
		try {
			S.removeMap();
		} catch (EmptySymbolTableException ex) {
			System.err.println("unexpected EmptySymbolTableException in IfElseStmtNode.processNames");
			System.exit(-1);
		}

		S.addMap();
		myElseDeclList.processNames(S);
		myElseStmtList.processNames(S);
		try {
			S.removeMap();
		} catch (EmptySymbolTableException ex) {
			System.err.println("unexpected EmptySymbolTableException in IfElseStmtNode.processNames");
			System.exit(-1);
		}
	}

	/** typeCheck **/
	public void typeCheck(Type retType) {
		Type T = myExp.typeCheck();
		if (! T.isBoolType() && ! T.isErrorType()) {
			Errors.fatal(myExp.linenum(), myExp.charnum(),
			"Non-bool expression used as an if condition");
		}
		myThenStmtList.typeCheck(retType);
		myElseStmtList.typeCheck(retType);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("if (");
		myExp.unparse(p,0);
		p.println(") {");
		if (myThenDeclList != null) myThenDeclList.unparse(p,indent+2);
		if (myThenStmtList != null) myThenStmtList.unparse(p,indent+2);
		doIndent(p, indent);
		p.println("}");
		doIndent(p, indent);
		p.println("else {");
		if (myElseDeclList != null) myElseDeclList.unparse(p,indent+2);
		if (myElseStmtList != null) myElseStmtList.unparse(p,indent+2);
		doIndent(p, indent);
		p.println("}");
	}

	public void codeGen(){
		String falseLabel = Codegen.nextLabel();
		String doneLabel = Codegen.nextLabel();

		// Step 1: evaluate the condition
		myExp.codeGen();
		String reg0 = pool.next();
		Codegen.genPop(reg0);
		Codegen.generate("beqz", reg0, falseLabel);
		pool.release(reg0);

		// Step 2: Then portion
		myThenDeclList.codeGen();
		myThenStmtList.codeGen();
		Codegen.generate("b",doneLabel);

		// Step 3: Else portion
		Codegen.genLabel(falseLabel);
		myElseDeclList.codeGen();
		myElseStmtList.codeGen();

		// Step 3: place falseLabel
		Codegen.genLabel(doneLabel);

	}


	// 5 kids
	private ExpNode myExp;
	private DeclListNode myThenDeclList;
	private StmtListNode myThenStmtList;
	private StmtListNode myElseStmtList;
	private DeclListNode myElseDeclList;
}

class WhileStmtNode extends StmtNode {
	public WhileStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
		myExp = exp;
		myDeclList = dlist;
		myStmtList = slist;
	}

	/** processNames
	 *  
	 *  process the condition, then enter scope; process decls & stmts;
	 *  exit scope
	 *
	 **/
	public void processNames(SymbolTable S) {
		myExp.processNames(S);
		S.addMap();
		myDeclList.processNames(S);
		myStmtList.processNames(S);
		try {
			S.removeMap();
		} catch (EmptySymbolTableException ex) {
			System.err.println("unexpected EmptySymbolTableException in WhileStmtNode.processNames");
			System.exit(-1);
		}
	}

	/** typeCheck **/
	public void typeCheck(Type retType) {
		Type T = myExp.typeCheck();
		if (! T.isBoolType() && ! T.isErrorType()) {
			Errors.fatal(myExp.linenum(), myExp.charnum(),
			"Non-bool expression used as a while condition");
		}
		myStmtList.typeCheck(retType);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("while (");
		myExp.unparse(p,0);
		p.println(") {");
		if (myDeclList != null) myDeclList.unparse(p,indent+2);
		if (myStmtList != null) myStmtList.unparse(p,indent+2);
		doIndent(p, indent);
		p.println("}");
	}

	public void codeGen(){
		// Step 1: generate repeat label
		String repeatLabel = Codegen.nextLabel();

		String reg0 = pool.next();
		Codegen.genLabel(repeatLabel);
		pool.release(reg0);

		// Step 2: generate code for decl and stmt lists
		myDeclList.codeGen();
		myStmtList.codeGen();

		// Step 3: evaluate expression
		myExp.codeGen();

		// Step 4: pop exp value into register reg0
		reg0 = pool.next();
		Codegen.genPop(reg0);

		// Step 5: if true, branch to repeatLabel
		Codegen.generate("bgtz", reg0, repeatLabel);
		pool.release(reg0);

	}

	// 3 kids
	private ExpNode myExp;
	private DeclListNode myDeclList;
	private StmtListNode myStmtList;
}

class CallStmtNode extends StmtNode {
	public CallStmtNode(CallExpNode call) {
		myCall = call;
	}

	/** processNames **/
	public void processNames(SymbolTable S) {
		myCall.processNames(S);
	}

	/** typeCheck **/
	public void typeCheck(Type retType) {
		myCall.typeCheck();
	}

	public void unparse(PrintWriter p, int indent) {
		myCall.unparse(p,indent);
		p.println(";");
	}

	public void codeGen(){
		myCall.codeGen();
	}

	// 1 kid
	private CallExpNode myCall;
}

class ReturnStmtNode extends StmtNode {
	public ReturnStmtNode(ExpNode exp) {
		myExp = exp;
	}

	/** processNames **/
	public void processNames(SymbolTable S) {
		if (myExp != null) myExp.processNames(S);
	}

	/** typeCheck **/
	public void typeCheck(Type retType) {
		if (myExp != null) {
			Type T = myExp.typeCheck();
			if (retType.isVoidType()) {
				Errors.fatal(myExp.linenum(), myExp.charnum(),
				"Return with a value in a void function");
			}
			else if (! T.isErrorType() && ! retType.equals(T)) {
				Errors.fatal(myExp.linenum(), myExp.charnum(),
				"Bad return value");
			}
		}
		else {
			if (! retType.isVoidType()) {
				Errors.fatal(0, 0, "Missing return value");
			}
		}
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("return");
		if (myExp != null) {
			p.print(" ");
			myExp.unparse(p,0);
		}
		p.println(";");
	}


	public void codeGen(){
		if(myExp != null) {
			myExp.codeGen();
		}
		Codegen.generate("b", lastSeenFnExit);
	}




	// 1 kid
	private ExpNode myExp; // possibly null
}

// **********************************************************************
// ExpNode and its subclasses
// **********************************************************************

abstract class ExpNode extends ASTnode {
	// default version of processNames (for nodes with no names)
	public void processNames(SymbolTable S) {}

	abstract public Type typeCheck();
	abstract public int linenum();
	abstract public int charnum();
	abstract public void codeGen();
}

class IntLitNode extends ExpNode {
	public IntLitNode(int lineNum, int charNum, int intVal) {
		myLineNum = lineNum;
		myCharNum = charNum;
		myIntVal = intVal;
	}

	/** typeCheck **/
	public Type typeCheck() {
		return new IntType();
	}

	public void unparse(PrintWriter p, int indent) {
		p.print(myIntVal);
	}

	/** linenum **/
	public int linenum() {
		return myLineNum;
	}

	/** charnum **/
	public int charnum() {
		return myCharNum;
	}

	public void codeGen(){
		if(DEBUG){Codegen.genText("# <IntLiteral>");}
		// Step 1: grab register
		String reg0 = pool.next();

		// Step 2: load value into register
		Codegen.generate("li", reg0, myIntVal);

		// Step 3: Push onto stack
		Codegen.genPush(reg0);

		// Step 4: Release register
		pool.release(reg0);
		if(DEBUG){Codegen.genText("# </IntLiteral>\n");}
	}

	private int myLineNum;
	private int myCharNum;
	private int myIntVal;
}

class StringLitNode extends ExpNode {
	public StringLitNode(int lineNum, int charNum, String strVal) {
		myLineNum = lineNum;
		myCharNum = charNum;
		myStrVal = strVal;
	}

	/** typeCheck **/
	public Type typeCheck() {
		return new StringType();
	}

	public void unparse(PrintWriter p, int indent) {
		p.print(myStrVal);
	}

	/** linenum **/
	public int linenum() {
		return myLineNum;
	}

	/** charnum **/
	public int charnum() {
		return myCharNum;
	}

	public void codeGen(){
		// Step 1: Make the String
		String label = Codegen.nextLabel();
		String reg0 = pool.next();

		Codegen.genText(".data");
		Codegen.genLabel(label);
		Codegen.genText(".asciiz \"" + myStrVal + "\"");

		// Step 2: Push the address of the String onto the stack
		Codegen.genText(".text");
		Codegen.generate("la", reg0, label );
		Codegen.genPush(reg0);


		pool.release(reg0);
	}

	private int myLineNum;
	private int myCharNum;
	private String myStrVal;
}

class TrueNode extends ExpNode {
	public TrueNode(int lineNum, int charNum) {
		myLineNum = lineNum;
		myCharNum = charNum;
	}

	/** typeCheck **/
	public Type typeCheck() {
		return new BoolType();
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("true");
	}

	/** linenum **/
	public int linenum() {
		return myLineNum;
	}

	/** charnum **/
	public int charnum() {
		return myCharNum;
	}

	public void codeGen(){
		// Step 1: grab register
		String reg0 = pool.next();

		// Step 2: load value into register
		Codegen.generate("li", reg0, 1);

		// Step 3: Push onto stack
		Codegen.genPush(reg0);

		// Step 4: Release register
		pool.release(reg0);
	}

	private int myLineNum;
	private int myCharNum;
}

class FalseNode extends ExpNode {
	public FalseNode(int lineNum, int charNum) {
		myLineNum = lineNum;
		myCharNum = charNum;
	}

	/** typeCheck **/
	public Type typeCheck() {
		return new BoolType();
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("false");
	}

	/** linenum **/
	public int linenum() {
		return myLineNum;
	}

	/** charnum **/
	public int charnum() {
		return myCharNum;
	}

	public void codeGen(){
		// Step 1: grab register
		String reg0 = pool.next();

		// Step 2: load value into register
		Codegen.generate("li", reg0, 0);

		// Step 3: Push onto stack
		Codegen.genPush(reg0);

		// Step 4: Release register
		pool.release(reg0);
	}

	private int myLineNum;
	private int myCharNum;
}

class IdNode extends ExpNode {
	public IdNode(int lineNum, int charNum, String strVal) {
		myLineNum = lineNum;
		myCharNum = charNum;
		myStrVal = strVal;
	}

	/** processNames
	 *
	 * check for use of an undeclared name
	 * if OK, link to SymbolTable entry
	 *
	 **/
	public void processNames(SymbolTable S) {
		Sym sym = S.globalLookup(myStrVal);
		if (sym  == null) {
			Errors.fatal(myLineNum, myCharNum, "Undeclared identifier");
		}
		else link(sym);
	}

	/** typeCheck **/
	public Type typeCheck() {
		if (mySym != null) return mySym.type();
		else {
			System.err.println("ID with null sym field in IdNode.typeCheck");
			System.exit(-1);
		}
		return null;
	}

	/** link **/
	public void link(Sym sym) {
		mySym = sym;
	}

	public void unparse(PrintWriter p, int indent) {
		p.print(myStrVal);
		if (mySym != null) {
			//p.print("(" + mySym.type() + ")");
			p.print("(" + mySym + ")");
		}
	}

	/** name **/
	public String name() {
		return myStrVal;
	}

	/** symbol-table entry **/
	public Sym sym() {
		return mySym;
	}

	/** linenum **/
	public int linenum() {
		return myLineNum;
	}

	/** charnum **/
	public int charnum() {
		return myCharNum;
	}

	public void codeGenByVal(){
		if(DEBUG){Codegen.genText("# <IdNodeByVal>");}
		if(DEBUG){Codegen.genText("# Trying to retrieve variable:" + myStrVal);}
		// If it's a global variable
		if(currLexLv > mySym.scopeLv){
			if(DEBUG){Codegen.genText("# Variable:" + myStrVal + " we want to access is global.");}
			String reg0 = pool.next();
			//Now we have the address pointing to the variable.
			if(DEBUG){Codegen.genText("# load address for the label:_" + myStrVal);}
			Codegen.generate("la",reg0,"_" + myStrVal);
			//Grab its value.
			if(DEBUG){Codegen.genText("# Get Value from that address");}
			Codegen.generateIndexed("lw", reg0, reg0, 0);
			Codegen.genPush(reg0);
			pool.release(reg0);	
		}
		else{
			if(DEBUG){Codegen.genText("# Variable:" + myStrVal + " we want to access is local.");}
		}

		// If it's in current scope
		String reg0 = pool.next();
		if(DEBUG){Codegen.genText("# get address for that variable");}
		Codegen.generate("add",reg0,"$fp",mySym.getOffset());
		if(DEBUG){Codegen.genText("# Load variable value from that address");}
		Codegen.generateIndexed("lw", reg0, reg0, 0);
		Codegen.genPush(reg0);
		pool.release(reg0);

		if(DEBUG){Codegen.genText("# DONE with retrieving variable:" + myStrVal);}
		if(DEBUG){Codegen.genText("# </IdNodeByVal>\n");}
	}

	public void codeGenByRef(){
		if(DEBUG){Codegen.genText("# <IdNodeByRef>");}

		if(DEBUG){Codegen.genText("# CurrLexLv: " + currLexLv);}
		if(DEBUG){Codegen.genText("# MySym.scopeLv: " + mySym.scopeLv);}

		// If it's a global variable
		if(currLexLv > mySym.scopeLv){

			String reg0 = pool.next();
			Codegen.generate("la",reg0,"_" + myStrVal);
			Codegen.genPush(reg0);
			pool.release(reg0);		
		}

		// If it's in current scope
		String reg0 = pool.next();
		Codegen.generate("add",reg0,"$fp",mySym.getOffset());
		Codegen.genPush(reg0);
		pool.release(reg0);
		if(DEBUG){Codegen.genText("# </IdNOdeByRef>\n");}
	}

	public void codeGenForFunction(){
		if(DEBUG){Codegen.genText("# <IdNodeForFunctionCall>");}
		// Make sure its a function call
		if(!FnSym.class.isInstance(mySym)){
			System.out.println("ast.java#1598:Fatal Error.");
			System.exit(-1);
		}

		// Call the function. (Params has been pushed already previously)
		if(DEBUG){Codegen.genText("#Jump to function:" + myStrVal + " from IdNode");}
		Codegen.generate("jal", "_" + myStrVal);
		if(DEBUG){Codegen.genText("# </IdNOdeForFunctionCall>\n");}
	}

	public void codeGen(){
		codeGenByVal();
	}

	private int myLineNum;
	private int myCharNum;
	private String myStrVal;
	private Sym mySym;
}

class ArrayExpNode extends ExpNode {
	public ArrayExpNode(IdNode name, ExpNode exp) {
		myId = name;
		myExp = exp;
	}

	/** processNames **/
	public void processNames(SymbolTable S) {
		myId.processNames(S);
		myExp.processNames(S);
	}

	/** typeCheck **/
	public Type typeCheck() {
		Type T = myId.sym().type();
		if (! T.isArrayType()) {
			Errors.fatal(myId.linenum(), myId.charnum(),
			"Index applied to non-array operand");
		}
		Type expT = myExp.typeCheck();
		if (! expT.isIntType() && ! expT.isErrorType()) {
			Errors.fatal(myExp.linenum(), myExp.charnum(),
			"Non-int expression used as an array index");
		}
		if (T.isArrayType()) return ((ArrayType)T).arrayToScalar();
		else return new ErrorType();
	}

	public void unparse(PrintWriter p, int indent) {
		myId.unparse(p, 0);
		if (myExp != null) {
			p.print("[");
			myExp.unparse(p,0);
			p.print("]");
		}
	}

	/** linenum **/
	public int linenum() {
		return myId.linenum();
	}

	/** charnum **/
	public int charnum() {
		return myId.charnum();
	}

	public void codeGen(){
		//TODO
	}

	// 2 kids
	private IdNode myId;
	private ExpNode myExp;
}

class CallExpNode extends ExpNode {
	public CallExpNode(IdNode name, ExpListNode elist) {
		myId = name;
		myExpList = elist;
	}

	public CallExpNode(IdNode name) {
		myId = name;
		myExpList = new ExpListNode(new LinkedList<ExpNode>());
	}

	/** processNames 
	 *
	 * process name of called fn and all actuals
	 **/
	public void processNames(SymbolTable S) {
		myId.processNames(S);
		myExpList.processNames(S);
	}

	/** typeCheck **/
	public Type typeCheck() {
		Type T = myId.typeCheck();
		// check that ID is a fn
		if (! T.isFnType()) {
			Errors.fatal(myId.linenum(), myId.charnum(),
			"Attempt to call a non-function");
			return new ErrorType();
		}

		// check number of args
		FnSym s = (FnSym)myId.sym();
		if (s == null) {
			System.out.println("null sym for ID in CallExpNode.typeCheck");
			System.exit(-1);
		}

		int numParams = s.numparams();
		if (numParams != myExpList.length()) {
			Errors.fatal(myId.linenum(), myId.charnum(),
			"Function call with wrong number of args");
			return s.returnType();
		}

		// check type of each arg
		myExpList.typeCheck(s.paramTypes());
		return s.returnType();
	}

	public void unparse(PrintWriter p, int indent) {
		myId.unparse(p,0);
		p.print("(");
		if (myExpList != null) myExpList.unparse(p,0);
		p.print(")");
	}

	/** linenum **/
	public int linenum() {
		return myId.linenum();
	}

	/** charnum **/
	public int charnum() {
		return myId.charnum();
	}

	public void codeGen(){
		myExpList.codeGen();
		if(true){Codegen.genText("#1728#CallExpNode, check if function is called correctly");}
		myId.codeGenForFunction();
	}

	// 2 kids
	private IdNode myId;
	private ExpListNode myExpList;  // possibly null
}

abstract class UnaryExpNode extends ExpNode {
	public UnaryExpNode(ExpNode exp) {
		myExp = exp;
	}

	/** processNames **/
	public void processNames(SymbolTable S) {
		myExp.processNames(S);
	}

	/** linenum **/
	public int linenum() {
		return myExp.linenum();
	}

	/** charnum **/
	public int charnum() {
		return myExp.charnum();
	}

	// one child
	protected ExpNode myExp;
}

abstract class BinaryExpNode extends ExpNode {
	public BinaryExpNode(ExpNode exp1, ExpNode exp2) {
		myExp1 = exp1;
		myExp2 = exp2;
	}

	/** processNames **/
	public void processNames(SymbolTable S) {
		myExp1.processNames(S);
		myExp2.processNames(S);
	}

	/** linenum **/
	public int linenum() {
		return myExp1.linenum();
	}

	/** charnum **/
	public int charnum() {
		return myExp1.charnum();
	}

	// two kids
	protected ExpNode myExp1;
	protected ExpNode myExp2;
}

// **********************************************************************
// Subclasses of UnaryExpNode
// **********************************************************************

class UnaryMinusNode extends UnaryExpNode {
	public UnaryMinusNode(ExpNode exp) {
		super(exp);
	}

	/** typeCheck **/
	public Type typeCheck() {
		Type T = myExp.typeCheck();
		if (! T.isIntType() && ! T.isErrorType()) {
			Errors.fatal(myExp.linenum(), myExp.charnum(),
			"Arithmetic operator applied to non-numeric operand");
			return new ErrorType();
		}
		if (T.isErrorType()) return T;
		else return new IntType();
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("(-");
		myExp.unparse(p, 0);
		p.print(")");
	}

	public void codeGen(){
		myExp.codeGen();

		String reg0 = pool.next();

		Codegen.genPop(reg0);
		Codegen.generate("mul", reg0, reg0, -1);
		Codegen.genPush(reg0);

		pool.release(reg0);

	}
}

class NotNode extends UnaryExpNode {
	public NotNode(ExpNode exp) {
		super(exp);
	}

	/** typeCheck **/
	public Type typeCheck() {
		Type T = myExp.typeCheck();
		if (! T.isBoolType() && ! T.isErrorType()) {
			Errors.fatal(myExp.linenum(), myExp.charnum(),
			"Logical operator applied to non-bool operand");
			return new ErrorType();
		}
		if (T.isErrorType()) return T;
		else return new BoolType();
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("(!");
		myExp.unparse(p, 0);
		p.print(")");
	}

	public void codeGen(){
		myExp.codeGen();

		String reg0 = pool.next();

		Codegen.genPop(reg0);
		Codegen.generate("not", reg0, reg0);
		Codegen.genPush(reg0);

		pool.release(reg0);
	}
}

// **********************************************************************
// Subclasses of BinaryExpNode
// **********************************************************************

abstract class ArithmeticExpNode extends BinaryExpNode {
	public ArithmeticExpNode(ExpNode exp1, ExpNode exp2) {
		super(exp1, exp2);
	}

	/** typeCheck **/
	public Type typeCheck() {
		Type T1 = myExp1.typeCheck();
		Type T2 = myExp2.typeCheck();
		Type retType = new IntType();
		if (! T1.isIntType() && ! T1.isErrorType()) {
			Errors.fatal(myExp1.linenum(), myExp1.charnum(),
			"Arithmetic operator applied to non-numeric operand");
			retType = new ErrorType();
		}
		if (! T2.isIntType() && ! T2.isErrorType()) {
			Errors.fatal(myExp2.linenum(), myExp2.charnum(),
			"Arithmetic operator applied to non-numeric operand");
			retType = new ErrorType();
		}
		if (T1.isErrorType() || T2.isErrorType()) return new ErrorType();
		else return retType;
	}
}

abstract class LogicalExpNode extends BinaryExpNode {
	public LogicalExpNode(ExpNode exp1, ExpNode exp2) {
		super(exp1, exp2);
	}

	/** typeCheck **/
	public Type typeCheck() {
		Type T1 = myExp1.typeCheck();
		Type T2 = myExp2.typeCheck();
		Type retType = new BoolType();
		if (! T1.isBoolType() && ! T1.isErrorType()) {
			Errors.fatal(myExp1.linenum(), myExp1.charnum(),
			"Logical operator applied to non-bool operand");
			retType = new ErrorType();
		}
		if (! T2.isBoolType() && ! T2.isErrorType()) {
			Errors.fatal(myExp2.linenum(), myExp2.charnum(),
			"Logical operator applied to non-bool operand");
			retType = new ErrorType();
		}
		if (T1.isErrorType() || T2.isErrorType()) return new ErrorType();
		else return retType;
	}
}

abstract class EqualityExpNode extends BinaryExpNode {
	public EqualityExpNode(ExpNode exp1, ExpNode exp2) {
		super(exp1, exp2);
	}

	/** typeCheck **/
	public Type typeCheck() {
		Type T1 = myExp1.typeCheck();
		Type T2 = myExp2.typeCheck();
		Type retType = new BoolType();
		if (T1.isArrayType() && T2.isArrayType()) {
			Errors.fatal(myExp1.linenum(), myExp1.charnum(),
			"Equality operator applied to arrays");
			retType = new ErrorType();
		}
		if (T1.isFnType() && T2.isFnType()) {
			Errors.fatal(myExp1.linenum(), myExp1.charnum(),
			"Equality operator applied to functions");
			retType = new ErrorType();
		}
		if (! T1.equals(T2) && ! T1.isErrorType() && ! T2.isErrorType()) {
			Errors.fatal(myExp1.linenum(), myExp1.charnum(), "Type mismatch");
			retType = new ErrorType();
		}
		if (T1.isErrorType() || T2.isErrorType()) return new ErrorType();
		else return retType;
	}
}

abstract class RelationalExpNode extends BinaryExpNode {
	public RelationalExpNode(ExpNode exp1, ExpNode exp2) {
		super(exp1, exp2);
	}

	/** typeCheck **/
	public Type typeCheck() {
		Type T1 = myExp1.typeCheck();
		Type T2 = myExp2.typeCheck();
		Type retType = new BoolType();
		if (! T1.isIntType() && ! T1.isErrorType()) {
			Errors.fatal(myExp1.linenum(), myExp1.charnum(),
			"Relational operator applied to non-numeric operand");
			retType = new ErrorType();
		}
		if (! T2.isIntType() && ! T2.isErrorType()) {
			Errors.fatal(myExp2.linenum(), myExp2.charnum(),
			"Relational operator applied to non-numeric operand");
			retType = new ErrorType();
		}
		if (T1.isErrorType() || T2.isErrorType()) return new ErrorType();
		else return retType;
	}
}

// **********************************************************************
// Subclasses of ArithmeticExpNode
// **********************************************************************

class PlusNode extends ArithmeticExpNode {
	public PlusNode(ExpNode exp1, ExpNode exp2) {
		super(exp1, exp2);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("(");
		myExp1.unparse(p, 0);
		p.print(" + ");
		myExp2.unparse(p, 0);
		p.print(")");
	}

	public void codeGen() {
		if(DEBUG){Codegen.genText("# <PlusNode>");}
		// step 1: evaluate both operands
		myExp1.codeGen();
		myExp2.codeGen();

		// Step 2: grab Registers
		String reg0 = ASTnode.pool.next();
		String reg1 = ASTnode.pool.next();

		// step 3: pop values in T0 and T1
		Codegen.genPop(reg0);
		Codegen.genPop(reg1);

		// step 3: do the addition (T0 = T0 + T1)
		Codegen.generate("add", reg0, reg0, reg1);

		// step 4: push result
		Codegen.genPush(reg0);

		// step 5: release registers
		pool.release(reg0);
		pool.release(reg1);

		if(DEBUG){Codegen.genText("# </PlusNode>\n");}
	}


}

class MinusNode extends ArithmeticExpNode {
	public MinusNode(ExpNode exp1, ExpNode exp2) {
		super(exp1, exp2);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("(");
		myExp1.unparse(p, 0);
		p.print(" - ");
		myExp2.unparse(p, 0);
		p.print(")");
	}

	public void codeGen() {
		if(DEBUG){Codegen.genText("# <MinusNode>");}
		// step 1: evaluate both operands
		myExp1.codeGen();
		myExp2.codeGen();

		// Step 2: grab Registers
		String reg0 = ASTnode.pool.next();
		String reg1 = ASTnode.pool.next();

		// step 3: pop values in T0 and T1
		Codegen.genPop(reg0);
		Codegen.genPop(reg1);

		// step 3: do the addition (T0 = T0 - T1)
		Codegen.generate("sub", reg0, reg0, reg1);

		// step 4: push result
		Codegen.genPush(reg0);

		// step 5: release registers
		pool.release(reg0);
		pool.release(reg1);
		if(DEBUG){Codegen.genText("# </MinusNode>\n");}
	}
}

class TimesNode extends ArithmeticExpNode {
	public TimesNode(ExpNode exp1, ExpNode exp2) {
		super(exp1, exp2);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("(");
		myExp1.unparse(p, 0);
		p.print(" * ");
		myExp2.unparse(p, 0);
		p.print(")");
	}

	public void codeGen() {
		if(DEBUG){Codegen.genText("# <TimesNode>");}
		// step 1: evaluate both operands
		myExp1.codeGen();
		myExp2.codeGen();

		// Step 2: grab Registers
		String reg0 = ASTnode.pool.next();
		String reg1 = ASTnode.pool.next();

		// step 3: pop values in T0 and T1
		Codegen.genPop(reg0);
		Codegen.genPop(reg1);

		// step 3: do the addition (T0 = T0 * T1)
		Codegen.generate("mul", reg0, reg0, reg1);

		// step 4: push result
		Codegen.genPush(reg0);

		// step 5: release registers
		pool.release(reg0);
		pool.release(reg1);
		if(DEBUG){Codegen.genText("# </TimesNode>\n");}
	}
}

class DivideNode extends ArithmeticExpNode {
	public DivideNode(ExpNode exp1, ExpNode exp2) {
		super(exp1, exp2);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("(");
		myExp1.unparse(p, 0);
		p.print(" / ");
		myExp2.unparse(p, 0);
		p.print(")");
	}

	public void codeGen() {
		if(DEBUG){Codegen.genText("# <DivideNode>");}
		// step 1: evaluate both operands
		myExp1.codeGen();
		myExp2.codeGen();

		// Step 2: grab Registers
		String reg0 = ASTnode.pool.next();
		String reg1 = ASTnode.pool.next();

		// step 3: pop values in T0 and T1
		Codegen.genPop(reg0);
		Codegen.genPop(reg1);

		// step 3: do the addition (T0 = T0 / T1)
		Codegen.generate("div", reg0, reg0, reg1);

		// step 4: push result
		Codegen.genPush(reg0);

		// step 5: release registers
		pool.release(reg0);
		pool.release(reg1);
		if(DEBUG){Codegen.genText("# </DivideNode>\n");}
	}

}

// **********************************************************************
// Subclasses of LogicalExpNode
// **********************************************************************

class AndNode extends LogicalExpNode {
	public AndNode(ExpNode exp1, ExpNode exp2) {
		super(exp1, exp2);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("(");
		myExp1.unparse(p, 0);
		p.print(" && ");
		myExp2.unparse(p, 0);
		p.print(")");
	}

	public void codeGen() {
		if(DEBUG){Codegen.genText("# <AndNode>");}
		// step 1: evaluate both operands
		myExp1.codeGen();
		myExp2.codeGen();

		// Step 2: grab Registers
		String reg0 = ASTnode.pool.next();
		String reg1 = ASTnode.pool.next();

		// step 3: pop values in T0 and T1
		Codegen.genPop(reg0);
		Codegen.genPop(reg1);

		// step 3: do the addition (T0 = T0 AND T1)
		Codegen.generate("and", reg0, reg0, reg1);

		// step 4: push result
		Codegen.genPush(reg0);

		// step 5: release registers 
		pool.release(reg0);
		pool.release(reg1);
		if(DEBUG){Codegen.genText("# </AndNode>\n");}
	}

}

class OrNode extends LogicalExpNode {
	public OrNode(ExpNode exp1, ExpNode exp2) {
		super(exp1, exp2);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("(");
		myExp1.unparse(p, 0);
		p.print(" || ");
		myExp2.unparse(p, 0);
		p.print(")");
	}

	public void codeGen() {
		// TODO short circuit 

		// step 1: evaluate both operands
		myExp1.codeGen();
		myExp2.codeGen();

		// Step 2: grab Registers
		String reg0 = ASTnode.pool.next();
		String reg1 = ASTnode.pool.next();

		// step 3: pop values in T0 and T1
		Codegen.genPop(reg0);
		Codegen.genPop(reg1);

		// step 3: do the addition (T0 = T0 OR T1)
		Codegen.generate("or", reg0, reg0, reg1);

		// step 4: push result
		Codegen.genPush(reg0);

		// step 5: release registers 
		pool.release(reg0);
		pool.release(reg1);
	}
}

// **********************************************************************
// Subclasses of EqualityExpNode
// **********************************************************************

class EqualsNode extends EqualityExpNode {
	public EqualsNode(ExpNode exp1, ExpNode exp2) {
		super(exp1, exp2);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("(");
		myExp1.unparse(p, 0);
		p.print(" == ");
		myExp2.unparse(p, 0);
		p.print(")");
	}

	public void codeGen(){
		String elseLabel = Codegen.nextLabel();

		myExp2.codeGen();
		myExp1.codeGen();

		String reg0 = pool.next();
		String reg1 = pool.next();
		String reg2 = pool.next();

		Codegen.genPop(reg1);
		Codegen.genPop(reg2);


		Codegen.generate("xor",reg0,reg1,reg2);
		Codegen.generate("not",reg0,reg0);

		Codegen.genPush(reg0);

		pool.release(reg0);
		pool.release(reg1);
		pool.release(reg2);
	}

}

class NotEqualsNode extends EqualityExpNode {
	public NotEqualsNode(ExpNode exp1, ExpNode exp2) {
		super(exp1, exp2);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("(");
		myExp1.unparse(p, 0);
		p.print(" != ");
		myExp2.unparse(p, 0);
		p.print(")");
	}

	public void codeGen(){
		String elseLabel = Codegen.nextLabel();

		myExp2.codeGen();
		myExp1.codeGen();

		String reg0 = pool.next();
		String reg1 = pool.next();
		String reg2 = pool.next();

		Codegen.genPop(reg1);
		Codegen.genPop(reg2);


		Codegen.generate("xor",reg0,reg1,reg2);

		Codegen.genPush(reg0);

		pool.release(reg0);
		pool.release(reg1);
		pool.release(reg2);
	}
}

// **********************************************************************
// Subclasses of RelationalExpNode
// **********************************************************************

class LessNode extends RelationalExpNode {
	public LessNode(ExpNode exp1, ExpNode exp2) {
		super(exp1, exp2);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("(");
		myExp1.unparse(p, 0);
		p.print(" < ");
		myExp2.unparse(p, 0);
		p.print(")");
	}

	public void codeGen(){
		String elseLabel = Codegen.nextLabel();

		myExp2.codeGen();
		myExp1.codeGen();

		String reg0 = pool.next();
		String reg1 = pool.next();
		String reg2 = pool.next();

		Codegen.generate("li", reg0, 0);
		Codegen.genPop(reg1);
		Codegen.genPop(reg2);


		Codegen.generate("bge",reg1,reg2,elseLabel);
		Codegen.generate("addi",reg0,1);
		Codegen.genLabel(elseLabel);

		Codegen.genPush(reg0);

		pool.release(reg0);
		pool.release(reg1);
		pool.release(reg2);
	}
}

class GreaterNode extends RelationalExpNode {
	public GreaterNode(ExpNode exp1, ExpNode exp2) {
		super(exp1, exp2);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("(");
		myExp1.unparse(p, 0);
		p.print(" > ");
		myExp2.unparse(p, 0);
		p.print(")");
	}

	public void codeGen(){
		if(DEBUG){Codegen.genText("# <GreaterNode>");}
		String elseLabel = Codegen.nextLabel();

		myExp2.codeGen();
		myExp1.codeGen();

		String reg0 = pool.next();
		String reg1 = pool.next();
		String reg2 = pool.next();
		if(DEBUG){Codegen.genText("# Default is false");}
		Codegen.generate("li", reg0, 0);
		Codegen.genPop(reg1);
		Codegen.genPop(reg2);


		Codegen.generate("ble",reg1,reg2,elseLabel);
		if(DEBUG){Codegen.genText("# Increment to turn false to true if TRUE");}
		Codegen.generate("addi",reg0,1);

		if(DEBUG){Codegen.genText("# This label is for FALSE to jump to");}
		Codegen.genLabel(elseLabel);
		Codegen.genPush(reg0);

		pool.release(reg0);
		pool.release(reg1);
		pool.release(reg2);
		if(DEBUG){Codegen.genText("# </GreaterNode>\n");}
	}
}

class LessEqNode extends RelationalExpNode {
	public LessEqNode(ExpNode exp1, ExpNode exp2) {
		super(exp1, exp2);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("(");
		myExp1.unparse(p, 0);
		p.print(" <= ");
		myExp2.unparse(p, 0);
		p.print(")");
	}

	public void codeGen(){
		String elseLabel = Codegen.nextLabel();

		myExp2.codeGen();
		myExp1.codeGen();

		String reg0 = pool.next();
		String reg1 = pool.next();
		String reg2 = pool.next();

		Codegen.generate("li", reg0, 0);
		Codegen.genPop(reg1);
		Codegen.genPop(reg2);


		Codegen.generate("bgt",reg1,reg2,elseLabel);
		Codegen.generate("addi",reg0,1);
		Codegen.genLabel(elseLabel);

		Codegen.genPush(reg0);

		pool.release(reg0);
		pool.release(reg1);
		pool.release(reg2);
	}

}

class GreaterEqNode extends RelationalExpNode {
	public GreaterEqNode(ExpNode exp1, ExpNode exp2) {
		super(exp1, exp2);
	}

	public void unparse(PrintWriter p, int indent) {
		p.print("(");
		myExp1.unparse(p, 0);
		p.print(" >= ");
		myExp2.unparse(p, 0);
		p.print(")");
	}

	public void codeGen(){
		String elseLabel = Codegen.nextLabel();

		myExp2.codeGen();
		myExp1.codeGen();

		String reg0 = pool.next();
		String reg1 = pool.next();
		String reg2 = pool.next();

		Codegen.generate("li", reg0, 0);
		Codegen.genPop(reg1);
		Codegen.genPop(reg2);


		Codegen.generate("blt",reg1,reg2,elseLabel);
		Codegen.generate("addi",reg0,1);
		Codegen.genLabel(elseLabel);

		Codegen.genPush(reg0);

		pool.release(reg0);
		pool.release(reg1);
		pool.release(reg2);
	}
}
