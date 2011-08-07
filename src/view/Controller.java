package view;

import static java.math.BigInteger.ONE;
import static main.Filter.is1Mod8;
import static main.Filter.isFermat;
import static main.Filter.isLastDigit2378;
import static main.Filter.isPrime;
import static main.Filter.isSquareNum;
import static main.Filter.shouldFilterNonFermats;
import static main.Filter.shouldFilterPrimes;
import static math.MathOperations.getSqrt;
import static math.MathOperations.getSquareNumber;
import static math.MathOperations.sqr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import main.CheckResult;
import main.Metric;
import math.MathOperations;
import util.MyLogger;
import util.Pair;

public class Controller {
	private static final MyLogger	log		= new MyLogger();

	private final List<Metric>		metrics	= new ArrayList<Metric>();

	private final GUI				gui;

	public Controller(GUI gui) {
		this.gui = gui;
		log.setLogLevel(MyLogger.OFF);
		createMetrics();
	}

	public List<Metric> createMetrics() {
		metrics.add(new Metric("x") {
			@Override
			public String getResult() {
				return bi.toString();
			}
		});
		metrics.add(new Metric("prime?") {
			@Override
			public String getResult() {
				return isPrime ? "yes" : "no";
			}
		});
		metrics.add(new Metric("prim fac") {
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
		metrics.add(new Metric("#prim factors") {
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
		metrics.add(new Metric("minimum") {
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
		// metrics.add(new Metric("start") {
		// @Override
		// public String getResult() {
		// return start.toString();
		// }
		// });
		metrics.add(new Metric("iterations") {
			@Override
			public String getResult() {
				if (max.compareTo(iterations) < 0) {
					max = iterations;
				}
				return iterations.toString();
			}
		});

		metrics.add(new Metric("x/iterations") {
			@Override
			public String getResult() {
				return String.format("%.3f", Metric.bi.doubleValue() / iterations.doubleValue());
			}
		});

		// metrics.add(new Metric("x mod (f-s)") {
		// @Override
		// public String getResult() {
		// return bi.mod(f.subtract(s)).toString();
		// }
		// });
		// metrics.add(new Metric("start-8*iterations") {
		// @Override
		// public String getResult() {
		// return start.subtract(iterations.shiftLeft(3)).toString();
		// }
		// });
		// metrics.add(new Metric("fmax/f") {
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
		// metrics.add(new Metric("f") {
		// @Override
		// public String getResult() {
		// if (max.compareTo(f) < 0) {
		// max = f;
		// }
		// return f.toString();
		// }
		// });/*
		// * metrics.add(new Metric("d") {
		// *
		// * @Override public String getResult() { return
		// f.subtract(s).toString(); } });
		// */
		// metrics.add(new Metric("s") {
		// @Override
		// public String getResult() {
		// if (max.compareTo(s) < 0) {
		// max = s;
		// }
		// return s.toString();
		// }
		// });
		metrics.add(new Metric("(f,s)") {
			@Override
			public String getResult() {
				String fs = "";
				for (Pair<BigInteger, BigInteger> pair : validPairs) {
					fs += "(" + pair.first + "," + pair.second + ")";
				}
				return fs;
			}
		});
		// metrics.add(new Metric("fi-si") {
		// @Override
		// public String getResult() {
		// String fs = "";
		// for (Pair<BigInteger, BigInteger> pair : validPairs) {
		// fs += "(" + pair.first.subtract(pair.second) + ")";
		// }
		// return fs;
		// }
		// });
		// metrics.add(new Metric("fi-fmin") {
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
		// metrics.add(new Metric("fi²-fmin²") {
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
		metrics.add(new Metric("2^(n-1) mod 53") {
			@Override
			public String getResult() {
				return BigInteger.valueOf(2)
				.modPow(bi.subtract(BigInteger.ONE), BigInteger.valueOf(53)).toString();
			}
		});
		metrics.add(new Metric("fmax") {
			@Override
			public String getResult() {
				return fmax.toString();
			}
		});
		metrics.add(new Metric("fmin") {
			@Override
			public String getResult() {
				return fmin.toString();
			}
		});
		// metrics.add(new Metric("q of n=2qk-k²") {
		// @Override
		// public String getResult() {
		// StringBuilder qsAsString = new StringBuilder();
		// for (double q : getAllQs(Metric.bi)) {
		// qsAsString.append(q + "|");
		// }
		// return qsAsString.toString();
		// }
		// });
		// metrics.add(new Metric("index of q") {
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
		// metrics.add(new Metric("n=2qk*k²") {
		// @Override
		// public String getResult() {
		// StringBuilder qs = new StringBuilder();
		// int k = 2;
		// double q = 0;
		// int kStart = k;
		// boolean started = false;
		// BigInteger savedQuo = BigInteger.ZERO;
		// do {
		// BigInteger z = bi.add(BigInteger.valueOf(k * k));
		// BigInteger n = BigInteger.valueOf(2 * k);
		// if (z.remainder(n).equals(BigInteger.ZERO)) {
		// BigInteger curQuo = z.divide(n);
		// if (!started) {
		// savedQuo = curQuo;
		// kStart = k;
		// started = true;
		// } else {
		// if (curQuo.equals(savedQuo)) {
		// break;
		// }
		// }
		// }
		// q = z.doubleValue() / n.doubleValue();
		// qs.append(q + "|");
		// k++;
		// } while (q < bi.doubleValue());
		// double integral = 0.5 * Math.log(k) * bi.doubleValue() + 0.25
		// * (k * k - kStart * kStart);
		// double diffIntegral = (k - kStart) * savedQuo.doubleValue() -
		// integral;
		// return "(" + kStart + "," + k + ")=" + diffIntegral;
		// }
		// });
		// metrics.add(new Metric("(a,b)") {
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
		metrics.add(new Metric("f/s") {
			@Override
			public String getResult() {
				if (validPairs.size() > 0) {
					Pair<BigInteger, BigInteger> p = validPairs.get(validPairs.size()-1);
					BigInteger fl = p.first;
					BigInteger sl = p.second;
					if (sl.equals(BigInteger.ZERO)) {
						return "";
					}
					return String.format("%.2f", fl.doubleValue() / sl.doubleValue());
				} else return "";
			}
		});
		metrics.add(new Metric("min(fmax)") {
			@Override
			public String getResult() {
				return fmax_min.toString();
			}
		});
		metrics.add(new Metric("min(smax)") {
			@Override
			public String getResult() {
				return smax_min.toString();
			}
		});
		metrics.add(new Metric("min(smax)+/-1/min(fmax)") {
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
				return "" + smax_min_temp.doubleValue() / fmax_min.doubleValue();
			}
		});
		metrics.add(new Metric("fmin² mod x") {
			@Override
			public String getResult() {
				BigInteger fmin = MathOperations.sqrtFast(bi).add(BigInteger.ONE);
				return sqr(fmin).mod(bi).toString();
			}
		});
		metrics.add(new Metric("fmax² mod x") {
			@Override
			public String getResult() {
				BigInteger fmax2_x = sqr(fmax).mod(bi);
				return ""+fmax2_x.doubleValue();
			}
		});
		metrics.add(new Metric("fmin² mod x") {
			@Override
			public String getResult() {
				BigInteger fmin2_x = sqr(fmin).mod(bi);
				return ""+Math.sqrt(fmin2_x.doubleValue());
			}
		});
		metrics.add(new Metric("fi² mod x") {
			@Override
			public String getResult() {
				String fmodn = "";
				for (Pair<BigInteger, BigInteger> pair : validPairs) {
					fmodn += sqr(pair.first).mod(bi) + ",";
				}
				return fmodn;
			}
		});
		metrics.add(new Metric("fi² mod fmax") {
			@Override
			public String getResult() {
				String fmodn = "";
				for (Pair<BigInteger, BigInteger> pair : validPairs) {
					fmodn += sqr(pair.first).mod(fmax) + ",";
				}
				return fmodn;
			}
		});
		metrics.add(new Metric("sqrt(fmax² mod x) / sqrt(fi² mod x)") {
			@Override
			public String getResult() {
				String fmodn = "";
				for (Pair<BigInteger, BigInteger> pair : validPairs) {
					fmodn += Math.sqrt(sqr(fmax).mod(bi).doubleValue())
					/ Math.sqrt(sqr(pair.second).mod(bi).doubleValue()) + ",";
				}
				return fmodn;
			}
		});
		// metrics.add(new Metric("smax/s") {
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

		// metrics.add(new Metric("(smax²-s²):16") {
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
		// metrics.add(new Metric("iter:[(smax²-s²):16]") {
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

		// metrics.add(new Metric("i/s") {
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

		// metrics.add(new Metric("s/i") {
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
		metrics.add(new Metric("fmax² mod x-fi² mod x") {
			@Override
			public String getResult() {
				if (validPairs.size() > 0) {
					Pair<BigInteger, BigInteger> f0 = validPairs.get(0);
					return sqr(fmax).mod(bi).subtract(sqr(f0.first).mod(bi)).toString();
				} else {
					return "";
				}
			}
		});
		metrics.add(new Metric("fmax² mod x/fi² mod x") {
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
		metrics.add(new Metric("fi² mod x - fi² mod fmax") {
			@Override
			public String getResult() {
				if (validPairs.size() > 0) {
					BigInteger f0 = validPairs.get(0).first;
					return sqr(f0).mod(bi).subtract(sqr(f0).mod(fmax)).toString();
				} else {
					return "";
				}
			}
		});
		metrics.add(new Metric("fmax-f") {
			@Override
			public String getResult() {
				return fmax_minus_f.toString();
			}
		});
		metrics.add(new Metric("smax-s") {
			@Override
			public String getResult() {
				return smax_minus_s.toString();
			}
		});
		// metrics.add(new Metric("(smax-s)mod(fmax-f)") {
		// @Override
		// public String getResult() {
		// return fmax_minus_f.signum() > 0 ?
		// smax_minus_s.mod(fmax_minus_f).toString() : "-";
		// }
		// });

		/*
		 * metrics.add(new Metric("f mod s") {
		 * 
		 * @Override public String getResult() { return (s.signum() > 0) ?
		 * f.mod(s).toString() : "-"; } }); metrics.add(new Metric("iter mod f")
		 * {
		 * 
		 * @Override public String getResult() { return
		 * iterations.mod(f).toString(); } }); metrics.add(new
		 * Metric("fmax mod f") {
		 * 
		 * @Override public String getResult() { return fmax.mod(f).toString();
		 * } }); metrics.add(new Metric("smax mod s") {
		 * 
		 * @Override public String getResult() { return (s.signum() > 0) ?
		 * smax.mod(s).toString() : "-"; } }); metrics.add(new
		 * Metric("fmax² - f²") { // es gilt: fmax² - f² = smax² - s²
		 * 
		 * @Override public String getResult() { return
		 * sqr(fmax).subtract(sqr(f)).toString(); } });
		 */
		/*
		 * metrics.add(new Metric("(fmax² - f²) / 16") {
		 * 
		 * @Override public String getResult() { return
		 * sqr(smax).subtract(sqr(s)).divide(BigInteger.valueOf(16)).toString();
		 * } }); metrics.add(new Metric("(fmax²-f²) mod 16") { // es gilt:
		 * (fmax²-f² == smax²-s² == 0) mod 16
		 * 
		 * @Override public String getResult() { return
		 * sqr(fmax).subtract(sqr(f)).mod(BigInteger.valueOf(16)).toString(); }
		 * });
		 */
		// metrics.add(new Metric("v") {
		// @Override
		// public String getResult() {
		// return v.toString();
		// }
		// });
		// metrics.add(new Metric("w") {
		//
		// @Override
		// public String getResult() {
		// return w.toString();
		// }
		// });
		// metrics.add(new Metric("min(fmax)") {
		// @Override
		// public String getResult() {
		// return fmax_min.toString();
		// }
		// });
		// metrics.add(new Metric("min(smax)") {
		// @Override
		// public String getResult() {
		// return smax_min.toString();
		// }
		// });
		// metrics.add(new Metric("r(fmax)") {
		// @Override
		// public String getResult() {
		// return fmax_r.multiply(BigInteger.valueOf(fmax_r_sign)).toString();
		// }
		// });
		// metrics.add(new Metric("r(smax)") {
		// @Override
		// public String getResult() {
		// return smax_r.multiply(BigInteger.valueOf(smax_r_sign)).toString();
		// }
		// });
		metrics.add(new Metric("diff_min(fmax)") {
			@Override
			public String getResult() {
				BigInteger k = v.divide(fmax_modulo);
				if (isOfNegativeForm(v, fmax_modulo)) {
					k = k.add(ONE);
				}
				return k.subtract(fmax_min).toString();
			}
		});
		metrics.add(new Metric("diff_max(fi)") {
			@Override
			public String getResult() {
				BigInteger max_fi = bi.add(BigInteger.valueOf(9)).divide(BigInteger.valueOf(6));
				return f.subtract(max_fi).toString();
			}
		});
		metrics.add(new Metric("diff_min(fi)") {
			@Override
			public String getResult() {
				BigInteger min_fi = MathOperations.upper_Gauss(Math.sqrt(bi.doubleValue()));
				return f.subtract(min_fi).toString();
			}
		});
		// metrics.add(new Metric("diff_min-max(fi)") {
		// @Override
		// public String getResult() {
		// BigInteger min_fi = upper_Gauss(Math.sqrt(bi.doubleValue()));
		// BigInteger max_fi =
		// bi.add(BigInteger.valueOf(9)).divide(BigInteger.valueOf(6));
		// return min_fi.subtract(max_fi).toString();
		// }
		// });
		/*
		 * metrics.add(new Metric("diff_smax") {
		 * 
		 * @Override public String getResult() { return
		 * w.divide(smax_modulo).subtract(smax_min).toString(); } });
		 */
		// metrics.add(new Metric("f mod 8") {
		// @Override
		// public String getResult() {
		// return f.mod(EIGHT).toString();
		// }
		// });
		// metrics.add(new Metric("f-s") {
		// @Override
		// public String getResult() {
		// return f.subtract(s).toString();
		// }
		// });
		// metrics.add(new Metric("Form(f)") {
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
		// metrics.add(new Metric("Form(s)") {
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
		// metrics.add(new Metric("Form(fmax)") {
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
		// metrics.add(new Metric("Form(smax)") {
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
		// metrics.add(new Metric("Form(fmax²)") {
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
		// metrics.add(new Metric("Form(smax²)") {
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
		// metrics.add(new Metric("max(fi)") {
		// @Override
		// public String getResult() {
		// BigInteger max_fi =
		// bi.add(BigInteger.valueOf(9)).divide(BigInteger.valueOf(6));
		// return getDivisorRemainderForm(max_fi, EIGHT);
		// }
		// });
		// metrics.add(new Metric("min(fi)") {
		// @Override
		// public String getResult() {
		// BigInteger min_fi = upper_Gauss(Math.sqrt(bi.doubleValue()));
		// return getDivisorRemainderForm(min_fi, EIGHT);
		// }
		// });
		// metrics.add(new Metric("sqrtForm(x)") {
		// @Override
		// public String getResult() {
		// double sqrt = Math.sqrt(bi.doubleValue());
		// int sqrtInt = (int) Math.floor(sqrt);
		// BigInteger diff = bi.subtract(BigInteger.valueOf(sqrtInt * sqrtInt));
		// return sqrtInt + "²" + (diff.signum() == -1 ? "-" : "+") +
		// diff.abs();
		// }
		// });

		return metrics;
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
	 * @return	true if t/o
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

	/**
	 * Returns true if <code>value</code> is of the form
	 * <code>divisor*k-r</code>; otherwise false.
	 * 
	 * @param value
	 * @param divisor
	 * @return
	 */
	private boolean isOfNegativeForm(BigInteger value, BigInteger divisor) {
		BigInteger halfModulo = divisor.shiftRight(1);
		BigInteger modulo = value.mod(divisor);
		return modulo.compareTo(halfModulo) >= 0;
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
	private BigInteger getIterationCount(BigInteger bi, BigInteger fmax, BigInteger start) {
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

	public void addResultRow(BigInteger bi, boolean isPrime, BigInteger primes_prod,
			BigInteger primes_sum, BigInteger remainder, boolean isFermat, BigInteger d,
			BigInteger s) {

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
		List<Pair<BigInteger, BigInteger>> validPairs = MathOperations.getPairsVW(bi, vIters,
				wIters);
		Metric.validPairs = validPairs;

		if (validPairs.size() > 0) {
			Metric.v = validPairs.get(0).first;
			Metric.w = validPairs.get(0).second;
		} else {
			Metric.v = BigInteger.valueOf(-1);
			Metric.w = BigInteger.valueOf(-1);
		}
		Pair<BigInteger, BigInteger> fmax_rem = MathOperations.getSquaredRemainder(Metric.fmax);
		Metric.fmax_r = fmax_rem.first;
		Metric.fmax_modulo = fmax_rem.second;
		Metric.fmax_r_sign = MathOperations.getRemainderSign(Metric.fmax, Metric.fmax_r,
				Metric.fmax_modulo);
		Metric.fmax_min = MathOperations.getLowerBound(bi, Metric.fmax_r, Metric.fmax_modulo);

		Pair<BigInteger, BigInteger> smax_rem = MathOperations.getSquaredRemainder(Metric.smax);
		Metric.smax_r = smax_rem.first;
		Metric.smax_modulo = smax_rem.second;
		Metric.smax_r_sign = MathOperations.getRemainderSign(Metric.smax, Metric.smax_r,
				Metric.smax_modulo);
		Metric.smax_min = MathOperations.getLowerBound(bi, Metric.smax_r, Metric.smax_modulo);

		if (Metric.fmax_r.equals(BigInteger.valueOf(3)) && Metric.smax_r.equals(BigInteger.ONE)) {
			log.info("3-1 @ " + bi);
			// not existent, because rf even <=> rs odd
		}

		// --------------------------------------------------------

		if (gui.cbxFilterDiff0.isSelected()) {
			BigInteger k = Metric.f.divide(Metric.fmax_modulo);
			if (isOfNegativeForm(Metric.f, Metric.fmax_modulo)) {
				k = k.add(ONE);
			}
			BigInteger diff_fmax = k.subtract(Metric.fmax_min);
			if (diff_fmax.equals(BigInteger.ZERO)) {
				return;
			}
		}

		if (gui.cbxFilterDiffMax_fi.isSelected()) {
			BigInteger max_fi = bi.add(BigInteger.valueOf(9)).divide(BigInteger.valueOf(6));
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

	public List<Metric> getMetrics() {
		return metrics;
	}
}
