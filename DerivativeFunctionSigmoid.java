
public class DerivativeFunctionSigmoid implements DerivativeFunction {

	@Override
	public double[] errorSingalDeltaO(double[] y, double[] o) {
		if (y.length != o.length) {
			throw new IllegalArgumentException("y and o have different length.");
		}

		double[] deltaO = new double[y.length];

		for (int k = 0; k < y.length; k++) {
			deltaO[k] = (y[k] - o[k]) * (1 - o[k]) * o[k];
		}

		return deltaO;
	}

	@Override
	public double[] errorSingalDeltaH(double[] h, double[] deltaO, double[][] w) {
		if (w.length < 1 || w[0].length < 1) {
			throw new IllegalArgumentException("h and deltaO have different length or empty W[][].");
		}

		double[] deltaH = new double[h.length - 1];

		for (int j = 0; j < h.length - 1; j++) {
			double sum = 0.0;
			for (int k = 0; k < w.length; k++) {
				sum += deltaO[k] * w[k][j];
			}
			deltaH[j] = h[j] * (1 - h[j]) * sum;
		}

		return deltaH;
	}

}
