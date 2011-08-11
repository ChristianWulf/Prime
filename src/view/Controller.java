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
import java.util.List;
import java.util.Vector;

import math.MathOperations;
import prime.metric.Algorithms;
import prime.metric.Metric;
import prime.metric.MetricList;
import util.StopWatch;
import util.StopWatch.MethodWithResult;
import view.helper.CheckResult;

public class Controller {

	private final GUI gui;

	public Controller(GUI gui) {
		this.gui = gui;
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

	public CheckResult check(final BigInteger testValue) {
		CheckResult result = new CheckResult();

		final BigInteger fmax = testValue.add(ONE).shiftRight(1);
		BigInteger smax = testValue.shiftRight(1);
		final BigInteger start = testValue.subtract(ONE).shiftLeft(1);
		BigInteger iterations = null;

		StopWatch<BigInteger> watch = new StopWatch<BigInteger>();
		result.elapsedTime = watch.measure(new MethodWithResult<BigInteger>() {
			@Override
			public BigInteger run() {
				return Algorithms.getIterationCount(testValue, fmax, start);
			}
		});
		iterations = watch.getResult();

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
				BigInteger max = bi.subtract(BigInteger.valueOf(5)).divide(BigInteger.valueOf(2));
				for (s = BigInteger.ZERO; s.compareTo(max) <= 0; s = s.add(BigInteger.ONE)) {
					BigInteger square = getSquareNumber(bi, s);

					BigInteger sqrt = getSqrt(square);
					if (!sqrt.equals(BigInteger.valueOf(-1))) {
						d = sqrt.subtract(s);
						break;
					}
				}
			}

			addResultRow(bi, isPrime, primes_prod, primes_sum, remainder, fermat, d, s);
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

	public void addResultRow(BigInteger bi, boolean isPrime, BigInteger primes_prod,
			BigInteger primes_sum, BigInteger remainder, boolean isFermat, BigInteger d,
			BigInteger s) {

		MetricList metrics = new MetricList(bi);

		// comment out for performance reasons
		//		vIters = MathOperations.getSquaredCongruenceIters(bi, Metric.fmax);
		//		wIters = MathOperations.getSquaredCongruenceIters(bi, Metric.smax);


		//		if (Metric.fmax_r.equals(BigInteger.valueOf(3)) && Metric.smax_r.equals(BigInteger.ONE)) {
		//			//			log.info("3-1 @ " + bi);
		//			// not existent, because rf even <=> rs odd
		//		}

		// --------------------------------------------------------
		//FIXME
		//		if (gui.cbxFilterDiff0.isSelected()) {
		//			BigInteger k = Metric.f.divide(Metric.fmax_modulo);
		//			if (ConditionChecker.isOfNegativeForm(Metric.f, Metric.fmax_modulo)) {
		//				k = k.add(ONE);
		//			}
		//			BigInteger diff_fmax = k.subtract(Metric.fmax_min);
		//			if (diff_fmax.equals(BigInteger.ZERO)) {
		//				return;
		//			}
		//		}
		//
		//		if (gui.cbxFilterDiffMax_fi.isSelected()) {
		//			BigInteger max_fi = bi.add(BigInteger.valueOf(9)).divide(BigInteger.valueOf(6));
		//			if (Metric.f.subtract(max_fi).equals(BigInteger.ZERO)) {
		//				return;
		//			}
		//		}

		// ----------------------------------------------------------

		Vector<Object> results = new Vector<Object>();
		for (Metric m : metrics) {
			results.add(m.getResult());
		}
		gui.getModel().addRow(results);
	}

	public List<String> getColumnNames() {				
		List<String> names = new ArrayList<String>();

		MetricList metrics = new MetricList(ONE);
		for (Metric m : metrics) {
			names.add(m.getColumnName());
		}

		return names;
	}

}
