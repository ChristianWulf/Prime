package prime;

import java.math.BigInteger;

public class IsPrime {

	private static final int CERTAINTY = 7;

	private IsPrime() {
		// utility class
	}

	public static boolean isPolynomialPrime(Integer n) {
		return AKSTest.isPrime(BigInteger.valueOf(n));
	}

	public static boolean isProbablyPrime(Integer n) {
		return BigInteger.valueOf(n).isProbablePrime(CERTAINTY);
	}

	public static boolean isPrime(int x) {
		int sroot = (int) Math.sqrt(x);

		for (int i = 2; i <= sroot; i++) {
			if (0 == x % i) {
				return false;
			}
		}
		return true;
	}
}
