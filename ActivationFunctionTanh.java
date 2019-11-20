
public class ActivationFunctionTanh implements ActivationFunction {

	@Override
	public double logistic(double x) {
		return Math.tanh(x);
	}

}
