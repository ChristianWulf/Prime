package math;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class PairFormFunction implements UnivariateRealFunction {

	private final int	n;

	public PairFormFunction(int n) {
		this.n = n;
	}

	@Override
	public double value(double k) throws FunctionEvaluationException {
		return (n + k * k) / (2 * k);
	}

}
