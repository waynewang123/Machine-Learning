import java.io.Serializable;
import java.util.ArrayList;

public class KernelPerceptron extends Classifier implements Serializable, OptionHandler {

	private static final long serialVersionUID = 6679341330147520354L;

	private boolean flachProbabilityCalibration = false;

	private boolean plattScaling = false;

	private boolean isotonicRegression = false;

	private Attributes attributes;

	private boolean converged = false;

	private int iterateMaxTimes = 50000;

	private double[] coefficients;

	private DataSet baised;

	public KernelPerceptron() {
		if (flachProbabilityCalibration) {

		}
		if (plattScaling) {

		}
		if (isotonicRegression) {

		}
	}

	public KernelPerceptron(String[] args) throws Exception {
		this.setOptions(args);
		if (flachProbabilityCalibration) {

		}
		if (plattScaling) {

		}
		if (isotonicRegression) {

		}
	}

	@Override
	public DataSet toHomogeneous(DataSet ds) throws Exception {
		DataSet after = this.toBipolar(ds);

		after.getAttributes().getAttributes().add(after.getAttributes().getClassAttribute());
		after.getAttributes().getAttributes().set(after.getAttributes().size() - 2, new NumericAttribute("bais"));

		for (int i = 0; i < after.getExamples().size(); i++) {
			after.getExamples().get(i).add(after.getExamples().get(i).get(after.getAttributes().getClassIndex()));
			after.getExamples().get(i).set(after.getAttributes().getClassIndex(), new Double(-1.0));
			if (after.getExamples().get(i).get(after.getAttributes().getClassIndex() + 1) == 0.0) {
				after.getExamples().get(i).set(after.getAttributes().getClassIndex() + 1, new Double(-1.0));
			}
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
			double result = this.classify(example);
			double type = example.get(after.getAttributes().getClassIndex());
			if (type == -1.0) {
				type = 0.0;
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
		if (this.baised.getExamples().size() < 1) {
			throw new IllegalArgumentException("Empty Baised Dataset!");
		}

		double[] result = new double[this.attributes.getClassAttribute().size()];
		double kernel = 0.0;

		for (int i = 0; i < this.baised.getExamples().size(); i++) {
			kernel += this.coefficients[i]
					* this.baised.getExamples().get(i).get(baised.getAttributes().getClassIndex())
					* kSimpleKernelPolynomial(getExampleVectorWithOutLabel(example),
							getExampleVectorWithOutLabel(this.baised.getExamples().get(i)));
		}

		switch (sign(kernel)) {
		case 1:
			result[1] = 1.0;
			break;
		case 0:
			result[0] = 1.0;
			break;
		case -1:
			result[0] = 1.0;
			break;
		default:
		}

		return result;
	}

	@Override
	public void train(DataSet dataset) throws Exception {
		int loopTimes = 0;
		DataSet baised = this.toHomogeneous(dataset);
		this.baised = baised;
		this.attributes = dataset.getAttributes();
		this.coefficients = new double[baised.getExamples().size()];
		this.converged = false;

		while (!this.converged) {
			if (loopTimes > this.iterateMaxTimes) {
				System.out.println("Kernel Perceptron Cannot converge on " + dataset.getName());
				return;
				// throw new FailedToConvergeException(
				// "Kernel Perceptron Cannot converge on " + dataset.getName() +
				// ", looptimes: " + loopTimes);
			}
			loopTimes++;
			this.converged = true;
			for (int i = 0; i < baised.getExamples().size(); i++) {
				double kerenl = 0.0;
				for (int j = 0; j < baised.getExamples().size(); j++) {
					kerenl += this.coefficients[j]
							* baised.getExamples().get(j).get(baised.getAttributes().getClassIndex())
							* kSimpleKernelPolynomial(getExampleVectorWithOutLabel(baised.getExamples().get(i)),
									getExampleVectorWithOutLabel(baised.getExamples().get(j)));

				}
				if (baised.getExamples().get(i).get(baised.getAttributes().getClassIndex()) * kerenl <= 0) {
					this.coefficients[i]++;
					converged = false;
				}
			}
		}
		System.out.println("Loop Times for " + dataset.getName() + ": " + loopTimes);
	}

	@Override
	public void setOptions(String[] options) throws Exception {
		for (int i = 0; i < options.length; i++) {
			if (options[i].equals("-pc")) {
				this.flachProbabilityCalibration = true;
			}
			if (options[i].equals("-ps")) {
				this.plattScaling = true;
			}
			if (options[i].equals("-ir")) {
				this.isotonicRegression = true;
			}
		}
	}

	public double kSimpleKernelPolynomial(double[] vector1, double[] vector2) {
		if (vector1.length != vector2.length) {
			throw new IllegalArgumentException("Two vectors have different length.");
		}

		return Math.pow(this.calculateMultiplicationVectors(vector1, vector2), 2);
	}

	public int sign(double x) {
		return x == 0.0 ? 0 : (x > 0 ? 1 : -1);
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

	public double[] getExampleVectorWithOutLabel(Example ex) {
		double[] result = new double[ex.size() - 1];
		for (int i = 0; i < result.length; i++) {
			result[i] = ex.get(i);
		}
		return result;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public double[] getCoefficients() {
		return coefficients;
	}

	public void setCoefficients(double[] coefficients) {
		this.coefficients = coefficients;
	}

	public DataSet getBaised() {
		return baised;
	}

	public void setBaised(DataSet baised) {
		this.baised = baised;
	}

	/* Main Functions */

	public static void main(String[] args) {
		try {
			Evaluator evaluator = new Evaluator(new KernelPerceptron(), args);
			Performance performance = evaluator.evaluate();
			System.out.println(performance);
		} // try
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} // catch
	} // KernelPerceptron::main

}
