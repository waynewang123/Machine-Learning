
public class ActivationFunctionPlattScaling implements ActivationFunction {

	private double A = 1.0;

	private double B = 1.0;

	@Override
	public double logistic(double x) {
		return 1.0 / (1.0 + Math.exp(A * x + B));
	}

	public double logistic(double a, double b, double f) {
		this.A = a;
		this.B = b;
		return logistic(f);
	}

	public double[] logisticAB(double[] out, boolean[] target, int prior1, int prior0) {
		double A = 0;
		double B = Math.log((prior0 + 1) / (prior1 + 1));
		double hiTarget = (prior1 + 1) / (prior1 + 2);
		double loTarget = 1 / (prior0 + 2);
		double lambda = 1E-3;
		double olderr = 1E300;
		double[] pp = new double[prior0 + prior1];
		for (int i = 0; i < pp.length; i++) {
			pp[i] = (prior1 + 1) / (prior0 + prior1 + 2);
		}
		int count = 0;
		for (int it = 0; it < 100; it++) {
			double a = 0.0;
			double b = 0.0;
			double c = 0.0;
			double d = 0.0;
			double e = 0.0;
			double t = 0.0;

			for (int i = 0; i < pp.length; i++) {
				if (target[i]) {
					t = hiTarget;
				} else {
					t = loTarget;
				}

				double d1 = pp[i] - t;
				double d2 = pp[i] * (1 - pp[i]);
				a += out[i] * out[i] * d2;
				b += d2;
				c += out[i] * d2;
				d += out[i] * d1;
				e += d1;
			}

			if (Math.abs(b) < 1e-9 && Math.abs(e) < 1e-9) {
				break;
			}

			double oldA = A;
			double oldB = B;
			double err = 0;

			while (true) {
				double det = (a + lambda) * (b + lambda) - c * c;
				if (det == 0) {
					lambda *= 10;
					continue;
				}
				A = oldA + ((b + lambda) * d - c * e) / det;
				B = oldB + ((a + lambda) * e - c * d) / det;

				err = 0;
				for (int i = 0; i < pp.length; i++) {
					double p = 1.0 / (1 + Math.exp(out[i] * A + B));
					pp[i] = p;
					err -= t * Math.log(p) + (1 - t) * Math.log(1 - p);
				}
				if (err < olderr * (1 + 1e-7)) {
					lambda *= 0.1;
					break;
				}

				lambda *= 10;
				if (lambda >= 1e6) {
					break;
				}
			}

			double diff = err - olderr;
			double scale = 0.5 * (err + olderr + 1);
			if (diff > -1e-3 * scale && diff < 1e-7 * scale) {
				count++;
			} else {
				count = 0;
			}
			olderr = err;
			if (count == 3) {
				break;
			}
		}

		double[] result = new double[2];
		result[0] = A;
		result[1] = B;
		return result;
	}

}
