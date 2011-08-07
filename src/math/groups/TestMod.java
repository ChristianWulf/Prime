package math.groups;

public class TestMod implements TestFunction<ModRecord> {

	private final int	m;
	private final int	a;
	private final int	c;
	private int			result;

	public TestMod(int a, int m, int c) {
		this.a = a;
		this.m = m;
		this.c = c;
	}

	@Override
	public boolean check(int x) {
		result = squareAndMultiply(a, x, m);
		return result == c;
	}

	private int squareAndMultiply(int basis, int expo, int m) {
		int z = 1;
		basis = basis % m;
		while (expo != 0) {
			if (expo % 2 != 0) {
				z = z * basis % m; // Exponent ungerade
			}
			basis = basis * basis % m;
			expo = expo / 2;
		}
		return z;
	}

	@Override
	public ModRecord getRecord(int x) {
		ModRecord record = new ModRecord();
		record.a = a;
		record.x = x;
		record.m = m;
		record.result = result;
		record.c = c;
		return record;
	}
}
