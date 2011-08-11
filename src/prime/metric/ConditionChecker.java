package prime.metric;

import static math.MathOperations.getSqrt;

import java.math.BigInteger;

public class ConditionChecker {

	private static BigInteger ZERO = BigInteger.valueOf(0);
	private static BigInteger ONE = BigInteger.valueOf(1);
	private static BigInteger TWO = BigInteger.valueOf(2);
	private static BigInteger FIVE = BigInteger.valueOf(5);
	private static BigInteger EIGHT = BigInteger.valueOf(8);
	private static BigInteger TEN = BigInteger.valueOf(10);

	public static boolean isLastDigit2378(BigInteger testValue) {
		byte lastDigit = testValue.mod(TEN).byteValue();
		if (lastDigit == 2 || lastDigit == 3 || lastDigit == 7 || lastDigit == 8) {
			return true;
		}
		return false;
	}

	public static boolean is1Mod8(BigInteger bi) {
		return bi.mod(EIGHT).equals(ONE);
	}

	public static boolean isSquareNum(BigInteger potSquareNum) {
		BigInteger sqrt = getSqrt(potSquareNum);
		return sqrt.signum() >= 0;
	}

	public static boolean isFermat(BigInteger x) {
		BigInteger remainer = TWO.modPow(x.subtract(ONE), x);
		return remainer.equals(ONE);
	}

	/**
	 * Dividend: <code>dividend</code><br>
	 * Divisor: <code>divisor</code>
	 */
	public static boolean isFinite(BigInteger dividend, BigInteger divisor) {
		if (dividend.equals(ZERO) || divisor.equals(ZERO)) {
			return true;
		}

		if (!divisor.equals(ONE) && !isDivisbleBy(divisor, TWO) && !isDivisbleBy(divisor, FIVE)) {
			return false; // => false is always correct
		}

		while (isDivisbleBy(divisor, TWO)) {
			divisor = divisor.divide(TWO);
		}

		while (isDivisbleBy(divisor, FIVE)) {
			divisor = divisor.divide(FIVE);
		}

		return dividend.mod(divisor).equals(ZERO);
	}

	/**
	 * Dividend: <code>dividend</code><br>
	 * Divisor: <code>divisor</code>
	 */
	public static boolean isPeriodic(BigInteger dividend, BigInteger divisor) {
		return !isFinite(dividend, divisor);
	}

	public static boolean isDivisbleBy(BigInteger number, BigInteger divisor) {
		return number.mod(divisor).equals(ZERO);
	}

	/**
	 * Returns true if <code>value</code> is of the form
	 * <code>divisor*k-r</code>; otherwise false.
	 * 
	 * @param value
	 * @param divisor
	 * @return
	 */
	public static boolean isOfNegativeForm(BigInteger value, BigInteger divisor) {
		BigInteger halfModulo = divisor.shiftRight(1);
		BigInteger modulo = value.mod(divisor);
		return modulo.compareTo(halfModulo) >= 0;

	}
}
