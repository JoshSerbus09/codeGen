// **********************************************************************
// This file contains a JLex specification for a scanner for the "Mini"
// language.
// **********************************************************************
import java_cup.runtime.*; // defines the Symbol class
// The generated scanner will return a Symbol for each token that it finds.
// A Symbol contains an Object field named value; that field will be of type
// TokenVal, defined below.
//
// A TokenVal object contains the line number on which the token occurs as
// well as the number of the character on that line that starts the token.
// Some tokens (literals and IDs) also include the value of the token.
class TokenVal {
 // fields
    int linenum;
    int charnum;
 // constructor
    TokenVal(int l, int c) {
        linenum = l;
        charnum = c;
    }
}
class IntLitTokenVal extends TokenVal {
 // new field: the value of the int literal
    int intVal;
 // constructor
    IntLitTokenVal(int l, int c, int val) {
        super(l,c);
        intVal = val;
    }
}
class IdTokenVal extends TokenVal {
 // new field: the value of the identifier
    String idVal;
 // constructor
    IdTokenVal(int l, int c, String val) {
        super(l,c);
        idVal = val;
    }
}
class StrLitTokenVal extends TokenVal {
 // new field: the value of the string literal
    String strVal;
 // constructor
    StrLitTokenVal(int l, int c, String val) {
        super(l,c);
        strVal = val;
    }
}
// The following class is used to keep track of the character number at which
// the current token starts on its line.
class CharNum {
  static int num=1;
}


class Yylex implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final char YYEOF = '\uFFFF';
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yyline;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yyline = 0;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private char yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YYEOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YYEOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_start () {
		if ((byte) '\n' == yy_buffer[yy_buffer_start]) {
			++yyline;
		}
		++yy_buffer_start;
	}
	private void yy_pushback () {
		--yy_buffer_end;
	}
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ((byte) '\n' == yy_buffer[i]) {
				++yyline;
			}
		}
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int yy_acpt[] = {
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NOT_ACCEPT,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR,
		YY_NO_ANCHOR
	};
	private int yy_cmap[] = {
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 1, 2, 0, 0, 1, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0,
		1, 3, 4, 5, 0, 0, 6, 7,
		8, 9, 10, 11, 12, 13, 0, 14,
		15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 0, 16, 17, 18, 19, 0,
		0, 20, 20, 20, 20, 20, 20, 20,
		20, 20, 20, 20, 20, 20, 20, 20,
		20, 20, 20, 20, 20, 20, 20, 20,
		20, 20, 20, 21, 22, 23, 0, 20,
		0, 24, 25, 26, 27, 28, 29, 20,
		30, 31, 20, 20, 32, 20, 33, 34,
		20, 20, 35, 36, 37, 38, 39, 40,
		20, 20, 20, 41, 42, 43, 0, 0
		
	};
	private int yy_rmap[] = {
		0, 1, 2, 1, 3, 4, 5, 1,
		1, 1, 1, 1, 1, 6, 7, 1,
		8, 9, 10, 11, 1, 1, 1, 1,
		1, 1, 1, 1, 1, 1, 1, 1,
		11, 1, 12, 1, 11, 11, 1, 11,
		11, 11, 11, 11, 11, 11, 11, 13,
		14, 15, 16, 17, 18, 19, 20, 21,
		22, 23, 24, 25, 26, 27, 28, 29,
		30, 31, 32, 33, 34, 35, 36, 37,
		38, 39, 40, 41, 42, 43, 44, 45,
		46, 47 
	};
	private int yy_nxt[][] = {
		{ 1, 2, 3, 4, 5, 6, 48, 1,
			7, 8, 9, 10, 11, 12, 13, 14,
			15, 16, 17, 18, 19, 20, 1, 21,
			19, 71, 62, 19, 72, 78, 19, 49,
			19, 19, 19, 81, 19, 73, 19, 74,
			79, 22, 51, 23 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1 
		},
		{ -1, 2, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, 24, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1 
		},
		{ 5, 5, -1, 5, 25, 5, 5, 5,
			5, 5, 5, 5, 5, 5, 5, 5,
			5, 5, 5, 5, 5, 5, 47, 5,
			5, 5, 5, 5, 5, 5, 5, 5,
			5, 5, 5, 5, 5, 5, 5, 5,
			5, 5, 5, 5 
		},
		{ 6, 6, -1, 6, 6, 6, 6, 6,
			6, 6, 6, 6, 6, 6, 6, 6,
			6, 6, 6, 6, 6, 6, 6, 6,
			6, 6, 6, 6, 6, 6, 6, 6,
			6, 6, 6, 6, 6, 6, 6, 6,
			6, 6, 6, 6 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, 6, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 14,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, 27, 28, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, 29, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, 30, 31, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ 34, 34, -1, 34, 38, 34, 34, 34,
			34, 34, 34, 34, 34, 34, 34, 34,
			34, 34, 34, 34, 34, 34, 50, 34,
			34, 34, 34, 34, 34, 34, 34, 34,
			34, 34, 34, 34, 34, 34, 34, 34,
			34, 34, 34, 34 
		},
		{ 34, 34, 35, 34, 5, 34, 34, 5,
			34, 34, 34, 34, 34, 34, 34, 34,
			34, 34, 34, 34, 34, 34, 5, 34,
			34, 34, 34, 34, 34, 34, 34, 34,
			34, 5, 34, 34, 34, 5, 34, 34,
			34, 34, 34, 34 
		},
		{ -1, -1, -1, -1, -1, -1, 26, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 32, 19, 19,
			19, 53, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ 34, 34, 35, 34, 34, 34, 34, 34,
			34, 34, 34, 34, 34, 34, 34, 34,
			34, 34, 34, 34, 34, 34, 34, 34,
			34, 34, 34, 34, 34, 34, 34, 34,
			34, 34, 34, 34, 34, 34, 34, 34,
			34, 34, 34, 34 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, 33, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, 36, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, 19, 19, 19, 19, 37, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			39, 19, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, 19, 19, 19, 19, 40, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 41, 19, 19, 19,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 42, 19, 19, 19,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 43, 19, 19, 19, 19,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 44, 19, 19, 19,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 45, 19, 19, 19,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, 46, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 52,
			19, 19, 64, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, 19, 54, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, 19, 19, 19, 19, 19, 55, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, 19, 19, 19, 56, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, 19, 19, 19, 19, 19, 57, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 58,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, 19, 19, 19, 59, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			60, 19, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, 19, 19, 61, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, 19, 63, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			65, 19, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, 19, 19, 66, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, 19, 67, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			68, 19, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 69,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, 19, 19, 19, 19, 19, 70, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			75, 19, 19, 19, 19, 19, 19, 19,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 76, 19,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, 19, 19, 19, 19, 77, 19, 19,
			19, -1, -1, -1 
		},
		{ -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, 19,
			-1, -1, -1, -1, 19, -1, -1, -1,
			19, 19, 19, 19, 80, 19, 19, 19,
			19, 19, 19, 19, 19, 19, 19, 19,
			19, -1, -1, -1 
		}
	};
	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		char yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			if (YYEOF != yy_lookahead) {
				yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YYEOF == yy_lookahead && true == yy_initial) {

return new Symbol(sym.EOF);
				}
				else if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_to_mark();
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_pushback();
					}
					if (0 != (YY_START & yy_anchor)) {
						yy_move_start();
					}
					switch (yy_last_accept_state) {
					case 1:
						{Errors.fatal(yyline+1, CharNum.num,
                         "ignoring illegal character: " + yytext());
            CharNum.num++;
           }
					case -2:
						break;
					case 2:
						{CharNum.num += yytext().length();}
					case -3:
						break;
					case 3:
						{CharNum.num = 1;}
					case -4:
						break;
					case 4:
						{Symbol S = new Symbol(sym.NOT,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -5:
						break;
					case 5:
						{// UNTERMINATED STRING LITERAL (NEWLINE OR EOF BEFORE CLOSING QUOTE)
            Errors.fatal(yyline+1, CharNum.num,
                         "ignoring unterminated string literal");
            CharNum.num += yytext().length();
           }
					case -6:
						break;
					case 6:
						{ // comment -- just ignore it
           }
					case -7:
						break;
					case 7:
						{Symbol S = new Symbol(sym.LPAREN,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -8:
						break;
					case 8:
						{Symbol S = new Symbol(sym.RPAREN,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -9:
						break;
					case 9:
						{Symbol S = new Symbol(sym.TIMES,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -10:
						break;
					case 10:
						{Symbol S = new Symbol(sym.PLUS,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -11:
						break;
					case 11:
						{Symbol S = new Symbol(sym.COMMA,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -12:
						break;
					case 12:
						{Symbol S = new Symbol(sym.MINUS,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -13:
						break;
					case 13:
						{Symbol S = new Symbol(sym.DIVIDE,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -14:
						break;
					case 14:
						{int val;
            double d = (new Double(yytext())).doubleValue();
            if (d > Integer.MAX_VALUE) {
                Errors.warn(yyline+1, CharNum.num,
                            "integer literal too large; using max value");
                val = Integer.MAX_VALUE;
            }
            else val = (new Integer(yytext())).intValue();
            Symbol S = new Symbol(sym.INTLITERAL,
                                new IntLitTokenVal(yyline+1, CharNum.num, val)
                                );
            CharNum.num += yytext().length();
            return S;
           }
					case -15:
						break;
					case 15:
						{Symbol S = new Symbol(sym.SEMICOLON,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -16:
						break;
					case 16:
						{Symbol S = new Symbol(sym.LESS,
                                 new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -17:
						break;
					case 17:
						{Symbol S = new Symbol(sym.ASSIGN,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -18:
						break;
					case 18:
						{Symbol S = new Symbol(sym.GREATER,
                                 new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -19:
						break;
					case 19:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -20:
						break;
					case 20:
						{Symbol S = new Symbol(sym.LSQBRACKET,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -21:
						break;
					case 21:
						{Symbol S = new Symbol(sym.RSQBRACKET,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -22:
						break;
					case 22:
						{Symbol S = new Symbol(sym.LCURLY,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -23:
						break;
					case 23:
						{Symbol S = new Symbol(sym.RCURLY,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -24:
						break;
					case 24:
						{Symbol S = new Symbol(sym.NOTEQUALS,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -25:
						break;
					case 25:
						{// LEGAL STRING LITERAL
            Symbol S = new Symbol(sym.STRINGLITERAL,
                                  new StrLitTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -26:
						break;
					case 26:
						{Symbol S = new Symbol(sym.AND,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -27:
						break;
					case 27:
						{Symbol S = new Symbol(sym.WRITE,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -28:
						break;
					case 28:
						{Symbol S = new Symbol(sym.LESSEQ,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -29:
						break;
					case 29:
						{Symbol S = new Symbol(sym.EQUALS,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -30:
						break;
					case 30:
						{Symbol S = new Symbol(sym.GREATEREQ,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -31:
						break;
					case 31:
						{Symbol S = new Symbol(sym.READ,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -32:
						break;
					case 32:
						{Symbol S = new Symbol(sym.IF,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -33:
						break;
					case 33:
						{Symbol S = new Symbol(sym.OR,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -34:
						break;
					case 34:
						{// UNTERMINATED STRING LITERAL WITH BAD ESCAPED CHAR
            // (NEWLINE OR EOF BEFORE CLOSING QUOTE)
            Errors.fatal(yyline+1, CharNum.num,
            "ignoring unterminated string literal with bad escaped character");
            CharNum.num += yytext().length();
           }
					case -35:
						break;
					case 35:
						{// UNTERMINATED STRING LITERAL WITH BAD ESCAPED CHAR
            // (NEWLINE RIGHT AFTER BACKSLASH)
            Errors.fatal(yyline+1, CharNum.num,
            "ignoring unterminated string literal with bad escaped character");
            CharNum.num = 1;
           }
					case -36:
						break;
					case 36:
						{Symbol S = new Symbol(sym.CIN,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -37:
						break;
					case 37:
						{Symbol S = new Symbol(sym.INT,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -38:
						break;
					case 38:
						{// TERMINATED STRING LITERAL WITH BAD ESCAPED CHAR
            Errors.fatal(yyline+1, CharNum.num,
                         "ignoring string literal with bad escaped character");
            CharNum.num += yytext().length();
           }
					case -39:
						break;
					case 39:
						{Symbol S = new Symbol(sym.BOOL,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -40:
						break;
					case 40:
						{Symbol S = new Symbol(sym.COUT,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -41:
						break;
					case 41:
						{Symbol S = new Symbol(sym.ELSE,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -42:
						break;
					case 42:
						{Symbol S = new Symbol(sym.TRUE,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -43:
						break;
					case 43:
						{Symbol S = new Symbol(sym.VOID,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -44:
						break;
					case 44:
						{Symbol S = new Symbol(sym.FALSE,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -45:
						break;
					case 45:
						{Symbol S = new Symbol(sym.WHILE,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -46:
						break;
					case 46:
						{Symbol S = new Symbol(sym.RETURN,
                                  new TokenVal(yyline+1, CharNum.num));
            CharNum.num += yytext().length();
            return S;
           }
					case -47:
						break;
					case 48:
						{Errors.fatal(yyline+1, CharNum.num,
                         "ignoring illegal character: " + yytext());
            CharNum.num++;
           }
					case -48:
						break;
					case 49:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -49:
						break;
					case 51:
						{Errors.fatal(yyline+1, CharNum.num,
                         "ignoring illegal character: " + yytext());
            CharNum.num++;
           }
					case -50:
						break;
					case 52:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -51:
						break;
					case 53:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -52:
						break;
					case 54:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -53:
						break;
					case 55:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -54:
						break;
					case 56:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -55:
						break;
					case 57:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -56:
						break;
					case 58:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -57:
						break;
					case 59:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -58:
						break;
					case 60:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -59:
						break;
					case 61:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -60:
						break;
					case 62:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -61:
						break;
					case 63:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -62:
						break;
					case 64:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -63:
						break;
					case 65:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -64:
						break;
					case 66:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -65:
						break;
					case 67:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -66:
						break;
					case 68:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -67:
						break;
					case 69:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -68:
						break;
					case 70:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -69:
						break;
					case 71:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -70:
						break;
					case 72:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -71:
						break;
					case 73:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -72:
						break;
					case 74:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -73:
						break;
					case 75:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -74:
						break;
					case 76:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -75:
						break;
					case 77:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -76:
						break;
					case 78:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -77:
						break;
					case 79:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -78:
						break;
					case 80:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -79:
						break;
					case 81:
						{Symbol S = new Symbol(sym.ID,
                                  new IdTokenVal(yyline+1, CharNum.num,
                                  yytext()));
            CharNum.num += yytext().length();
            return S;
           }
					case -80:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
					}
				}
			}
		}
	}
}
