package prime.db;

import java.util.ArrayList;
import java.util.List;

import prime.IsPrime;
import util.Pair;

public class Filter {

	private Filter() {
		// hide constructor
	}

	public static List<Pair<Integer, Integer>> getOnlyPrimes(List<Pair<Integer, Integer>> rests) {
		List<Pair<Integer, Integer>> filtered = new ArrayList<Pair<Integer, Integer>>();
		for (Pair<Integer, Integer> rest : rests) {
			Integer n = rest.first;
			if (!IsPrime.isPolynomialPrime(n)) {
				filtered.add(rest);
			}
		}
		return filtered;
	}

}
