package math.groups;

import java.util.ArrayList;
import java.util.List;

public class HigherOrderSubset {

	public <T> List<T> getElementsUntil(int m, TestFunction<T> func) {
		List<T> elements = new ArrayList<T>();
		for (int i = 1; i < m; i++) {
			if (func.check(i)) {
				elements.add(func.getRecord(i));
			}
		}
		return elements;
	}

	public static void main(String[] args) {
		HigherOrderSubset hos = new HigherOrderSubset();

		for (int n = 2; n < 100; n++) {
			for (int c = 1; c < 100; c++) {
				TestGgT func = new TestGgT(n, c);
				List<GgtRecord> elements = hos.getElementsUntil(100, func);
				if (elements.size() > 0) {
					System.out.println(elements);
				}
			}
		}

		for (int a = 2; a < 100; a++) {
			for (int m = 2; m < 100; m++) {
				for (int c = 0; c < 100; c++) {
					TestMod func = new TestMod(a, m, c);
					List<ModRecord> elements = hos.getElementsUntil(100, func);
					if (elements.size() > 0) {
						System.out.println(elements);
					}
				}
			}
		}
	}
}
