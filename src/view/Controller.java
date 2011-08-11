package view;

import static java.math.BigInteger.ONE;
import static math.MathOperations.getSqrt;
import static math.MathOperations.getSquareNumber;
import static math.MathOperations.sqr;
import static prime.IsPrime.isPrime;
import static prime.metric.ConditionChecker.is1Mod8;
import static prime.metric.ConditionChecker.isFermat;
import static prime.metric.ConditionChecker.isLastDigit2378;
import static prime.metric.ConditionChecker.isSquareNum;
import static view.helper.GUIShouldChecker.shouldFilterNonFermats;
import static view.helper.GUIShouldChecker.shouldFilterPrimes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import math.MathOperations;
import prime.metric.ConditionChecker;
import prime.metric.Metric;
import prime.metric.MetricCollection;
import util.MyLogger;
import util.Pair;
import view.helper.CheckResult;

public class Controller {
	private static final MyLogger log = new MyLogger();

	private final Collection<Metric> metrics;

	private final GUI gui;

	public Controller(GUI gui) {
		this.gui = gui;
		log.setLogLevel(MyLogger.OFF);
		metrics = new MetricCollection();
	}

	protected List<Double> getAllQs(BigInteger bi) {
		List<Double> qs = new ArrayList<Double>();
		int k = 2;
		double q = 0;
		do {
			BigInteger z = bi.add(BigInteger.valueOf(k * k));
			BigInteger n = BigInteger.valueOf(2 * k);
			q = z.doubleValue() / n.doubleValue();
			qs.add(q);
			k++;
		} while (q < bi.doubleValue());
		return qs;
	}

	public String getDivisorRemainderForm(BigInteger x, BigInteger divisor) {
		BigInteger[] rem = x.divideAndRemainder(divisor);
		BigInteger factor = rem[0];
		BigInteger r = rem[1];
		if (r.compareTo(divisor.shiftRight(1)) >= 0) {
			r = r.subtract(divisor);
			factor = factor.add(ONE);
		}
		String sign = r.signum() < 0 ? "-" : "+";
		return "8*" + factor + sign + r.abs();
	}

	public CheckResult check(BigInteger testValue) {
		CheckResult result = new CheckResult();

		BigInteger fmax = testValue.add(ONE).shiftRight(1);
		BigInteger smax = testValue.shiftRight(1);
		BigInteger start = testValue.subtract(ONE).shiftLeft(1);

		long startTime = System.currentTimeMillis();

		BigInteger iterations = getIterationCount(testValue, fmax, start);

		result.elapsedTime = System.currentTimeMillis() - startTime;
		result.isPrime = iterations.compareTo(BigInteger.valueOf(-1)) == 0;
		if (!result.isPrime) {
			result.f = fmax.subtract(iterations.shiftLeft(1));
			result.s = getSqrt(sqr(result.f).subtract(testValue));
		} else {
			result.f = fmax;
			result.s = smax;
		}

		return result;
	}

	/**
	 * 
	 * @return true if t/o
	 */
	public boolean show() {
		int minInt = Integer.valueOf(gui.txfMinInt.getText());
		if (minInt % 2 == 0) {
			minInt++;
		}
		final int maxInt = Integer.valueOf(gui.txfMaxInt.getText());
		BigInteger primes_prod = MathOperations.TWO;
		BigInteger primes_sum = MathOperations.TWO;

		long start = System.currentTimeMillis();

		for (int i = minInt; i <= maxInt; i += 2) {
			final BigInteger bi = BigInteger.valueOf(i);

			if (isOneFilterExecuted(bi)) {
				continue;
			}

			boolean fermat = isFermat(bi);
			if (shouldFilterNonFermats(fermat)) {
				continue;
			}

			boolean isPrime = isPrime(i);
			if (shouldFilterPrimes(isPrime)) {
				continue;
			}

			BigInteger remainder = primes_prod.mod(bi);
			BigInteger d = BigInteger.ONE;
			BigInteger s = bi.shiftRight(1);
			if (!isPrime) {
				// search another "less" definition in form of x =
				// f²-s²
				BigInteger max = bi.subtract(BigInteger.valueOf(5)).divide(
						BigInteger.valueOf(2));
				for (s = BigInteger.ZERO; s.compareTo(max) <= 0; s = s
						.add(BigInteger.ONE)) {
					BigInteger square = getSquareNumber(bi, s);

					BigInteger sqrt = getSqrt(square);
					if (!sqrt.equals(BigInteger.valueOf(-1))) {
						d = sqrt.subtract(s);
						break;
					}
				}
			}

			addResultRow(bi, isPrime, primes_prod, primes_sum, remainder,
					fermat, d, s);
			// can be prime, but is not prime
			if (isPrime) {
				// primes_prod = primes_prod.multiply(bi);
				// primes_sum = primes_sum.add(bi);
			}

			// timeout
			if (System.currentTimeMillis() - start > 7 * 1000) {
				return true;
			}
		}
		return false;
	}

	private boolean isOneFilterExecuted(BigInteger bi) {
		if (gui.cbxFilterLastDigit2378.isSelected()) {
			boolean is2378 = isLastDigit2378(bi);
			if (is2378) {
				return true;
			}
		}

		if (gui.cbxFilterNon1Mod8.isSelected()) {
			boolean is1Mod8 = is1Mod8(bi);
			if (!is1Mod8) {
				return true;
			}
		}

		if (gui.cbxFilterNonSquares.isSelected()) {
			boolean isSquare = isSquareNum(bi);
			if (!isSquare) {
				return true;
			}
		}

		if (gui.cbxFilterSquares.isSelected()) {
			boolean isSquare = isSquareNum(bi);
			if (isSquare) {
				return true;
			}
		}
		return false;
	}

	// ----------------------------------------------------------------------

	/**
	 * Returns -1 if <code>bi</code> is a prime (not vice versa!).<br>
	 * There are primes where this method returns 1, e.g. 19, 73 and 163.
	 * 
	 * @param bi
	 * @param fmax
	 * @param start
	 * @return
	 */
	private BigInteger getIterationCount(BigInteger bi, BigInteger fmax,
			BigInteger start) {
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

			log.info("f = " + f);

			toTest = f_sqr.subtract(bi).subtract(st);

			if (toTest.signum() < 0) {
				return BigInteger.valueOf(-1);
			}
		}

		return iter;
	}

	public void addResultRow(BigInteger bi, boolean isPrime,
			BigInteger primes_prod, BigInteger primes_sum,
			BigInteger remainder, boolean isFermat, BigInteger d, BigInteger s) {

		Metric.bi = bi;
		Metric.fmax = bi.add(BigInteger.ONE).divide(MathOperations.TWO);
		Metric.fmin = MathOperations.sqrtFast(bi).add(BigInteger.ONE);
		Metric.smax = bi.shiftRight(1);
		Metric.isPrime = isPrime;
		Metric.isFermat = isFermat;
		// comment out for performance reasons
		Metric.primFactors = MathOperations.getPrimFactorization(bi);
		Metric.start = bi.subtract(ONE).shiftLeft(1);
		Metric.iterations = getIterationCount(bi, Metric.fmax, Metric.start);
		Metric.f = s.add(d);
		Metric.s = s;
		Metric.fmax_minus_f = Metric.fmax.subtract(Metric.f);
		Metric.smax_minus_s = Metric.smax.subtract(Metric.s);

		List<BigInteger> vIters = new ArrayList<BigInteger>(), wIters = new ArrayList<BigInteger>();
		// comment out for performance reasons
		vIters = MathOperations.getSquaredCongruenceIters(bi, Metric.fmax);
		wIters = MathOperations.getSquaredCongruenceIters(bi, Metric.smax);
		List<Pair<BigInteger, BigInteger>> validPairs = MathOperations
				.getPairsVW(bi, vIters, wIters);
		Metric.validPairs = validPairs;

		if (validPairs.size() > 0) {
			Metric.v = validPairs.get(0).first;
			Metric.w = validPairs.get(0).second;
		} else {
			Metric.v = BigInteger.valueOf(-1);
			Metric.w = BigInteger.valueOf(-1);
		}
		Pair<BigInteger, BigInteger> fmax_rem = MathOperations
				.getSquaredRemainder(Metric.fmax);
		Metric.fmax_r = fmax_rem.first;
		Metric.fmax_modulo = fmax_rem.second;
		Metric.fmax_r_sign = MathOperations.getRemainderSign(Metric.fmax,
				Metric.fmax_r, Metric.fmax_modulo);
		Metric.fmax_min = MathOperations.getLowerBound(bi, Metric.fmax_r,
				Metric.fmax_modulo);

		Pair<BigInteger, BigInteger> smax_rem = MathOperations
				.getSquaredRemainder(Metric.smax);
		Metric.smax_r = smax_rem.first;
		Metric.smax_modulo = smax_rem.second;
		Metric.smax_r_sign = MathOperations.getRemainderSign(Metric.smax,
				Metric.smax_r, Metric.smax_modulo);
		Metric.smax_min = MathOperations.getLowerBound(bi, Metric.smax_r,
				Metric.smax_modulo);

		if (Metric.fmax_r.equals(BigInteger.valueOf(3))
				&& Metric.smax_r.equals(BigInteger.ONE)) {
			log.info("3-1 @ " + bi);
			// not existent, because rf even <=> rs odd
		}

		// --------------------------------------------------------

		if (gui.cbxFilterDiff0.isSelected()) {
			BigInteger k = Metric.f.divide(Metric.fmax_modulo);
			if (ConditionChecker.isOfNegativeForm(Metric.f, Metric.fmax_modulo)) {
				k = k.add(ONE);
			}
			BigInteger diff_fmax = k.subtract(Metric.fmax_min);
			if (diff_fmax.equals(BigInteger.ZERO)) {
				return;
			}
		}

		if (gui.cbxFilterDiffMax_fi.isSelected()) {
			BigInteger max_fi = bi.add(BigInteger.valueOf(9)).divide(
					BigInteger.valueOf(6));
			if (Metric.f.subtract(max_fi).equals(BigInteger.ZERO)) {
				return;
			}
		}

		// ----------------------------------------------------------

		Vector<Object> results = new Vector<Object>();
		for (Metric m : metrics) {
			results.add(m.getResult());
		}
		gui.getModel().addRow(results);
	}

	public Collection<Metric> getMetrics() {
		return metrics;
	}
}
