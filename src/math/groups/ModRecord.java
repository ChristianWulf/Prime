package math.groups;

public class ModRecord {

	public int	a, x, m, c;
	public int	result;

	@Override
	public String toString() {
		return a + "^" + x + " mod " + m + " = " + result + " (" + c + ")";
	}
}
