###
# This Makefile can be used to make a parser for the Mini language
# (parser.class) and to make a program (P3.class) that tests the parser and
# the unparse methods in ast.java.
#
# make clean removes all generated files.
#
###

JC = javac

P6.class: P6.java parser.class Yylex.class ASTnode.class
	$(JC) -g P6.java

parser.class: parser.java ASTnode.class Yylex.class Errors.class
	$(JC) parser.java

parser.java: Mini.cup
	java java_cup.Main < Mini.cup

Yylex.class: Mini.jlex.java sym.class Errors.class
	$(JC) Mini.jlex.java

ASTnode.class: ast.java
	$(JC) -g ast.java

Sym.class: Sym.java
	$(JC) Sym.java

Mini.jlex.java: Mini.jlex sym.class
	java JLex.Main Mini.jlex

sym.class: sym.java
	$(JC) -g sym.java

sym.java: Mini.cup
	java java_cup.Main < Mini.cup

SymbolTable.class: SymbolTable.java
	$(JC) SymbolTable.java

Errors.class: Errors.java
	$(JC) Errors.java

###
# clean
###
clean:
	rm -f *~ *.class parser.java Mini.jlex.java sym.java
