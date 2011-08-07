package math.groups;

public class TestGgT implements TestFunction<GgtRecord> {

	private final int	n;
	private final int	c;
	private int			result;

	public TestGgT(int n, int c) {
		this.n = n;
		this.c = c;
	}

	@Override
	public boolean check(int x) {
		result = ggT(x, n);
		return result == c;
	}

	private int ggT(int a, int b) {
		while (b != 0) {
			int h = a % b;
			a = b;
			b = h;
		}
		return a;
	}

	@Override
	public GgtRecord getRecord(int x) {
		GgtRecord record = new GgtRecord();
		record.a = x;
		record.b = n;
		record.result = result;
		record.c = c;
		return record;
	}
}
