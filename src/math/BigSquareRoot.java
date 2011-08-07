package math;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigSquareRoot {

	private static BigDecimal ZERO = new BigDecimal("0");
	private static BigDecimal ONE = new BigDecimal("1");
	private static BigDecimal TWO = new BigDecimal("2");
	public static final int DEFAULT_MAX_ITERATIONS = 10;
	public static final int DEFAULT_SCALE = 10;

	private static BigDecimal error;
	private static int iterations;
	private static int scale = DEFAULT_SCALE;
	private static int maxIterations = DEFAULT_MAX_ITERATIONS;

	// ---------------------------------------
	// The error is the original number minus
	// (sqrt * sqrt). If the original number
	// was a perfect square, the error is 0.
	// ---------------------------------------

	public static BigDecimal getError() {
		return error;
	}

	// -------------------------------------------------------------
	// Number of iterations performed when square root was computed
	// -------------------------------------------------------------

	public static int getIterations() {
		return iterations;
	}

	// ------
	// Scale
	// ------

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	// -------------------
	// Maximum iterations
	// -------------------

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	// --------------------------
	// Get initial approximation
	// --------------------------

	private static BigDecimal getInitialApproximation(BigDecimal n) {
		BigInteger integerPart = n.toBigInteger();
		int length = integerPart.toString().length();
		if ((length % 2) == 0) {
			length--;
		}
		length /= 2;
		BigDecimal guess = ONE.movePointRight(length);
		return guess;
	}

	// ----------------
	// Get square root
	// ----------------

	public static BigDecimal get(BigInteger n) {
		return get(new BigDecimal(n));
	}

	public static BigDecimal get(BigDecimal n) {

		// Make sure n is a positive number

		if (n.compareTo(ZERO) <= 0) {
			throw new IllegalArgumentException();
		}

		BigDecimal initialGuess = getInitialApproximation(n);
		BigDecimal lastGuess = ZERO;
		BigDecimal guess = new BigDecimal(initialGuess.toString());

		// Iterate

		iterations = 0;
		boolean more = true;
		while (more) {
			lastGuess = guess;
			guess = n.divide(guess, scale, BigDecimal.ROUND_HALF_UP);
			guess = guess.add(lastGuess);
			guess = guess.divide(TWO, scale, BigDecimal.ROUND_HALF_UP);
			error = n.subtract(guess.multiply(guess));
			if (++iterations >= maxIterations) {
				more = false;
			} else if (lastGuess.equals(guess)) {
				more = error.abs().compareTo(ONE) >= 0;
			}
		}
		return guess;

	}
}