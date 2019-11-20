
public class ActivationFunctionSigmoid implements ActivationFunction {

	private static final double lambda = 1.0;

	@Override
	public double logistic(double x) {
		return 1.0 / (1.0 + Math.pow(Math.E, -lambda * x));
	}

}
