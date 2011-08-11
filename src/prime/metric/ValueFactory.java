package prime.metric;

import static java.math.BigInteger.ONE;
import static math.MathOperations.sqr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import math.MathOperations;
import util.Pair;

public class ValueFactory {

	private final BigInteger x;

	private BigInteger fmax, fmin, smax;
	private BigInteger f0, s0;

	private BigInteger fmax_minus_f, smax_minus_s;
	private BigInteger fmax_min, smax_min; // TODO rename

	private Boolean isPrime;
	private Boolean isFermat;
	private Map<BigInteger, BigInteger> primFactors;

	private BigInteger start;
	private BigInteger iterations;

	private Pair<BigInteger, BigInteger> fmax_rem, smax_rem;
	private BigInteger fmax_r, fmax_modulo;
	private BigInteger smax_r, smax_modulo;

	private final List<Pair<BigInteger, BigInteger>> cache = new ArrayList<Pair<BigInteger, BigInteger>>();
	private boolean cacheComplete = false;

	private BigInteger currentF;

	public ValueFactory(BigInteger x) {
		this.x = x;
	}

	public BigInteger getX() {
		return x;
	}

	public BigInteger getFmax() {
		if (fmax == null)
			fmax = getX().add(ONE).shiftRight(1);
		return fmax;
	}

	public BigInteger getFmin() {
		if (fmin == null) {
			fmin = MathOperations.sqrtFast(x);
			if (sqr(fmin).compareTo(x) < 0) {
				fmin = fmin.add(ONE);
			}
		}
		return fmin;
	}

	public BigInteger getSmax() {
		if (smax == null)
			smax = getFmax().subtract(ONE);
		return smax;
	}

	public BigInteger getF0() {
		if (f0 == null) {
			Pair<BigInteger, BigInteger> pair = getValidPairs().iterator().next();
			f0 = pair.first;
		}
		return f0;
	}

	public BigInteger getS0() {
		if (s0 == null) {
			Pair<BigInteger, BigInteger> pair = getValidPairs().iterator().next();
			s0 = pair.second;
		}
		return s0;
	}

	public boolean getIsPrime() {
		if (isPrime == null)
			isPrime = ConditionChecker.isPolynomialPrime(x);
		return isPrime;
	}

	public boolean getIsFermat() {
		if (isFermat == null)
			isFermat = ConditionChecker.isFermat(x);
		return isFermat;
	}

	public Map<BigInteger, BigInteger> getPrimFactors() {
		if (primFactors == null)
			primFactors = MathOperations.getPrimFactorization(getX());
		return primFactors;
	}

	public BigInteger getStart() {
		if (start == null)
			start = getX().subtract(ONE).shiftLeft(1);
		return start;
	}

	public BigInteger getIterations() {
		if (iterations == null)
			iterations = Algorithms.getIterationCount(getX(), getFmax(), getStart());
		return iterations;
	}

	public BigInteger getFax_minus_f0() {
		if (fmax_minus_f == null)
			fmax_minus_f = getFmax().subtract(getF0());
		return fmax_minus_f;
	}

	public BigInteger getFmax_minus_f() {
		if (fmax_minus_f == null)
			fmax_minus_f = getFmax().subtract(getF0());
		return fmax_minus_f;
	}

	public BigInteger getSmax_minus_s() {
		if (smax_minus_s == null)
			smax_minus_s = getSmax().subtract(getS0());
		return smax_minus_s;
	}

	public Iterable<Pair<BigInteger, BigInteger>> getValidPairs() {
		return new ValidPairsIterator(this);
	}

	public BigInteger getFmax_min() {
		if (fmax_min == null)
			fmax_min = MathOperations.getLowerBound(x, getFmax_r(), getFmax_modulo());
		return fmax_min;
	}

	public BigInteger getSmax_min() {
		if (smax_min == null)
			smax_min = MathOperations.getLowerBound(x, getSmax_r(), getSmax_modulo());
		return smax_min;
	}

	public BigInteger getFmax_r() {
		if (fmax_r == null)
			fmax_r = getFmax_rem().first;
		return fmax_r;
	}

	public BigInteger getFmax_modulo() {
		if (fmax_modulo == null)
			fmax_modulo = getFmax_rem().second;
		return fmax_modulo;
	}

	public BigInteger getSmax_r() {
		if (smax_r == null)
			smax_r = getSmax_rem().first;
		return smax_r;
	}

	public BigInteger getSmax_modulo() {
		if (smax_modulo == null)
			smax_modulo = getSmax_rem().second;
		return smax_modulo;
	}

	public Pair<BigInteger, BigInteger> getFmax_rem() {
		if (fmax_rem == null)
			fmax_rem = MathOperations.getSquaredRemainder(getFmax());
		return fmax_rem;
	}

	public Pair<BigInteger, BigInteger> getSmax_rem() {
		if (smax_rem == null)
			smax_rem = MathOperations.getSquaredRemainder(getSmax());
		return smax_rem;
	}

	public boolean isCacheComplete() {
		return cacheComplete;
	}

	public void setCacheComplete(boolean cacheComplete) {
		this.cacheComplete = cacheComplete;
	}

	public Pair<BigInteger, BigInteger> getFromCache(int index) {
		return cache.get(index);
	}

	public int getCacheSize() {
		return cache.size();
	}

	public void addToCache(Pair<BigInteger, BigInteger> newPair) {
		cache.add(newPair);
	}

	public BigInteger getCurrentF() {
		if (currentF == null)
			currentF = getFmin();
		return currentF;
	}

	public void incCurrentF() {
		currentF = currentF.add(ONE);
	}

}
