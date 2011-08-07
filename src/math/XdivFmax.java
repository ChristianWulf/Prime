package math;

import java.util.Scanner;

public class XdivFmax {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ready for input.");
		// xDivDMalAnders();
		// while (scanner.hasNextInt()) {
		// printSolutionsAB(scanner.nextInt());
		// }
		printFmax();
	}

	private static double xDivD(int x, int d) {
		int min = 2;
		int max = d;
		float sum = 1;
		// System.out.println(sqrt(40000));
		// System.out.println(sqrtFast(BigInteger.valueOf(40000)));
		for (int i = min; i <= max; i++) {
			sum -= 1.0 / delta(i);
			// System.out.println(delta(i));
		}
		return x * sum;
	}

	private static float delta(int i) {
		int i1 = i - 1;
		if (i1 <= 0) {
			i1 = 1;
		}
		return i * i1;
	}

	private static void xDivDMalAnders() {
		int x = 87;
		int MAX_D = 100;
		for (int d = 2; d <= MAX_D; d++) {
			// double result = xDivD(x, d);
			if (x <= d) {
				break;
			} else if (x % d == 0) {
				System.out.println(delta(d));
			}
			// System.out.println(x + "/" + d + " = " + result);
			// System.out.println("-----------------------");
		}
	}

	private static void printSolutionsAB(final int n) {
		int fmax = n / 2 + 1;
		System.out.println("n = " + n + ", fmax = " + fmax);
		for (int a = 0; a < fmax / 2; a++) {
			for (int b = 0; b < fmax / 2; b++) {
				int result = f(fmax, a, b);
				if (result == 0) {
					System.out.print("(" + a + "," + b + ") ");
				}
			}
		}
		System.out.println();
	}

	public static int ggT(int a, int b) {
		while (b != 0) {
			int h = a % b;
			a = b;
			b = h;
		}
		return a;
	}

	static int f(int fmax, int a, int b) {
		// return 4 * a * a - 4 * b * b + 4 * b * fmax - 4 * a * fmax - 4 * b;
		// return 4 * a * a - 4 * b * b + 4 * b * (fmax - 1) - 4 * a * fmax;
		return a * a - b * b + b * (fmax - 1) - a * fmax;
	}

	private static void printFmax() {
		System.out.println("a\tb\tfmax\tnumber");
		final int an = 50;
		final int bn = an + 1;
		for (int a = 1; a < an; a++) {
			for (int b = a + 1; b < bn; b++) {
				if (z(a, b) % n(a, b) == 0 && number(a, b) % 3 != 0) {
					float result = d(a, b);
					System.out.println(a + "\t" + b + "\t" + String.format("%.0f", result) + "\t"
							+ number(a, b));
				}
			}
		}
	}

	private static int number(int a, int b) {
		return (int) (2 * d(a, b) - 1);
	}

	private static float d(int a, int b) {
		return (float) z(a, b) / n(a, b);
		// return a + b + 2;
	}

	private static int z(int a, int b) {
		return a * a - b * b - b;
	}

	private static int n(int a, int b) {
		return a - b;
	}
}
