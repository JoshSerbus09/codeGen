// **********************************************************************
// Type class and its subclasses: ErrorType, FnType,   IntType
//                                BoolType,  VoidType, ArrayType,
//                                StringType
//                                
//
// ArrayType also has subclasses: IntArrayType, BoolArrayType
// **********************************************************************

abstract public class Type {

    // *******************
    // default constructor
    // *******************
    public Type() {
    }

    // ******************************************************************
    // every subclass must provide a toString method and an equals method
    // ******************************************************************
    abstract public String toString();
    abstract public boolean equals(Type t);

    // ********************************
    // default methods for "isXXXType"
    // ********************************
    public boolean isErrorType() {
        return false;
    }

    public boolean isIntType() {
        return false;
    }

    public boolean isBoolType() {
        return false;
    }

    public boolean isStringType() {
        return false;
    }

    public boolean isVoidType() {
        return false;
    }

    public boolean isIntArrayType() {
        return false;
    }

    public boolean isBoolArrayType() {
        return false;
    }

    public boolean isFnType() {
        return false;
    }

    public boolean isArrayType() {
        return false;
    }
}

// **********************************************************************
// ErrorType
// **********************************************************************
class ErrorType extends Type {

    public boolean isErrorType() {
        return true;
    }

    public boolean equals(Type t) {
        return t.isErrorType();
    }

    public String toString() {
        return "error";
    }
}

// **********************************************************************
// FnType
// **********************************************************************
class FnType extends Type {

    public boolean isFnType() {
        return true;
    }

    public boolean equals(Type t) {
        return t.isFnType();
    }

    public String toString() {
        return "function";
    }
}

// **********************************************************************
// IntType
// **********************************************************************
class IntType extends Type {

    public boolean isIntType() {
        return true;
    }

    public boolean equals(Type t) {
        return t.isIntType();
    }

    public String toString() {
        return "int";
    }
}

// **********************************************************************
// BoolType
// **********************************************************************
class BoolType extends Type {

    public boolean isBoolType() {
        return true;
    }

    public boolean equals(Type t) {
        return t.isBoolType();
    }

    public String toString() {
        return "bool";
    }
}

// **********************************************************************
// VoidType
// **********************************************************************
class VoidType extends Type {

    public boolean isVoidType() {
        return true;
    }

    public boolean equals(Type t) {
        return t.isVoidType();
    }

    public String toString() {
        return "void";
    }
}

// **********************************************************************
// StringType
// **********************************************************************
class StringType extends Type {

    public boolean isStringType() {
        return true;
    }

    public boolean equals(Type t) {
        return t.isStringType();
    }

    public String toString() {
        return "String";
    }
}

// **********************************************************************
// ArrayType
// **********************************************************************
abstract class ArrayType extends Type {
    protected int mySize;

    // constructor
    public ArrayType(int size) {
        mySize = size;
    }

    public boolean isArrayType() {
        return true;
    }

    abstract public Type arrayToScalar();
 
    public int size() {
        return mySize;
    }
}

// **********************************************************************
// IntArrayType
// **********************************************************************
class IntArrayType extends ArrayType {

    // constructor
    public IntArrayType(int size) {
        super(size);
    }

    public boolean isIntArrayType() {
        return true;
    }

    public Type arrayToScalar() {
        return new IntType();
    }

    public boolean equals(Type t) {
        return t.isIntArrayType();
    }

    public String toString() {
        return "int[" + mySize + "]";
    }
}

// **********************************************************************
// BoolArrayType
// **********************************************************************
class BoolArrayType extends ArrayType {

    // constructor
    public BoolArrayType(int size) {
        super(size);
    }

    public boolean isBoolArrayType() {
        return true;
    }

    public Type arrayToScalar() {
        return new BoolType();
    }

    public boolean equals(Type t) {
        return t.isBoolArrayType();
    }

    public String toString() {
        return "bool[" + mySize + "]";
    }
}

