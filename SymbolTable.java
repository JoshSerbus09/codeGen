import java.util.*;

public class SymbolTable {
	private List<HashMap<String, Sym>> list;
	
	public SymbolTable() {
		list = new LinkedList<HashMap<String, Sym>>();
		list.add(new HashMap<String, Sym>());
	}
	
	public void insert(String name, Sym sym) 
	throws DuplicateException, EmptySymbolTableException {
		if (name == null || sym == null)
			throw new NullPointerException();
		
		if (list.isEmpty())
			throw new EmptySymbolTableException();
		
		HashMap<String, Sym> symTab = list.get(0);
		if (symTab.containsKey(name))
			throw new DuplicateException();
		
		symTab.put(name, sym);
	}
	
	public void addMap() {
		list.add(0, new HashMap<String, Sym>());
	}
	
	public Sym localLookup(String name) {
		if (list.isEmpty())
			return null;
		
		HashMap<String, Sym> symTab = list.get(0); 
		return symTab.get(name);
	}
	
	public Sym globalLookup(String name) {
		if (list.isEmpty())
			return null;
		
		for (HashMap<String, Sym> symTab : list) {
			Sym sym = symTab.get(name);
			if (sym != null)
				return sym;
		}
		return null;
	}
	
	public void removeMap() throws EmptySymbolTableException {
		if (list.isEmpty())
			throw new EmptySymbolTableException();
		list.remove(0);
	}
	
	public void print() {
		System.out.print("\nSymbol Table\n");
		for (HashMap<String, Sym> symTab : list) {
			System.out.println(symTab.toString());
		}
		System.out.println();
	}
}
