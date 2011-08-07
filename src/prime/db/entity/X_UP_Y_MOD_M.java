package prime.db.entity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import util.Pair;

public class X_UP_Y_MOD_M {

	private int	x;
	private int	y;
	private int	m;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public int computeOverM(int m) {
		BigInteger rest = BigInteger.valueOf(x)
				.modPow(BigInteger.valueOf(y), BigInteger.valueOf(m));
		return rest.intValue();
	}

	public List<Pair<Integer, Integer>> computeALL(int max) {
		List<Pair<Integer, Integer>> rests = new ArrayList<Pair<Integer, Integer>>(max);
		for (int modulo = 2; modulo <= max; modulo++) {
			int rest = computeOverM(modulo);

			rests.add(Pair.of(modulo, rest));
		}
		return rests;
	}
}
