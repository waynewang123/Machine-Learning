
public interface DerivativeFunction {

	public double[] errorSingalDeltaO(double[] y, double[] o);

	public double[] errorSingalDeltaH(double[] h, double[] deltaO, double[][] w);
}
