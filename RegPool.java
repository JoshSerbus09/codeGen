import java.util.ArrayList;
import java.util.Collections;


public class RegPool {
	
	private static final String[] REGS = {"$t0","$t1","$t2","$t3","$t4","$t5","$t6","$t7",};
	
	ArrayList<String> pool = new ArrayList<String>();

	public int savedRegCount(){
		return REGS.length;
	}
	
	public RegPool(){
	}
	
	public void saveAll(){
		for(String str : REGS){
			Codegen.genPush(str);
			pool.add(str);
		}
	}
	
	public String next(){
		if(!hasNext()){
			System.out.println("RegisterPool fatal error.");
			System.exit(-1);
		}
		return pool.remove(0);
	}
	
	public boolean hasNext(){
		if(pool.size() > 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void release(String reg){
		pool.add(0,reg);
		Collections.sort(pool);
	}
	
	public void restoreAll(){
		for(int i = REGS.length - 1; i >= 0; i--){
			Codegen.genPop(REGS[i]);
		}
	}
}
