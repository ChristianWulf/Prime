package prime.metric;

import java.math.BigInteger;

public abstract class Metric {

	private static class PositiveInfinity extends BigInteger {
		public PositiveInfinity() {
			super("0");
		}

		@Override
		public int compareTo(BigInteger val) {
			// should always be greater than any other value
			return 1;
		};
	}

	private static class NegativeInfinity extends BigInteger {
		public NegativeInfinity() {
			super("0");
		}

		@Override
		public int compareTo(BigInteger val) {
			// should always be less than any other value
			return -1;
		};
	}

	public static final BigInteger						posInfinity	= new PositiveInfinity();
	public static final BigInteger						negInfinity	= new NegativeInfinity();
	//	public static BigInteger							fmax;
	//	public static BigInteger							fmin;
	//	public static BigInteger							smax;
	//	public static boolean								isPrime;
	//	public static boolean								isFermat;
	//	public static Map<BigInteger, BigInteger>			primFactors	= new HashMap<BigInteger, BigInteger>();
	//	public static BigInteger							start;
	//	public static BigInteger							iterations	= BigInteger.ZERO;
	//	public static BigInteger							f;
	//	public static BigInteger							s;
	//	public static BigInteger							fmax_minus_f;
	//	public static BigInteger							smax_minus_s;
	//	public static BigInteger							v;
	//	public static BigInteger							w;
	//	public static BigInteger							fmax_r;
	//	public static BigInteger							smax_r;
	//	public static BigInteger							fmax_min;
	//	public static BigInteger							smax_min;
	//	public static BigInteger							smax_modulo;
	//	public static BigInteger							fmax_modulo;
	//	public static int									fmax_r_sign;
	//	public static int									smax_r_sign;
	//	public static List<Pair<BigInteger, BigInteger>>	validPairs;
	public BigInteger									min			= posInfinity;
	public BigInteger									max			= negInfinity;

	private final String								columnName;

	public Metric(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnName() {
		return columnName;
	}

	public abstract String getResult();

	public String getMin() {
		return min.toString();
	}

	public String getMax() {
		return max.toString();
	}

	/**
	 * Reset the minimum and maximum to infinity.
	 */
	public void reset() {
		this.min = posInfinity;
		this.max = negInfinity;
	}
}
