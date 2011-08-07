package prime.db;

import java.math.BigInteger;

public class Test {

	private Test() {
		// hide constructor
	}

	public static boolean isPrime(Integer n) {
		return AKSTest.isPrime(BigInteger.valueOf(n));
	}

}
