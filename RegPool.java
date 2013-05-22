import java.util.ArrayList;
import java.util.Collections;

/**
 * This is the class which keeps track of the free registers available during
 * compile time. The saveAll() method is called in the function preamble to
 * store the current values of the registers. Then on function exit the
 * registers are restored.
 * <p>
 * Whenever we need a register for an operation, pool.next() is called to obtain
 * a register that is not in use currently, then when the operation is complete
 * we free the register by calling pool.restore(<register>).
 * 
 * @author serbus, jiyao
 * 
 */
public class RegPool {

	private static final String[] REGS = { "$t0", "$t1", "$t2", "$t3", "$t4",
			"$t5", "$t6", "$t7", };
	private static boolean staticPoolInitialized = false;
	ArrayList<String> pool = new ArrayList<String>();
	public static ArrayList<String> staticPool = new ArrayList<String>();

	public int savedRegCount() {
		return REGS.length;
	}

	public RegPool() {
		if (!staticPoolInitialized) {
			staticPool.add("$t8");
			staticPool.add("$t9");
			staticPoolInitialized = true;
		}
	}

	public static String nextStatic() {
		if (!hasNextStatic()) {
			System.out.println("RegisterPool fatal error.");
			System.exit(-1);
		}
		return staticPool.get(0);
	}

	public static void releaseStatic(String REG) {
		staticPool.add(REG);
		Collections.sort(staticPool);
	}

	public static boolean hasNextStatic() {
		if (staticPool.size() <= 0) {
			return false;
		} else {
			return true;
		}
	}

	public void saveAll() {
		for (String str : REGS) {
			Codegen.genPush(str);
			pool.add(str);
		}
	}

	public String next() {
		if (!hasNext()) {
			System.out.println("RegisterPool fatal error.");
			System.exit(-1);
		}
		return pool.remove(0);
	}

	public boolean hasNext() {
		if (pool.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void release(String reg) {
		if (pool.contains(reg)) {
			System.out
					.println("Fatal Error:RegPool#4646: Register released multiple times.");
			int a = 0 / 0;
		}

		pool.add(0, reg);
		Collections.sort(pool);
	}

	public void restoreAll() {
		for (int i = REGS.length - 1; i >= 0; i--) {
			Codegen.genPop(REGS[i]);
		}
	}
}
