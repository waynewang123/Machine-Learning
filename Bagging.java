import java.io.Serializable;

public class Bagging extends Classifier implements Serializable, OptionHandler {

	private static final long serialVersionUID = -213740522350175446L;

	private Attributes attributes;

	private int ensembleSize = 10;

	private WeightedDT learningAlgorithm;

	private WeightedNode[] models;

	public Bagging() {
		attributes = new Attributes();
		models = new WeightedNode[ensembleSize];
		learningAlgorithm = new WeightedDT();
	}

	public Bagging(String[] args) throws Exception {
		attributes = new Attributes();
		models = new WeightedNode[ensembleSize];
		learningAlgorithm = new WeightedDT(args);
		this.setOptions(args);
	}

	@Override
	public Performance classify(DataSet dataset) throws Exception {
		Performance performance = new Performance(dataset.getAttributes());
		performance.setName(dataset.getName());

		for (Example example : dataset.getExamples()) {
			double[] result = this.getDistribution(example);
			int type = example.get(dataset.getAttributes().getClassIndex()).intValue();

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
		double[] result = new double[this.attributes.getClassAttribute().size()];

		for (int i = 0; i < models.length; i++) {
			double[] dist = learningAlgorithm.getDistribution(models[i], example);
			for (int j = 0; j < dist.length; j++) {
				result[j] += dist[j];
			}
		}

		for (int i = 0; i < result.length; i++) {
			result[i] /= ensembleSize;
		}

		return result;
	}

	@Override
	public void train(DataSet dataset) throws Exception {
		attributes = dataset.getAttributes();

		for (int i = 0; i < ensembleSize; i++) {
			DataSet ds = dataset.getBootstrapSamples();
			learningAlgorithm.train(ds);
			models[i] = learningAlgorithm.getRoot();
		}

	}

	@Override
	public void setOptions(String[] options) throws Exception {

	}

	/* Main Functions */

	public static void main(String[] args) {
		try {
			Evaluator evaluator = new Evaluator(new Bagging(), args);
			Performance performance = evaluator.evaluate();
			System.out.println(performance);
		} // try
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} // catch
	}

}
