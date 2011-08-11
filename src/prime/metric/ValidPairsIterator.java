package prime.metric;

import static math.MathOperations.getSqrt;
import static math.MathOperations.sqr;

import java.math.BigInteger;
import java.util.Iterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.Pair;

public class ValidPairsIterator implements Iterator<Pair<BigInteger, BigInteger>>,
Iterable<Pair<BigInteger, BigInteger>> {

	private static BigInteger ONE = BigInteger.valueOf(1);

	private final ValueFactory factory;
	private final BigInteger x;

	private BigInteger currentF;

	private int index;

	public ValidPairsIterator(ValueFactory valueFactory) {
		this.factory = valueFactory;
		x = factory.getX();
		currentF = factory.getFmin();
		index = 0;
	}

	@Override
	public boolean hasNext() {
		if (canUseCache()) {
			System.out.println(x + ": " + "canUseCache");
			return true;
		} else {
			// if the next element is not in the cache, even though the cache is
			// complete, then we have passed through
			if (factory.isCacheComplete()) {
				System.out.println(x + ": " + "Cache Complete");
				return false;
			} else {
				// check whether we have already passed through
				System.out.println(x + ": f=" + currentF + " < " + factory.getFmax());
				if (currentF.compareTo(factory.getFmax()) >= 0) {
					return false;
				} else {
					Pair<BigInteger, BigInteger> newPair = computeNext();
					if (newPair.first.equals(factory.getFmax())) {
						factory.setCacheComplete(true);
						return false;
					} else {
						factory.addToCache(newPair);
						return true;
					}
				}
			}
		}
	}

	@Override
	public Pair<BigInteger, BigInteger> next() {
		System.out.println("next");
		if (!canUseCache()) {	// access without prior invocation of hasNext
			Pair<BigInteger, BigInteger> newPair = computeNext();
			if (newPair.first.equals(factory.getFmax())) {
				factory.setCacheComplete(true);
			} else {
				factory.addToCache(newPair);
			}
			return newPair;
		} else {
			return factory.getFromCache(index++);
		}
	}

	private boolean canUseCache() {
		System.out.println(x + ": " + index + " < " + factory.getCacheSize());
		return index < factory.getCacheSize();
	}

	private Pair<BigInteger, BigInteger> computeNext() {
		System.out.println("computeNext");
		BigInteger sqrt;
		do {
			BigInteger diff = sqr(currentF).subtract(x);
			sqrt = getSqrt(diff);
			currentF = currentF.add(ONE);
		} while (sqrt.equals(BigInteger.valueOf(-1)));

		BigInteger s = sqrt;
		return Pair.of(currentF.subtract(ONE), s);
	}

	@Override
	public void remove() {
		throw new NotImplementedException();
	}

	@Override
	public Iterator<Pair<BigInteger, BigInteger>> iterator() {
		return this;
	}

}
