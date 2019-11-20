
public class ErrorFunctionSigmoid implements ErrorFunction {

	@Override
	public double computeError(double[] y, double[] o) {
		double E = 0.0;

		for (int k = 0; k < y.length; k++) {
			E += (Math.pow((y[k] - o[k]), 2) / 2);
		}

		return E;
	}

}
