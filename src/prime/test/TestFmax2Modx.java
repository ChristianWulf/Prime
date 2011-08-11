package prime.test;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import math.MathOperations;
import prime.metric.ConditionChecker;
import util.Pair;

public class TestFmax2Modx {

	public static class PairComparator implements Comparator<Pair<BigInteger, BigInteger>> {

		@Override
		public int compare(Pair<BigInteger, BigInteger> o1, Pair<BigInteger, BigInteger> o2) {
			return o1.first.compareTo(o2.first);
		}

	}

	public static class MyThread extends Thread {

		private final int startValue;
		private final int maxNum;
		private final List<Pair<BigInteger, BigInteger>> ones = new LinkedList<Pair<BigInteger, BigInteger>>();
		private final int inc;
		private int num;

		public MyThread(int startValue, int maxNum, int inc) {
			this.startValue = startValue;
			this.maxNum = maxNum;
			this.inc = inc;
		}

		@Override
		public void run() {
			num = 0;
			for (int i = startValue; i < maxNum; i += inc) {
				BigInteger n = BigInteger.valueOf(i);

				// gib alle fermat-zahlen aus, die keine primzahlen sind
				boolean fermat = ConditionChecker.isFermat(n);
				if (!fermat) {
					continue;
				} else {
					boolean isPrime = n.isProbablePrime(10);// Filter.isPrime(i);
					// //
					if (isPrime) {
						continue;
					}
				}
				num++;

				BigInteger fmax = n.add(BigInteger.ONE).divide(MathOperations.TWO);
				BigInteger smax = fmax.subtract(BigInteger.ONE);

				List<BigInteger> vIters = MathOperations.getSquaredCongruenceIters(n, fmax);
				List<BigInteger> wIters = MathOperations.getSquaredCongruenceIters(n, smax);
				List<Pair<BigInteger, BigInteger>> validPairs = MathOperations.getPairsVW(n,
						vIters, wIters);

				BigInteger f0 = validPairs.get(0).first;
				BigInteger s0 = validPairs.get(0).second;

				/*
				 * for (Pair<BigInteger, BigInteger> pair : validPairs) {
				 * BigInteger fi = pair.first; if (!fi.equals(fmax)) {
				 * BigInteger diff = sqr(fi).mod(n).subtract(sqr(fi).mod(fmax));
				 * if (diff.equals(BigInteger.ONE)) { getOnes().add(Pair.of(n,
				 * fi)); break; } else if
				 * (diff.mod(fmax).equals(BigInteger.ONE)) {
				 * getOnes().add(Pair.of(n, fi)); break; } } }
				 */
				// BigInteger metric = sqr(fmax).mod(n);
				// double sqrtd = Math.sqrt(metric.doubleValue());
				// int sqrti = MathOperations.sqrt(metric.intValue());
				// if (sqrti*sqrti == metric.intValue()){
				// getOnes().add(Pair.of(n, metric));
				// }

				// BigInteger fmin = MathOperations.sqrtFast(n)
				// .add(BigInteger.ONE);
				// BigInteger l = MathOperations.sqr(fmin).mod(n);
				// BigInteger possD = l.divide(BigInteger.valueOf(3));
				// BigInteger divisor = n.mod(possD);

				BigInteger difff = fmax.subtract(f0);
				BigInteger diffs = smax.subtract(s0);

				if (difff.compareTo(diffs) < 1) {
					getOnes().add(Pair.of(n, f0));
				}
			}
		}

		public List<Pair<BigInteger, BigInteger>> getOnes() {
			return ones;
		}

		public int getNum() {
			return num;
		}
	}

	private static final BigInteger TWO = BigInteger.valueOf(2);

	public static void main(String[] args) throws InterruptedException {
		fmax2Modx();
	}

	private static void fmax2Modx() {
		int maxNum = 1000000;
		int numProcessors = Runtime.getRuntime().availableProcessors();
		final List<Pair<Integer, Integer>> sqrts = new LinkedList<Pair<Integer, Integer>>();

		long start = System.currentTimeMillis();

		int fermat_num = 0;
		for (int x = 7; x < maxNum; x++) {
			BigInteger xb = BigInteger.valueOf(x);
			if (ConditionChecker.isFermat(xb) /* && !xb.isProbablePrime(10) */) {
				BigInteger fmax = xb.add(BigInteger.ONE).divide(TWO);
				BigInteger remainder = MathOperations.sqr(fmax).mod(xb);
				BigInteger sqrt = MathOperations.sqrtFast(remainder);
				if (MathOperations.sqr(sqrt).equals(remainder)) {
					sqrts.add(Pair.of(x, sqrt.intValue()));
				}
				fermat_num++;
			}
		}

		long duration = System.currentTimeMillis() - start;

		System.out.println("duration: " + duration + " ms");
		System.out.println("# false fermats: " + sqrts.size());
		System.out.println("# fermats incl. primes: " + fermat_num);
		System.out.println("" + (1.0 * sqrts.size() / fermat_num));

		// duration: 502 ms
		// # false fermats: 7
		// # fermats incl. primes: 1248
		// 0.005608974358974359

		// duration: 1820 ms
		// # false fermats: 12
		// # fermats incl. primes: 9667
		// 0.0012413365056377365

		// duration: 11228 ms
		// # false fermats: 12
		// # fermats incl. primes: 78740
		// 1.524003048006096E-4
	}

}
