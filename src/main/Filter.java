package main;

import static math.MathOperations.getSqrt;

import java.math.BigInteger;

import view.GUI;

public class Filter {

	private static final BigInteger	TWO	= BigInteger.valueOf(2);

	public static boolean isLastDigit2378(BigInteger testValue) {
		byte lastDigit = testValue.mod(BigInteger.TEN).byteValue();
		if (lastDigit == 2 || lastDigit == 3 || lastDigit == 7 || lastDigit == 8) {
			return true;
		}
		return false;
	}

	public static boolean is1Mod8(BigInteger bi) {
		return bi.mod(BigInteger.valueOf(8)).equals(BigInteger.ONE);
	}

	public static boolean isSquareNum(BigInteger potSquareNum) {
		BigInteger sqrt = getSqrt(potSquareNum);
		return sqrt.signum() >= 0;
	}

	public static boolean isFermat(BigInteger x) {
		BigInteger remainer = TWO.modPow(x.subtract(BigInteger.ONE), x);
		return remainer.equals(BigInteger.ONE);
	}

	/**
	 * Dividend: <code>dividend</code><br>
	 * Divisor: <code>divisor</code>
	 */
	public static boolean isFinite(BigInteger dividend, BigInteger divisor) {
		if (dividend.equals(BigInteger.ZERO) || divisor.equals(BigInteger.ZERO)) {
			return true;
		}

		final BigInteger two = BigInteger.valueOf(2);
		final BigInteger five = BigInteger.valueOf(5);

		if (!divisor.equals(BigInteger.ONE) && !isDivisbleBy(divisor, two)
				&& !isDivisbleBy(divisor, five)) {
			return false; // => false is always correct
		}

		while (isDivisbleBy(divisor, two)) {
			divisor = divisor.divide(two);
		}

		while (isDivisbleBy(divisor, five)) {
			divisor = divisor.divide(five);
		}

		return dividend.mod(divisor).equals(BigInteger.ZERO);
	}

	/**
	 * Dividend: <code>dividend</code><br>
	 * Divisor: <code>divisor</code>
	 */
	public static boolean isPeriodic(BigInteger dividend, BigInteger divisor) {
		return !isFinite(dividend, divisor);
	}

	public static boolean isDivisbleBy(BigInteger number, BigInteger divisor) {
		return number.mod(divisor).equals(BigInteger.ZERO);
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

	// -------------------------------------------------------------------------

	public static boolean shouldFilterNonFermats(boolean fermat) {
		return GUI.cbxFilterNonFermats.isSelected() && !fermat;
	}

	public static boolean shouldFilterPrimes(boolean isPrime) {
		return GUI.cbxFilterPrimes.isSelected() && isPrime;
	}
}
