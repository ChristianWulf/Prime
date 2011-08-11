package prime.metric;

import static java.math.BigInteger.ONE;
import static math.MathOperations.sqr;
import static prime.metric.ConditionChecker.isSquareNum;

import java.math.BigInteger;

import math.MathOperations;

public class Algorithms {

	private Algorithms() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Returns -1 if <code>bi</code> is a prime (not vice versa!).<br>
	 * There are primes where this method returns 1, e.g. 19, 73 and 163.
	 * 
	 * @param bi
	 * @param fmax
	 * @param start
	 * @return
	 */
	public static BigInteger getIterationCount(BigInteger bi, BigInteger fmax, BigInteger start) {
		BigInteger iter = ONE;
		BigInteger st = start;
		BigInteger f = fmax;
		BigInteger f_sqr = sqr(f);
		BigInteger toTest = start;

		while (!isSquareNum(toTest)) {
			iter = iter.add(ONE);
			st = st.subtract(MathOperations.EIGHT);

			// BigInteger diff = f.subtract(BigInteger.ONE).shiftLeft(2); //
			// 4*(f-1)
			// f = f.subtract(TWO);
			// f_sqr = f_sqr.subtract(diff);

			f = f.subtract(MathOperations.TWO);
			f_sqr = sqr(f);

			//			log.info("f = " + f);

			toTest = f_sqr.subtract(bi).subtract(st);

			if (toTest.signum() < 0) {
				return BigInteger.valueOf(-1);
			}
		}

		return iter;
	}
}
