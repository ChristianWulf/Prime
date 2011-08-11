package prime.test;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import math.MathOperations;
import prime.metric.ConditionChecker;
import util.Pair;

public class TestDistribution {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		distribution();
	}


	public static class PairComparator implements
	Comparator<Pair<BigInteger, BigInteger>> {

		@Override
		public int compare(Pair<BigInteger, BigInteger> o1,
				Pair<BigInteger, BigInteger> o2) {
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

				BigInteger fmax = n.add(BigInteger.ONE).divide(
						MathOperations.TWO);
				BigInteger smax = fmax.subtract(BigInteger.ONE);

				List<BigInteger> vIters = MathOperations
						.getSquaredCongruenceIters(n, fmax);
				List<BigInteger> wIters = MathOperations
						.getSquaredCongruenceIters(n, smax);
				List<Pair<BigInteger, BigInteger>> validPairs = MathOperations
						.getPairsVW(n, vIters, wIters);

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

				//				BigInteger fmin = MathOperations.sqrtFast(n)
				//						.add(BigInteger.ONE);
				//				BigInteger l = MathOperations.sqr(fmin).mod(n);
				//				BigInteger possD = l.divide(BigInteger.valueOf(3));
				//				BigInteger divisor = n.mod(possD);

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

	private static void distribution() throws InterruptedException {
		int maxNum = 30000;
		int numProcessors = Runtime.getRuntime().availableProcessors();
		MyThread[] threads = new MyThread[numProcessors];
		Set<Pair<BigInteger, BigInteger>> merged = new TreeSet<Pair<BigInteger, BigInteger>>(
				new PairComparator());
		int num = 0;

		// 10 ~ 0
		// 20 ~ 1
		// 30 ~ 3
		// 40 ~ 5
		// 50 ~ 6
		// 100 ~ 14
		// 500 ~ 75
		// 1000 ~ 146
		// 2000 ~ 292
		// 3000 ~ 434
		// 4000 ~ 579
		// 8000 ~ 1144 (153646 ms)
		// 9000 ~ 1286 (220953 ms)

		// 10000 ~ 12/22 (3337 ms) 0.5454545454545454
		// 20000 ~ 18/36 (19321 ms) 0.5
		// 30000 ~ 21/40 (29859 ms) 0.525

		long start = System.currentTimeMillis();

		for (int i = 0; i < numProcessors; i++) {
			threads[i] = new MyThread(3 + 2 * i, maxNum, 2 * numProcessors);
			threads[i].start();
		}
		for (int i = 0; i < numProcessors; i++) {
			threads[i].join();
			merged.addAll(threads[i].getOnes());
			num += threads[i].getNum();
		}

		long duration = System.currentTimeMillis() - start;

		String str = "";
		for (Pair<BigInteger, BigInteger> pair : merged) {
			str += pair.first + "\t = " + pair.second + "²" + "\n";
		}
		System.out.println(str);

		System.out.println(merged.size() + "/" + num);
		System.out.println((double) merged.size() / num);
		System.out.println(duration + " ms");

		// Ergebnis: Verteilung der Diff=1 liegt bei
		// ca. 29 %

		// Ergebnis: Verteilung von Non-Fermat,Non-Prime der Diff=1 liegt bei
		// ca. 50 %

		// Ergebnis: Verteilung von Non-Fermat,Non-Prime der Diff=1 und Diff mod
		// fmax=1 liegt bei:
		// 10,000 ~ 86 %
		// 20,000 ~ 80 %
		// 30,000 ~ 80 %
		// 40,000 ~ 81 %
		// 50,000 ~ 81 %
		// 60,000 ~ 81 %
		// 70,000 ~ 76 %
		// 100,000 ~ 75 %
		// 200,000 ~ 74 %
	}

}
