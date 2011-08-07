package prime.db;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import prime.db.entity.X_UP_Y_MOD_M;
import util.Pair;

public class DB {

	public static void main(String[] args) {
		String val = "200"; // default
		if (args.length > 0) {
			val = args[0];
		}
		int MAX = new Integer(val);

		Set<Integer>[] primeRests = new Set[MAX];
		for (int i = 0; i < primeRests.length; i++) {
			primeRests[i] = new HashSet<Integer>();
		}
		Set<Integer>[] compositeRests = new Set[MAX];
		for (int i = 0; i < compositeRests.length; i++) {
			compositeRests[i] = new HashSet<Integer>();
		}

		System.out.println("x\ty\tr\tm");

		for (int n = 3; n < MAX; n += 2) {
			if (Test.isPrime(n)) {
				X_UP_Y_MOD_M metric = new X_UP_Y_MOD_M();
				metric.setX(2);
				metric.setY(n - 1);
				List<Pair<Integer, Integer>> rests = metric.computeALL(n);
				for (Pair<Integer, Integer> p : rests) {
					Integer m = p.first;
					Integer rest = p.second;
					// System.out.println(String.format("%d\t%d\t%d\t%d", metric.getX(),
					// metric.getY(), rest, m));
				}
			}
			// System.out.println("--------------------------------");
		}

		for (int n = 3; n < MAX; n += 2) {
			X_UP_Y_MOD_M metric = new X_UP_Y_MOD_M();
			metric.setX(2);
			metric.setY(n - 1);
			List<Pair<Integer, Integer>> rests = metric.computeALL(n);
			for (Pair<Integer, Integer> p : rests) {
				Integer m = p.first;
				Integer rest = p.second;
				if (!Test.isPrime(n)) {
					compositeRests[m].add(rest);
				} else {
					primeRests[m].add(rest);
				}
			}
		}

		for (int i = 0; i < primeRests.length; i++) {
			//			System.out.println(String.format("mod %d\t%s", i, primeRests[i].toString()));
			//			System.out.println(String.format("mod %d\t%s", i, compositeRests[i].toString()));
			compositeRests[i].removeAll(primeRests[i]);
			if (compositeRests[i].size() > 0) {
				System.out.println(String.format("diff %d\t%s", i, compositeRests[i].toString()));
			}
			System.out.println("----------------");
		}
	}
}
