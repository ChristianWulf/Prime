package prime.metric;

import static java.math.BigInteger.ONE;
import static math.MathOperations.sqr;

import java.math.BigInteger;
import java.util.ArrayList;

import math.MathOperations;
import util.Pair;

public class MetricCollection extends ArrayList<Metric> {

	public MetricCollection() {
		addMetrics();
	}

	private void addMetrics() {
		add(new Metric("x") {
			@Override
			public String getResult() {
				return bi.toString();
			}
		});
		add(new Metric("prime?") {
			@Override
			public String getResult() {
				return isPrime ? "yes" : "no";
			}
		});
		add(new Metric("prim fac") {
			@Override
			public String getResult() {
				if (primFactors.isEmpty()) {
					return "-";
				}
				String primFactorsStr = "";
				for (BigInteger key : primFactors.keySet()) {
					primFactorsStr += key + "^" + primFactors.get(key) + ",";
				}
				return primFactorsStr.substring(0, primFactorsStr.length() - 1);
			}
		});
		add(new Metric("#prim factors") {
			@Override
			public String getResult() {
				if (primFactors.isEmpty()) {
					return "-";
				}
				BigInteger primFactorsNum = BigInteger.ZERO;
				for (BigInteger val : primFactors.values()) {
					primFactorsNum = primFactorsNum.add(val);
				}
				return primFactorsNum.toString();
			}
		});
		add(new Metric("minimum") {
			@Override
			public String getResult() {
				if (primFactors.isEmpty()) {
					return "-";
				}
				BigInteger minimum = bi;
				for (BigInteger key : primFactors.keySet()) {
					if (key.compareTo(minimum) < 0) {
						minimum = key;
					}
				}
				return minimum.toString();
			}
		});
		// add(new Metric("start") {
		// @Override
		// public String getResult() {
		// return start.toString();
		// }
		// });
		add(new Metric("iterations") {
			@Override
			public String getResult() {
				if (max.compareTo(iterations) < 0) {
					max = iterations;
				}
				return iterations.toString();
			}
		});

		add(new Metric("x/iterations") {
			@Override
			public String getResult() {
				return String.format("%.3f", Metric.bi.doubleValue()
						/ iterations.doubleValue());
			}
		});

		// add(new Metric("x mod (f-s)") {
		// @Override
		// public String getResult() {
		// return bi.mod(f.subtract(s)).toString();
		// }
		// });
		// add(new Metric("start-8*iterations") {
		// @Override
		// public String getResult() {
		// return start.subtract(iterations.shiftLeft(3)).toString();
		// }
		// });
		// add(new Metric("fmax/f") {
		// @Override
		// public String getResult() {
		// BigInteger result = fmax.divide(f);
		// if (min.compareTo(result) > 0) {
		// min = result;
		// }
		// if (max.compareTo(result) < 0) {
		// max = result;
		// }
		// return result.toString();
		// }
		// });
		// add(new Metric("f") {
		// @Override
		// public String getResult() {
		// if (max.compareTo(f) < 0) {
		// max = f;
		// }
		// return f.toString();
		// }
		// });/*
		// * add(new Metric("d") {
		// *
		// * @Override public String getResult() { return
		// f.subtract(s).toString(); } });
		// */
		// add(new Metric("s") {
		// @Override
		// public String getResult() {
		// if (max.compareTo(s) < 0) {
		// max = s;
		// }
		// return s.toString();
		// }
		// });
		add(new Metric("(f,s)") {
			@Override
			public String getResult() {
				String fs = "";
				for (Pair<BigInteger, BigInteger> pair : validPairs) {
					fs += "(" + pair.first + "," + pair.second + ")";
				}
				return fs;
			}
		});
		// add(new Metric("fi-si") {
		// @Override
		// public String getResult() {
		// String fs = "";
		// for (Pair<BigInteger, BigInteger> pair : validPairs) {
		// fs += "(" + pair.first.subtract(pair.second) + ")";
		// }
		// return fs;
		// }
		// });
		// add(new Metric("fi-fmin") {
		// @Override
		// public String getResult() {
		// String fs = "";
		// BigInteger fmin = MathOperations.sqrtFast(bi).add(BigInteger.ONE);
		// for (Pair<BigInteger, BigInteger> pair : validPairs) {
		// fs += "(" + pair.first.subtract(fmin) + ")";
		// }
		// return fs;
		// }
		// });
		// add(new Metric("fi²-fmin²") {
		// @Override
		// public String getResult() {
		// String fs = "";
		// BigInteger fmin = MathOperations.sqrtFast(bi).add(BigInteger.ONE);
		// BigInteger fmin_sqr = sqr(fmin);
		// for (Pair<BigInteger, BigInteger> pair : validPairs) {
		// fs += "(" + sqr(pair.first).subtract(fmin_sqr) + ")";
		// }
		// return fs;
		// }
		// });
		add(new Metric("2^(n-1) mod 53") {
			@Override
			public String getResult() {
				return BigInteger
						.valueOf(2)
						.modPow(bi.subtract(BigInteger.ONE),
								BigInteger.valueOf(53)).toString();
			}
		});
		add(new Metric("fmax") {
			@Override
			public String getResult() {
				return fmax.toString();
			}
		});
		add(new Metric("fmin") {
			@Override
			public String getResult() {
				return fmin.toString();
			}
		});
		// add(new Metric("q of n=2qk-k²") {
		// @Override
		// public String getResult() {
		// StringBuilder qsAsString = new StringBuilder();
		// for (double q : getAllQs(Metric.bi)) {
		// qsAsString.append(q + "|");
		// }
		// return qsAsString.toString();
		// }
		// });
		// add(new Metric("index of q") {
		// @Override
		// public String getResult() {
		// List<Double> qs = getAllQs(Metric.bi);
		// for (int index = 0; index < qs.size(); index++) {
		// double q = qs.get(index);
		// // if q is even then return q
		// if (q - Math.floor(q) == 0) {
		// if (index > max.intValue()) {
		// max = BigInteger.valueOf(index);
		// }
		// return String.valueOf(index);
		// }
		// }
		// return "-";
		// }
		// });
		//		add(new Metric("n=2qk*k²") {
		//			@Override
		//			public String getResult() {
		//				StringBuilder qs = new StringBuilder();
		//				int k = 2;
		//				double q = 0;
		//				int kStart = k;
		//				boolean started = false;
		//				BigInteger savedQuo = BigInteger.ZERO;
		//				do {
		//					// x+k²
		//					BigInteger z = bi.add(BigInteger.valueOf(k * k));
		//					// 2k
		//					BigInteger n = BigInteger.valueOf(2 * k);
		//					// x+k² mod 2k == 0 ?
		//					if (z.remainder(n).equals(BigInteger.ZERO)) {
		//						BigInteger curQuo = z.divide(n);
		//						if (!started) {
		//							savedQuo = curQuo;
		//							kStart = k;
		//							started = true;
		//						} else {
		//							if (curQuo.equals(savedQuo)) {
		//								break;
		//							}
		//						}
		//					}
		//					q = z.doubleValue() / n.doubleValue();
		//					qs.append(q + "|");
		//					k++;
		//				} while (q < bi.doubleValue());
		//				double integral = 0.5 * Math.log(k) * bi.doubleValue() + 0.25
		//						* (k * k - kStart * kStart);
		//				double diffIntegral = (k - kStart) * savedQuo.doubleValue()
		//						- integral;
		//				return "(" + kStart + "," + k + ")=" + diffIntegral;
		//			}
		//		});
		// add(new Metric("(a,b)") {
		// @Override
		// public String getResult() {
		// String ab = "";
		// for (Pair<BigInteger, BigInteger> pair : validPairs) {
		// ab += "(" + fmax.subtract(pair.first) + "," +
		// fmax.subtract(pair.second) + ")";
		// }
		// return ab;
		// }
		// });
		add(new Metric("f/s") {
			@Override
			public String getResult() {
				if (validPairs.size() > 0) {
					Pair<BigInteger, BigInteger> p = validPairs.get(validPairs
							.size() - 1);
					BigInteger fl = p.first;
					BigInteger sl = p.second;
					if (sl.equals(BigInteger.ZERO)) {
						return "";
					}
					return String.format("%.2f",
							fl.doubleValue() / sl.doubleValue());
				} else
					return "";
			}
		});
		add(new Metric("min(fmax)") {
			@Override
			public String getResult() {
				return fmax_min.toString();
			}
		});
		add(new Metric("min(smax)") {
			@Override
			public String getResult() {
				return smax_min.toString();
			}
		});
		add(new Metric("min(smax)+/-1/min(fmax)") {
			// 1/2, 1 oder 2
			@Override
			public String getResult() {
				if (fmax_min.equals(BigInteger.ZERO)) {
					return BigInteger.ZERO.toString();
				}
				BigInteger smax_min_temp = smax_min;
				if (smax_min_temp.compareTo(fmax_min) > 0) {
					return smax_min_temp.add(ONE).divide(fmax_min).toString();
				}
				// return smax_min.divide(fmax_min).toString();
				return "" + smax_min_temp.doubleValue()
						/ fmax_min.doubleValue();
			}
		});
		add(new Metric("fmin² mod x") {
			@Override
			public String getResult() {
				BigInteger fmin = MathOperations.sqrtFast(bi).add(
						BigInteger.ONE);
				return sqr(fmin).mod(bi).toString();
			}
		});
		add(new Metric("fmax² mod x") {
			@Override
			public String getResult() {
				BigInteger fmax2_x = sqr(fmax).mod(bi);
				return "" + fmax2_x.doubleValue();
			}
		});
		add(new Metric("sqrt(fmin² mod x)") {
			@Override
			public String getResult() {
				BigInteger fmin2_x = sqr(fmin).mod(bi);
				return "" + Math.sqrt(fmin2_x.doubleValue());
			}
		});
		add(new Metric("fi² mod x") {
			@Override
			public String getResult() {
				String fmodn = "";
				for (Pair<BigInteger, BigInteger> pair : validPairs) {
					fmodn += sqr(pair.first).mod(bi) + ",";
				}
				return fmodn;
			}
		});
		add(new Metric("fi² mod fmax") {
			@Override
			public String getResult() {
				String fmodn = "";
				for (Pair<BigInteger, BigInteger> pair : validPairs) {
					fmodn += sqr(pair.first).mod(fmax) + ",";
				}
				return fmodn;
			}
		});
		add(new Metric("sqrt(fmax² mod x) / sqrt(fi² mod x)") {
			@Override
			public String getResult() {
				String fmodn = "";
				for (Pair<BigInteger, BigInteger> pair : validPairs) {
					fmodn += Math.sqrt(sqr(fmax).mod(bi).doubleValue())
							/ Math.sqrt(sqr(pair.second).mod(bi).doubleValue())
							+ ",";
				}
				return fmodn;
			}
		});
		// add(new Metric("smax/s") {
		// @Override
		// public String getResult() {
		// if (s.equals(ZERO)) {
		// return BigInteger.valueOf(-1).toString();
		// }
		// String smax_s = "";
		// for (Pair<BigInteger, BigInteger> pair : validPairs) {
		// double quotient = smax.doubleValue() / pair.second.doubleValue();
		// smax_s += String.format("%.2f", quotient) + "|";
		// }
		// return smax_s;
		// }
		// });

		// add(new Metric("(smax²-s²):16") {
		// // gilt auch für fmax und f
		// @Override
		// public String getResult() {
		// String si = "";
		// for (Pair<BigInteger, BigInteger> pair : validPairs) {
		// BigInteger diff = sqr(smax).subtract(sqr(pair.second));
		// diff = diff.divide(BigInteger.valueOf(16));
		// si += diff + "|";
		// }
		// return si;
		// }
		// });
		//
		// add(new Metric("iter:[(smax²-s²):16]") {
		// // gilt auch für fmax und f
		// @Override
		// public String getResult() {
		// String si = "";
		// for (Pair<BigInteger, BigInteger> pair : validPairs) {
		// BigInteger diff = sqr(smax).subtract(sqr(pair.second));
		// diff = diff.divide(BigInteger.valueOf(16));
		// diff = diff.divide(iterations);
		// si += diff + "|";
		// }
		// return si;
		// }
		// });

		// add(new Metric("i/s") {
		// @Override
		// public String getResult() {
		// if (s.equals(ZERO)) {
		// return BigInteger.valueOf(-1).toString();
		// }
		//
		// String is = "";
		// for (Pair<BigInteger, BigInteger> pair : validPairs) {
		// BigInteger myS = pair.second;
		// BigInteger i = sqr(smax).subtract(sqr(myS));
		// double quotient = i.doubleValue() / (16 * myS.doubleValue());
		// is += String.format("%.2f", quotient) + "|";
		// }
		// // compute max
		// Pair<BigInteger, BigInteger> last = validPairs.get(0);
		// BigInteger i = sqr(smax).subtract(sqr(last.second));
		// double quotient = i.doubleValue() / (16 * last.second.doubleValue());
		// if (quotient > max.doubleValue()) {
		// max = new BigDecimal(quotient).toBigInteger();
		// }
		// return is;
		// }
		// });

		// add(new Metric("s/i") {
		// @Override
		// public String getResult() {
		// String si = "";
		// for (Pair<BigInteger, BigInteger> pair : validPairs) {
		// double quotient = pair.second.doubleValue() /
		// iterations.doubleValue();
		// si += String.format("%.2f", quotient) + "|";
		// }
		// return si;
		// }
		// });
		add(new Metric("fmax² mod x-fi² mod x") {
			@Override
			public String getResult() {
				if (validPairs.size() > 0) {
					Pair<BigInteger, BigInteger> f0 = validPairs.get(0);
					return sqr(fmax).mod(bi).subtract(sqr(f0.first).mod(bi))
							.toString();
				} else {
					return "";
				}
			}
		});
		add(new Metric("fmax² mod x/fi² mod x") {
			@Override
			public String getResult() {
				if (validPairs.size() > 0) {
					Pair<BigInteger, BigInteger> f0 = validPairs.get(0);
					double result = sqr(fmax).mod(bi).doubleValue()
							/ sqr(f0.first).mod(bi).doubleValue();
					return "" + result;
				} else {
					return "";
				}
			}
		});
		add(new Metric("fi² mod x - fi² mod fmax") {
			@Override
			public String getResult() {
				if (validPairs.size() > 0) {
					BigInteger f0 = validPairs.get(0).first;
					return sqr(f0).mod(bi).subtract(sqr(f0).mod(fmax))
							.toString();
				} else {
					return "";
				}
			}
		});
		add(new Metric("fmax-f") {
			@Override
			public String getResult() {
				return fmax_minus_f.toString();
			}
		});
		add(new Metric("smax-s") {
			@Override
			public String getResult() {
				return smax_minus_s.toString();
			}
		});
		// add(new Metric("(smax-s)mod(fmax-f)") {
		// @Override
		// public String getResult() {
		// return fmax_minus_f.signum() > 0 ?
		// smax_minus_s.mod(fmax_minus_f).toString() : "-";
		// }
		// });

		/*
		 * add(new Metric("f mod s") {
		 * 
		 * @Override public String getResult() { return (s.signum() > 0) ?
		 * f.mod(s).toString() : "-"; } }); add(new Metric("iter mod f")
		 * {
		 * 
		 * @Override public String getResult() { return
		 * iterations.mod(f).toString(); } }); add(new
		 * Metric("fmax mod f") {
		 * 
		 * @Override public String getResult() { return fmax.mod(f).toString();
		 * } }); add(new Metric("smax mod s") {
		 * 
		 * @Override public String getResult() { return (s.signum() > 0) ?
		 * smax.mod(s).toString() : "-"; } }); add(new
		 * Metric("fmax² - f²") { // es gilt: fmax² - f² = smax² - s²
		 * 
		 * @Override public String getResult() { return
		 * sqr(fmax).subtract(sqr(f)).toString(); } });
		 */
		/*
		 * add(new Metric("(fmax² - f²) / 16") {
		 * 
		 * @Override public String getResult() { return
		 * sqr(smax).subtract(sqr(s)).divide(BigInteger.valueOf(16)).toString();
		 * } }); add(new Metric("(fmax²-f²) mod 16") { // es gilt:
		 * (fmax²-f² == smax²-s² == 0) mod 16
		 * 
		 * @Override public String getResult() { return
		 * sqr(fmax).subtract(sqr(f)).mod(BigInteger.valueOf(16)).toString(); }
		 * });
		 */
		// add(new Metric("v") {
		// @Override
		// public String getResult() {
		// return v.toString();
		// }
		// });
		// add(new Metric("w") {
		//
		// @Override
		// public String getResult() {
		// return w.toString();
		// }
		// });
		// add(new Metric("min(fmax)") {
		// @Override
		// public String getResult() {
		// return fmax_min.toString();
		// }
		// });
		// add(new Metric("min(smax)") {
		// @Override
		// public String getResult() {
		// return smax_min.toString();
		// }
		// });
		// add(new Metric("r(fmax)") {
		// @Override
		// public String getResult() {
		// return fmax_r.multiply(BigInteger.valueOf(fmax_r_sign)).toString();
		// }
		// });
		// add(new Metric("r(smax)") {
		// @Override
		// public String getResult() {
		// return smax_r.multiply(BigInteger.valueOf(smax_r_sign)).toString();
		// }
		// });
		add(new Metric("diff_min(fmax)") {
			@Override
			public String getResult() {
				BigInteger k = v.divide(fmax_modulo);
				if (ConditionChecker.isOfNegativeForm(v, fmax_modulo)) {
					k = k.add(ONE);
				}
				return k.subtract(fmax_min).toString();
			}
		});
		// add(new Metric("diff_max(fi)") {
		// @Override
		// public String getResult() {
		// BigInteger max_fi =
		// bi.add(BigInteger.valueOf(9)).divide(BigInteger.valueOf(6));
		// return f.subtract(max_fi).toString();
		// }
		// });
		// add(new Metric("diff_min(fi)") {
		// @Override
		// public String getResult() {
		// BigInteger min_fi =
		// MathOperations.upper_Gauss(Math.sqrt(bi.doubleValue()));
		// return f.subtract(min_fi).toString();
		// }
		// });
		// add(new Metric("diff_min-max(fi)") {
		// @Override
		// public String getResult() {
		// BigInteger min_fi = upper_Gauss(Math.sqrt(bi.doubleValue()));
		// BigInteger max_fi =
		// bi.add(BigInteger.valueOf(9)).divide(BigInteger.valueOf(6));
		// return min_fi.subtract(max_fi).toString();
		// }
		// });
		/*
		 * add(new Metric("diff_smax") {
		 * 
		 * @Override public String getResult() { return
		 * w.divide(smax_modulo).subtract(smax_min).toString(); } });
		 */
		// add(new Metric("f mod 8") {
		// @Override
		// public String getResult() {
		// return f.mod(EIGHT).toString();
		// }
		// });
		// add(new Metric("f-s") {
		// @Override
		// public String getResult() {
		// return f.subtract(s).toString();
		// }
		// });
		// add(new Metric("Form(f)") {
		// @Override
		// public String getResult() {
		// BigInteger[] rem = f.divideAndRemainder(fmax_modulo);
		// if (isOfNegativeForm(f, fmax_modulo)) {
		// return fmax_modulo + "*" + rem[0].add(ONE) + "-" +
		// fmax_modulo.subtract(rem[1]);
		// } else {
		// return fmax_modulo + "*" + rem[0] + "+" + rem[1];
		// }
		// }
		// });
		// add(new Metric("Form(s)") {
		// @Override
		// public String getResult() {
		// BigInteger[] rem = s.divideAndRemainder(smax_modulo);
		// if (isOfNegativeForm(s, smax_modulo)) {
		// return smax_modulo + "*" + rem[0].add(ONE) + "-" +
		// smax_modulo.subtract(rem[1]);
		// } else {
		// return smax_modulo + "*" + rem[0] + "+" + rem[1];
		// }
		// }
		// });
		// add(new Metric("Form(fmax)") {
		// @Override
		// public String getResult() {
		// BigInteger[] rem = fmax.divideAndRemainder(fmax_modulo);
		// if (isOfNegativeForm(fmax, fmax_modulo)) {
		// return fmax_modulo + "*" + rem[0].add(ONE) + "-" +
		// fmax_modulo.subtract(rem[1]);
		// } else {
		// return fmax_modulo + "*" + rem[0] + "+" + rem[1];
		// }
		// }
		// });
		// add(new Metric("Form(smax)") {
		// @Override
		// public String getResult() {
		// BigInteger[] rem = smax.divideAndRemainder(smax_modulo);
		// if (isOfNegativeForm(smax, smax_modulo)) {
		// return smax_modulo + "*" + rem[0].add(ONE) + "-" +
		// smax_modulo.subtract(rem[1]);
		// } else {
		// return smax_modulo + "*" + rem[0] + "+" + rem[1];
		// }
		// }
		// });
		// add(new Metric("Form(fmax²)") {
		// @Override
		// public String getResult() {
		// BigInteger fmax_sqrd = sqr(fmax);
		// BigInteger[] rem = fmax_sqrd.divideAndRemainder(fmax_modulo);
		// if (isOfNegativeForm(fmax_sqrd, fmax_modulo)) {
		// return fmax_modulo + "*" + rem[0].add(ONE) + "-" +
		// fmax_modulo.subtract(rem[1]);
		// } else {
		// return fmax_modulo + "*" + rem[0] + "+" + rem[1];
		// }
		// }
		// });
		// add(new Metric("Form(smax²)") {
		// @Override
		// public String getResult() {
		// BigInteger smax_sqrd = sqr(smax);
		// BigInteger[] rem = smax_sqrd.divideAndRemainder(smax_modulo);
		// if (isOfNegativeForm(smax_sqrd, smax_modulo)) {
		// return smax_modulo + "*" + rem[0].add(ONE) + "-" +
		// smax_modulo.subtract(rem[1]);
		// } else {
		// return smax_modulo + "*" + rem[0] + "+" + rem[1];
		// }
		// }
		// });
		// add(new Metric("max(fi)") {
		// @Override
		// public String getResult() {
		// BigInteger max_fi =
		// bi.add(BigInteger.valueOf(9)).divide(BigInteger.valueOf(6));
		// return getDivisorRemainderForm(max_fi, EIGHT);
		// }
		// });
		// add(new Metric("min(fi)") {
		// @Override
		// public String getResult() {
		// BigInteger min_fi = upper_Gauss(Math.sqrt(bi.doubleValue()));
		// return getDivisorRemainderForm(min_fi, EIGHT);
		// }
		// });
		// add(new Metric("sqrtForm(x)") {
		// @Override
		// public String getResult() {
		// double sqrt = Math.sqrt(bi.doubleValue());
		// int sqrtInt = (int) Math.floor(sqrt);
		// BigInteger diff = bi.subtract(BigInteger.valueOf(sqrtInt * sqrtInt));
		// return sqrtInt + "²" + (diff.signum() == -1 ? "-" : "+") +
		// diff.abs();
		// }
		// });
	}
}
