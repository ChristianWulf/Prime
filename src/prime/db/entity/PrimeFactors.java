package prime.db.entity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PrimeFactors {

	private final List<BigInteger>	primeFactors;

	// private int length;

	public PrimeFactors() {
		this.primeFactors = new ArrayList<BigInteger>();
	}

	public List<BigInteger> getPrimeFactors() {
		return primeFactors;
	}

}
