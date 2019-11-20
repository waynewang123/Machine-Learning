import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class BP extends Classifier implements Serializable, OptionHandler {

	private static final long serialVersionUID = -4421176421905849368L;

	private static final int maxIteritionTimes = 50000;

	private ActivationFunction activationFunction;

	private ErrorFunction errorFunction;

	private DerivativeFunction derivativeFunction;

	private boolean tanhMeanSquaredError = false;

	private boolean softmaxCrossEntropy = false;

	private Attributes attributes;

	private double learningRate = 0.9;

	private double minAcceptedError = 0.1;

	private double E = 0.0;

	private int hiddenLayers = 2;

	private double[][] V;

	private double[][] W;

	private int J;

	private int Q = 0;

	private double[] h;

	private boolean converge = false;

	public BP() {
		this.activationFunction = new ActivationFunctionSigmoid();
		this.errorFunction = new ErrorFunctionSigmoid();
		this.derivativeFunction = new DerivativeFunctionSigmoid();

		if (tanhMeanSquaredError) {
			this.activationFunction = new ActivationFunctionTanh();
			this.errorFunction = new ErrorFunctionMeanSquared();
		}
		if (softmaxCrossEntropy) {

		}
	}

	public BP(String[] args) throws Exception {
		this.activationFunction = new ActivationFunctionSigmoid();
		this.errorFunction = new ErrorFunctionSigmoid();
		this.derivativeFunction = new DerivativeFunctionSigmoid();
		this.setOptions(args);

		if (tanhMeanSquaredError) {

		}
		if (softmaxCrossEntropy) {

		}
	}

	@Override
	public DataSet toHomogeneous(DataSet ds) throws Exception {
		DataSet after = this.toBinary(ds);

		after.getAttributes().getAttributes().add(after.getAttributes().getClassAttribute());
		after.getAttributes().getAttributes().set(after.getAttributes().size() - 2, new NumericAttribute("bais"));

		for (int i = 0; i < after.getExamples().size(); i++) {
			after.getExamples().get(i).add(after.getExamples().get(i).get(after.getAttributes().getClassIndex()));
			after.getExamples().get(i).set(after.getAttributes().getClassIndex(), new Double(-1.0));
		}

		after.getAttributes().setClassIndex(after.getAttributes().getClassIndex() + 1);
		after.getExamples().setAttributes(after.getAttributes());

		return after;
	}

	@Override
	public DataSet toBinary(DataSet ds) {
		DataSet after = ds.clone();
		Attributes attrs = new Attributes();
		for (int i = 0; i < ds.getAttributes().size() - 1; i++) {
			if (ds.getAttributes().get(i) instanceof NumericAttribute) {
				attrs.add(ds.getAttributes().get(i));
			} else {
				int bits = this.getBitNumber(ds.getAttributes().get(i).size());
				ArrayList<Attribute> binary = new ArrayList<Attribute>();
				for (int j = 0; j < bits; j++) {
					binary.add(new NumericAttribute(ds.getAttributes().get(i).getName() + j));
					attrs.add(binary.get(j));
				}
			}
		}
		attrs.add(ds.getAttributes().getClassAttribute());
		after.setAttributes(attrs);

		for (int i = 0; i < ds.getExamples().size(); i++) {
			Example ex = new Example(after.getAttributes().size());
			for (int j = 0; j < ds.getAttributes().size() - 1; j++) {
				if (ds.getAttributes().get(j) instanceof NumericAttribute) {
					ex.add(ds.getExamples().get(i).get(j));
				} else {
					int bits = this.getBitNumber(ds.getAttributes().get(j).size());
					int[] result = Utils.convertBinary(ds.getExamples().get(i).get(j).intValue(), bits);
					for (int k = 0; k < result.length; k++) {
						ex.add(new Double(result[k]));
					}
				}

			}
			ex.add(ds.getExamples().get(i).get(ds.getAttributes().getClassIndex()));
			after.getExamples().setAttributes(attrs);
			after.add(ex);
		}

		return after;
	}

	@Override
	public DataSet toBipolar(DataSet ds) {
		DataSet after = ds.clone();
		Attributes attrs = new Attributes();
		for (int i = 0; i < ds.getAttributes().size() - 1; i++) {
			if (ds.getAttributes().get(i) instanceof NumericAttribute) {
				attrs.add(ds.getAttributes().get(i));
			} else {
				int bits = this.getBitNumber(ds.getAttributes().get(i).size());
				ArrayList<Attribute> binary = new ArrayList<Attribute>();
				for (int j = 0; j < bits; j++) {
					binary.add(new NumericAttribute(ds.getAttributes().get(i).getName() + j));
					attrs.add(binary.get(j));
				}
			}
		}
		attrs.add(ds.getAttributes().getClassAttribute());
		after.setAttributes(attrs);

		for (int i = 0; i < ds.getExamples().size(); i++) {
			Example ex = new Example(after.getAttributes().size());
			for (int j = 0; j < ds.getAttributes().size() - 1; j++) {
				if (ds.getAttributes().get(j) instanceof NumericAttribute) {
					ex.add(ds.getExamples().get(i).get(j));
				} else {
					int bits = this.getBitNumber(ds.getAttributes().get(j).size());
					int[] result = Utils.convertBinary(ds.getExamples().get(i).get(j).intValue(), bits);
					for (int k = 0; k < result.length; k++) {
						if (result[k] == 0) {
							ex.add(new Double(-1.0));
						} else {
							ex.add(new Double(result[k]));
						}
					}
				}
			}
			ex.add(ds.getExamples().get(i).get(ds.getAttributes().getClassIndex()));
			after.getExamples().setAttributes(attrs);
			after.add(ex);
		}

		return after;
	}

	public int getBitNumber(int size) {
		int bits = 1;
		while (size > Math.pow(2, bits)) {
			bits++;
		}
		return bits;
	}

	@Override
	public Performance classify(DataSet dataset) throws Exception {
		DataSet after = this.toHomogeneous(dataset);
		Performance performance = new Performance(after.getAttributes());
		performance.setName(dataset.getName());

		for (Example example : after.getExamples()) {
			double[] result = this.getDistribution(example);
			int type = example.get(after.getAttributes().getClassIndex()).intValue();

			if (type == -1) {
				type = 0;
			}

			performance.add(type, result);
		}
		return performance;
	}

	@Override
	public double classify(Example example) throws Exception {
		return 1.0 * Utils.maxIndex(this.getDistribution(example));
	}

	@Override
	public double[] getDistribution(Example example) throws Exception {
		int K = this.attributes.getClassAttribute().size();
		double[] x = this.getExampleVectorWithOutLabel(example);

		double[] h = new double[J];
		for (int j = 0; j < J - 1; j++) {
			h[j] = this.activationFunction
					.logistic(this.calculateMultiplicationVectors(this.getRowFromMatrix(j, V), x));
		}
		h[J - 1] = -1;

		double[] o = new double[K];
		for (int k = 0; k < K; k++) {
			o[k] = this.activationFunction
					.logistic(this.calculateMultiplicationVectors(this.getRowFromMatrix(k, W), h));
		}

		return o;

	}

	@Override
	public void train(DataSet dataset) throws Exception {
		if (this.J <= 0) {
			throw new IllegalArgumentException("Wrong or not set J value.");

		}
		int loopTimes = 0;
		DataSet baised = this.toHomogeneous(dataset);
		this.attributes = baised.getAttributes();

		int I = baised.getAttributes().size() - 1;
		int K = baised.getAttributes().getClassAttribute().size();

		this.V = this.randomInitizeSmallValueMatrix(J - 1, I);
		this.W = this.randomInitizeSmallValueMatrix(K, J);

		this.h = new double[J];
		int q = 1;
		int M = baised.getExamples().size();

		// double E = 0.0;

		while (!this.converge) {
			if (loopTimes > maxIteritionTimes) {
				throw new FailedToConvergeException(
						"BP cannot converge on dataset: " + dataset.getName() + ", looptimes: " + loopTimes);
			}

			loopTimes++;

			for (int m = 0; m < M; m++) {
				double[] x = this.getExampleVectorWithOutLabel(baised.getExamples().get(m));
				double[] y = this.convertLabelToVector(baised.getExamples().get(m).get(I), K);
				for (int j = 0; j < J - 1; j++) {
					h[j] = this.activationFunction
							.logistic(this.calculateMultiplicationVectors(this.getRowFromMatrix(j, V), x));
				}
				h[J - 1] = -1;

				double[] o = new double[K];
				for (int k = 0; k < K; k++) {
					o[k] = this.activationFunction
							.logistic(this.calculateMultiplicationVectors(this.getRowFromMatrix(k, W), h));
				}

				E += this.errorFunction.computeError(y, o);

				double[] deltaO = new double[K];
				double[] deltaH = new double[J - 1];

				deltaO = this.derivativeFunction.errorSingalDeltaO(y, o);
				deltaH = this.derivativeFunction.errorSingalDeltaH(h, deltaO, W);

				for (int k = 0; k < K; k++) {
					for (int j = 0; j < J; j++) {
						this.W[k][j] += this.learningRate * deltaO[k] * h[j];
					}
				}

				for (int j = 0; j < J - 1; j++) {
					for (int i = 0; i < I; i++) {
						this.V[j][i] += this.learningRate * deltaH[j] * x[i];
					}
				}
			}
			// System.out.println("LoopTimes: " + loopTimes + " Error: " + E);

			if (E < this.minAcceptedError) {
				this.Q = ++q;
				this.converge = true;
			} else {
				E = 0.0;
			}
		}
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		for (int i = 0; i < options.length; i++) {
			if (options[i].equals("-J") || options.equals("-j")) {
				this.J = Integer.valueOf(options[i + 1]) + 1;
			}
			if (options[i].equals("-tms")) {
				this.tanhMeanSquaredError = true;
			}
			if (options[i].equals("-sc")) {
				this.softmaxCrossEntropy = true;
			}
		}
	}

	public double calculateMultiplicationVectors(double[] vector1, double[] vector2) {
		if (vector1.length != vector2.length) {
			throw new IllegalArgumentException("Two vectors have different length.");
		}

		double result = 0.0;

		for (int i = 0; i < vector1.length; i++) {
			result += vector1[i] * vector2[i];
		}

		return result;
	}

	public double[] calculateAddVectors(double[] vector1, double[] vector2) {
		if (vector1.length != vector2.length) {
			throw new IllegalArgumentException("Two vectors have different length.");
		}

		double result[] = new double[vector1.length];

		for (int i = 0; i < vector1.length; i++) {
			result[i] = vector1[i] + vector2[i];
		}

		return result;
	}

	public double[] calculateNumberMultiplicationVectors(double number, double[] vector1) {
		double[] result = new double[vector1.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = number * vector1[i];
		}

		return result;
	}

	public double[][] calculateNumberMultiplicationMatrix(double number, double[][] matrix) {
		if (matrix.length == 0) {
			throw new IllegalArgumentException("The matrix has no data");
		}

		double[][] result = new double[matrix.length][matrix[0].length];

		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				result[i][j] = matrix[i][j] * number;
			}
		}

		return result;
	}

	public double[][] calculateMatrixMinusMatrix(double[][] matrix1, double[][] matrix2) {
		if (matrix1.length != matrix2.length || matrix1.length < 1 || matrix2.length < 1
				|| matrix1[0].length != matrix2[0].length) {
			throw new IllegalArgumentException("Illegal Matrix Dimensions");
		}

		double[][] result = new double[matrix1.length][matrix1[0].length];

		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				result[i][j] = matrix1[i][j] - matrix2[i][j];
			}
		}

		return result;
	}

	public double[][] calculateMatrixAddMatrix(double[][] matrix1, double[][] matrix2) {
		if (matrix1.length != matrix2.length || matrix1.length < 1 || matrix2.length < 1
				|| matrix1[0].length != matrix2[0].length) {
			throw new IllegalArgumentException("Illegal Matrix Dimensions");
		}

		double[][] result = new double[matrix1.length][matrix1[0].length];

		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[0].length; j++) {
				result[i][j] = matrix1[i][j] + matrix2[i][j];
			}
		}

		return result;
	}

	public double[][] calculateTranspose(double[][] matrix) {
		if (matrix.length < 1 || matrix[0].length < 1) {
			throw new IllegalArgumentException("Illegal Matrix Dimensions");
		}

		double[][] result = new double[matrix[0].length][matrix.length];

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				result[j][i] = matrix[i][j];
			}
		}

		return result;

	}

	public double[][] identity(int n) {
		double[][] I = new double[n][n];

		for (int j = 0; j < I.length; j++) {
			I[j][j] = 1;
		}

		return I;
	}

	/**
	 * Generate A matrix with all the value
	 * 
	 * @param m
	 *            row number of matrix
	 * @param n
	 *            column number of matrix
	 * @return double[][] matrix
	 */
	public double[][] randomInitizeSmallValueMatrix(int m, int n) {
		Random random = new Random();
		double[][] result = new double[m][n];

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				result[i][j] = random.nextDouble() * 2 - 1;
			}
		}

		return result;
	}

	public double[] getRowFromMatrix(int i, double[][] matrix) {
		if (matrix.length < 1 || matrix[0].length < 1 || i >= matrix.length) {
			throw new IllegalArgumentException("Illegal Matrix Dimension.");
		}

		double[] row = new double[matrix[0].length];

		for (int j = 0; j < row.length; j++) {
			row[j] = matrix[i][j];
		}

		return row;
	}

	public double[] getExampleVectorWithOutLabel(Example ex) {
		double[] result = new double[ex.size() - 1];
		for (int i = 0; i < result.length; i++) {
			result[i] = ex.get(i);
		}
		return result;
	}

	public double[] convertLabelToVector(double label, int classSize) {
		if (classSize < 1) {
			throw new IllegalArgumentException("Wrong Class Label Size.");
		}

		double l = label;

		if (label == -1) {
			l = 0.0;
		}

		double[] classlabel = new double[classSize];

		classlabel[(new Double(l)).intValue()]++;

		return classlabel;
	}

	/* Getter and Setter */

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public double getMinAcceptedError() {
		return minAcceptedError;
	}

	public void setMinAcceptedError(double minAcceptedError) {
		this.minAcceptedError = minAcceptedError;
	}

	public int getHiddenLayers() {
		return hiddenLayers;
	}

	public void setHiddenLayers(int hiddenLayers) {
		this.hiddenLayers = hiddenLayers;
	}

	public double[][] getV() {
		return V;
	}

	public void setV(double[][] v) {
		V = v;
	}

	public double[][] getW() {
		return W;
	}

	public void setW(double[][] w) {
		W = w;
	}

	public int getJ() {
		return J;
	}

	public void setJ(int j) {
		J = j;
	}

	public int getQ() {
		return Q;
	}

	public void setQ(int q) {
		Q = q;
	}

	public double[] getOutputs() {
		return h;
	}

	public void setOutputs(double[] outputs) {
		this.h = outputs;
	}

	public boolean isConverge() {
		return converge;
	}

	public void setConverge(boolean converge) {
		this.converge = converge;
	}

	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}

	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public double getE() {
		return E;
	}

	public void setE(double e) {
		E = e;
	}

	public double[] getH() {
		return h;
	}

	public void setH(double[] h) {
		this.h = h;
	}

	/* Main Functions */

	public static void main(String[] args) {
		try {
			Evaluator evaluator = new Evaluator(new BP(), args);
			Performance performance = evaluator.evaluate();
			System.out.println(performance);
		} // try
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} // catch
	} // BP::main
}
