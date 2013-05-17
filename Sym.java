import java.util.*;

// **********************************************************************
// The Sym class defines a symbol-table entry;
// an object that contains a type (a Type).
// **********************************************************************
public class Sym {
    private static int base = 0;
    public Type type;
    public String mark;

    public static int getBase(){
	base++;
	return base;
    }
    
    public Sym(){
    	
    }
    
    public Sym(Type type) {
        this.type = type;
	mark = "SYM[" + getBase() + "]";
    }

    public Type type() {
        return type;
    }

    public String toString() {
        return type.toString();
    }
}

// **********************************************************************
// The FnSym class is a subclass of the Sym class, just for functions.
// The returnType field holds the return type, and there are new fields
// to hold information about the parameters.
// **********************************************************************
class FnSym extends Sym {
    // new fields
    private Type returnType;
    private int numParams;
    private List<Type> paramTypes; 
    private int localSize;

    public FnSym(Type type, int numparams, int lSize) {
        super(new FnType());
        returnType = type;
        numParams = numparams;
       	localSize = lSize;
    }

    public void addFormals(List<Type> L) {
        paramTypes = L;
    }
    
    public Type returnType() {
        return returnType;
    }

    public int numparams() {
        return numParams;
    }

    public List<Type> paramTypes() {
        return paramTypes;
    }

    public String toString() {
        // make list of formals
        String str = "";
        boolean notfirst = false;
        for (Type type : paramTypes) {
            if (notfirst)
                str += ",";
            else
                notfirst = true;
            str += type.toString();
        }

        str += "->" + returnType.toString();
        return str;
    }
    
    
    public int getLocal(int localIndex){
    	return getParam(numParams) - 4*localIndex;
    }
    
    public int getParam(int paramIndex){
    	return -12 - paramIndex*4;
    }
    
    public int getControlLinkOff(){
    	return -8;
    }
    
    public int getAccessLinkOff(){
    	return -4;
    }
    
    public int getReturnOff(){
    	return 0;
    }
}
