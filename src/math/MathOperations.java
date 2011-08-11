package math;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static prime.metric.ConditionChecker.isLastDigit2378;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Pair;

public class MathOperations {

	public static final BigInteger	TWO		= BigInteger.valueOf(2);
	public static final BigInteger	FOUR	= BigInteger.valueOf(4);
	public static final BigInteger	EIGHT	= BigInteger.valueOf(8);
	public static final BigInteger	SIXTEEN	= BigInteger.valueOf(16);

	public static BigInteger sqr(BigInteger val) {
		return val.multiply(val);
	}

	/**
	 * Returns -1 if <code>testValue</code> is not a square number.
	 * 
	 * @param testValue
	 * @return
	 */
	public static BigInteger getSqrt(final BigInteger testValue) {
		boolean isLastDigit2378 = isLastDigit2378(testValue);
		if (isLastDigit2378) {
			return BigInteger.valueOf(-1);
		}

		BigInteger sqrt = sqrtFast(testValue);
		if (testValue.compareTo(sqr(sqrt)) != 0) {
			return BigInteger.valueOf(-1);
		} else {
			return sqrt;
		}
	}

	private static BigInteger sqrt(final BigInteger number) {
		BigInteger square = BigInteger.ZERO;
		BigInteger i = BigInteger.ZERO;
		do {
			if (square.equals(number)) {
				return i;
			}
			// BigInteger diff = i.shiftLeft(1).add(BigInteger.ONE);
			// square = square.add(diff);
			i = i.add(BigInteger.ONE);
			square = i.multiply(i);
		} while (square.compareTo(number) <= 0);

		return i.negate();
	}

	public static BigInteger getSquareNumber(BigInteger bi, BigInteger s) {
		return bi.add(s.multiply(s));
	}

	public static Map<BigInteger, BigInteger> getPrimFactorization(BigInteger number) {
		Map<BigInteger, BigInteger> primFactors = new HashMap<BigInteger, BigInteger>();
		getPrimFactorizationRec(primFactors, number);
		return primFactors;
	}

	private static void getPrimFactorizationRec(Map<BigInteger, BigInteger> primFactors,
			BigInteger number) {
		for (int i = 2; i <= number.longValue(); i++) {
			BigInteger val = BigInteger.valueOf(i);
			if (number.mod(val).equals(BigInteger.ZERO)) {
				BigInteger amount = primFactors.get(val);
				if (amount == null) {
					primFactors.put(val, BigInteger.ONE);
				} else {
					primFactors.put(val, amount.add(BigInteger.ONE));
				}
				number = number.divide(val);
				if (number.longValue() > 1) {
					getPrimFactorizationRec(primFactors, number);
				}
				break;
			}
		}

	}

	/**
	 * Returns the nearest sqrt below the exact sqrt.<br>
	 * Example: sqrtFast(99)=9
	 * 
	 * @param num
	 * @return
	 */
	public static BigInteger sqrtFast(BigInteger num) {
		BigInteger op = num;
		BigInteger res = ZERO;
		BigInteger one = ONE.shiftLeft(14); // The second-to-top bit is set:
		// 1L<<30 for long

		// "one" starts at the highest power of four <= the argument.
		while (one.compareTo(op) > 0) {
			one = one.shiftRight(2);
		}

		while (one.compareTo(ZERO) != 0) {
			if (op.compareTo(res.add(one)) >= 0) {
				op = op.subtract(res.add(one));
				res = res.shiftRight(1).add(one);
			} else {
				res = res.shiftRight(1);
			}
			one = one.shiftRight(2);
		}
		return res;
	}

	public static int sqrt(int num) {
		int op = num;
		int res = 0;
		int one = 1 << 14; // The second-to-top bit is set: 1L<<30 for long

		// "one" starts at the highest power of four <= the argument.
		while (one > op) {
			one >>= 2;
		}

		while (one != 0) {
			if (op >= res + one) {
				op -= res + one;
				res = (res >> 1) + one;
			} else {
				res >>= 1;
			}
			one >>= 2;
		}
		return res;
	}

	/**
	 * @param testValue
	 * @param r
	 *            +/-
	 * @param modulo
	 *            8 or 4
	 * @return
	 */
	public static BigInteger getLowerBound(BigInteger testValue, BigInteger r, BigInteger modulo) {
		// FIXME replace double by an impl of sqrt for BigDecimal, because
		// double has a bounded range
		double testValue_sqrt = Math.sqrt(testValue.doubleValue());
		// double min = (testValue_sqrt - r.doubleValue()) /
		// modulo.doubleValue();
		// return upper_Gauss(min);
		BigInteger upper = upper_Gauss(testValue_sqrt);
		BigInteger[] rem = upper.divideAndRemainder(modulo);
		if (rem[1].compareTo(r) > 0) {
			return rem[0].add(ONE);
		} else {
			return rem[0];
		}
	}

	/**
	 * @param base
	 * @param r
	 *            with the correct sign
	 * @param modulo
	 * @return -1, if r is negative, otherwise it is positive.
	 */
	public static int getRemainderSign(BigInteger base, BigInteger r, BigInteger modulo) {
		return base.subtract(r).mod(modulo).equals(BigInteger.ZERO) ? -1 : +1;
	}

	public static BigInteger upper_Gauss(double min) {
		return BigInteger.valueOf((long) Math.ceil(min));
	}

	public static BigInteger getUpperBound(BigInteger base, BigInteger r, BigInteger modulo) {
		return base.add(r).divide(modulo);
	}

	public static Pair<BigInteger, BigInteger> getSquaredRemainder(BigInteger base) {
		final BigInteger base_sqrd = sqr(base);
		/*
		 * y1 \in {0,1,..,15} at least, more precisely y1 \in {0,1,4,9} because x^2==y1 mod 16
		 * always results in one of these four remainders.
		 */
		int y1 = base_sqrd.mod(SIXTEEN).intValue();
		BigInteger r;
		BigInteger modulo;
		switch (y1) {
		case 0:
			r = BigInteger.valueOf(0);
			modulo = FOUR;
			break;
		case 1:
			r = BigInteger.valueOf(1);
			modulo = EIGHT;
			break;
		case 4:
			r = BigInteger.valueOf(2);
			modulo = EIGHT;
			break;
		case 9:
			r = BigInteger.valueOf(3);
			modulo = EIGHT;
			break;
		default:
			return null; // cannot happen; only for compiler logic
		}

		return Pair.of(r, modulo);
	}

	public static List<BigInteger> getSquaredCongruenceIters(BigInteger testValue, BigInteger base) {
		// log.info("(testValue, base) = (" + testValue + "," + base + ")");

		// compute r and corresponding modulo
		Pair<BigInteger, BigInteger> rem = getSquaredRemainder(base);
		BigInteger r = rem.first;
		BigInteger modulo = rem.second;

		// compute r's sign
		final int sign = getRemainderSign(base, r, modulo);
		if (sign < 0) {
			r = r.negate();
		}
		BigInteger min = getLowerBound(testValue, r, modulo);
		BigInteger max = getUpperBound(base, r, modulo);

		ArrayList<BigInteger> iters = new ArrayList<BigInteger>();

		BigInteger sequence = r;

		if (sign < 0) {
			r = r.negate();
		}
		final BigInteger two_r = r.multiply(TWO);
		final BigInteger dist1 = modulo.subtract(two_r);
		final BigInteger dist2 = r.equals(BigInteger.ZERO) ? FOUR : two_r;

		sequence = r;
		iters.add(sequence);
		sequence = sequence.add(dist1);
		iters.add(sequence);
		for (BigInteger i = min.add(BigInteger.ONE); i.compareTo(max) <= 0; i = i
				.add(BigInteger.ONE)) {
			sequence = sequence.add(dist2);
			iters.add(sequence);
			sequence = sequence.add(dist1);
			iters.add(sequence);
		}

		// log
		String logText = "";
		for (BigInteger i : iters) {
			logText += i + ",";
		}

		return iters;
	}

	public static List<Pair<BigInteger, BigInteger>> getPairsVW(BigInteger testValue,
			List<BigInteger> vIters, List<BigInteger> wIters) {
		List<Pair<BigInteger, BigInteger>> pairs = new ArrayList<Pair<BigInteger, BigInteger>>();

		for (BigInteger v : vIters) {
			BigInteger v_sqrd = sqr(v);
			for (BigInteger w : wIters) {
				BigInteger w_sqrd = sqr(w);
				// if w² >= v² then v²-w² <= 0 != testValue
				if (w_sqrd.compareTo(v_sqrd) >= 0) {
					break; // out the w-loop
				}
				// if v²-w² = testValue then another form of a squared
				// difference is found
				if (v_sqrd.subtract(w_sqrd).compareTo(testValue) == 0) {
					pairs.add(Pair.of(v, w));
				}
			}
		}

		return pairs;
	}

	public static int ggT(int a, int b) {
		while (b != 0) {
			int h = a % b;
			a = b;
			b = h;
		}
		return a;
	}

}
