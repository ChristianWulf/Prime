package math;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.integration.SimpsonIntegrator;

public class IntegralTest {

	static double diff(UnivariateRealFunction f, double x) throws FunctionEvaluationException {
		final double dx = 0.0001; // Anything small
		return (f.value(x + dx) - f.value(x)) / dx;
	}

	static double steigung(double x1, double y1, double x2, double y2) {
		return (y2 - y1) / (x2 - x1);
	}

	/**
	 * @param args
	 * @throws IllegalArgumentException
	 * @throws FunctionEvaluationException
	 * @throws MaxIterationsExceededException
	 */
	public static void main(String[] args) throws MaxIterationsExceededException,
			FunctionEvaluationException, IllegalArgumentException {
		SimpsonIntegrator integrator = new SimpsonIntegrator();
		// 27->(3,9)=6, int=32,83
		// 33->(3,11)=7, int=49,43
		// 35->(5,7)=6, int=11,89
		// 85->(5,17)=11, int=118,01
		int n = 35;
		UnivariateRealFunction f = new PairFormFunction(n);
		double a = 5;
		double b = 7;
		double result = integrator.integrate(f, a, b);
		System.out.println("f_" + n + "(" + a + ")=" + f.value(a));
		System.out.println("f_" + n + "(" + b + ")=" + f.value(b));
		System.out.println("f_" + n + "'(" + a + ")=" + diff(f, a));
		System.out.println("f_" + n + "'(" + b + ")=" + diff(f, b));
		System.out.println("int(f," + a + "," + b + ") = " + result);
		System.out.println("steigung(" + a + "," + b + ") = "
				+ steigung(Math.sqrt(n), Math.sqrt(n), b, f.value(b)));
	}
}
