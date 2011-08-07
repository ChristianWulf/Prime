package prime.db.entity;

import java.math.BigInteger;

public class GGTi {

	private final BigInteger	n;
	private final BigInteger[]	ggti;
	/**
	 * for performance purpose
	 */
	private int					currentLength;

	public GGTi(BigInteger n, int numGGT) {
		this.n = n;
		ggti = new BigInteger[numGGT];
		currentLength = 0;
	}

	public BigInteger getGGTi(int i) {
		if (ggti[i] == null) {
			ggti[i] = ggT(n, i);
			currentLength++;
		}
		return ggti[i];
	}

	public BigInteger[] getAllGGT() {
		if (currentLength < ggti.length) {
			for (int i = 0; i < ggti.length; i++) {
				ggti[i] = ggT(n, i);
			}
		}
		return ggti;
	}

	public static BigInteger ggT(BigInteger n, int i) {
		// TODO Auto-generated method stub
		return null;
	}

}
