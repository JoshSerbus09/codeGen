///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            P5
// Files:            P5.java 
// Semester:         CS536 Spring 2013
//
// Author:           Josh Serbus
// Email:            serbus@wisc.edu
// CS Login:         serbus
// Lecturer's Name:  Hasti
//
//                   PAIR PROGRAMMERS COMPLETE THIS SECTION
// Pair Partner:     Jiyao Yang
// Email:            jyang73@wisc.edu
// CS Login:         jiyao
// Lecturer's Name:  Hasti
//////////////////////////// 80 columns wide //////////////////////////////////

import java.io.*;
import java_cup.runtime.*;

/**
 * Main program to test the Mini parser.
 *
 * There should be 2 command-line arguments:
 *    1. the file to be parsed
 *    2. the output file into which the AST built by the parser should be
 *       unparsed
 * The program opens the two files, creates a scanner and a parser, and
 * calls the parser.  If the parse is successful, the AST is unparsed.
 */

public class P6 {
    public static void main(String[] args)
        throws IOException // may be thrown by the scanner
    {
        // check for command-line args
        if (args.length != 2) {
            System.err.println("please supply name of file to be parsed and name of file for unparsed version.");
            System.exit(-1);
        }

        // open input file
        FileReader inFile = null;
        try {
            inFile = new FileReader(args[0]);
        } catch (FileNotFoundException ex) {
            System.err.println("File " + args[0] + " not found.");
            System.exit(-1);
        }

        // open output file
        PrintWriter outFile = null;
        try {
            outFile = new PrintWriter(args[1] + ".txt");
        } catch (FileNotFoundException ex) {
            System.err.println("File " + args[1] +
                               " could not be opened for writing.");
            System.exit(-1);
        }

        parser P = new parser(new Yylex(inFile));

        Symbol root=null; // the parser will return a Symbol whose value
                          // field is the translation of the root
                          // nonterminal (i.e., of the nonterminal
                          // "program")

	// parse errors boolean
	boolean noErrors = false;

        try {
            root = P.parse(); // do the parse
            System.out.println ("Program Parsed Correctly. Beginning Name Analysis");
	    noErrors = true;
	
        } catch (Exception ex){
            System.err.println("Exception occured during parse: " + ex);
            System.exit(-1);
        } 

	// only call if parse successful
	if(noErrors){
		((ProgramNode)root.value).processNames();
		
		if(!ASTnode.foundMain){
			Errors.fatal(0, 0,
            "No main function");
			System.exit(-1);
		}
	}

	// only call if name analysis successful
	if(!Errors.programFail) {
	    System.out.println("Name Analysis Successful. Beginning Type Check.");
	    ((ProgramNode)root.value).typeCheck();
	} else {
	    System.err.println("Name Analysis Failed. Exiting.");
	    System.exit(-1);
	}

	if(!Errors.programFail){
		System.out.println("Type Checking Successful. Beginning Unparse.");
		((ASTnode)root.value).unparse(outFile, 0);
	} else {
		System.out.println("Type Checking Failed. Exiting.");
		System.exit(-1);
	}

        outFile.close();
        
        
        
    // Now start code generation
    	 System.out.println("Beginning Code generation.");
    	 Codegen.initialize("spim_" + args[1] + ".s");
    	 ((ProgramNode)root.value).codeGen();
    	 Codegen.writeFile();
    	 System.out.println("Code generation complete. Output file: spim_" + args[1] + ".s");
        return;
    }
}
