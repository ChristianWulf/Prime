package prime.db;

import java.util.ArrayList;
import java.util.List;

import util.Pair;

public class Filter {

	private Filter() {
		// hide constructor
	}

	public static List<Pair<Integer, Integer>> getOnlyPrimes(List<Pair<Integer, Integer>> rests) {
		List<Pair<Integer, Integer>> filtered = new ArrayList<Pair<Integer, Integer>>();
		for (Pair<Integer, Integer> rest : rests) {
			Integer n = rest.first;
			if (!Test.isPrime(n)) {
				filtered.add(rest);
			}
		}
		return filtered;
	}

}
