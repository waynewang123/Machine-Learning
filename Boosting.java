import java.io.Serializable;

public class Boosting extends Classifier implements Serializable, OptionHandler {

	private static final long serialVersionUID = 3967222242049062575L;

	private Attributes attributes;

	private int ensembleSize = 10;

	private WeightedDT learningAlgorithm;

	private WeightedNode[] models;

	private double[] alpha;

	public Boosting() {
		models = new WeightedNode[ensembleSize];
		attributes = new Attributes();
		learningAlgorithm = new WeightedDT();
		alpha = new double[ensembleSize];
	}

	public Boosting(String[] args) throws Exception {
		models = new WeightedNode[ensembleSize];
		attributes = new Attributes();
		learningAlgorithm = new WeightedDT(args);
		alpha = new double[ensembleSize];

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
		
//		for (int i = 0; i < alpha.length; i++) {
//			System.out.println(alpha[i]);
//		}

		return performance;
	}

	@Override
	public double classify(Example example) throws Exception {
		return 1.0 * Utils.maxIndex(this.getDistribution(example));
	}

	@Override
	public double[] getDistribution(Example example) throws Exception {
		double[] dist = new double[attributes.getClassAttribute().size()];
		double sum = 0.0;
		
		for (int i = 0; i < ensembleSize; i++) {
			double[] mt = learningAlgorithm.getDistribution(models[i], example);
			dist[0] += alpha[i] * mt[0];
			dist[1] += alpha[i] * mt[1];
			sum += dist[0];
			sum += dist[1];
		}
		
		for (int i = 0; i < dist.length; i++) {
			dist[i] /= sum;
		}

		// Votes Method

		return dist;
	}

	@Override
	public void train(DataSet dataset) throws Exception {
		this.attributes = dataset.getAttributes();
		// Set Weight
		
		for (int i = 0; i < dataset.getExamples().size(); i++) {
			dataset.getExamples().get(i).setWeight(1.0 / dataset.getExamples().size());
		}

		// for t = 1 to T

		for (int i = 0; i < ensembleSize; i++) {
			learningAlgorithm.train(dataset);
			models[i] = learningAlgorithm.getRoot();

			// Calculte weighted error
			double error = 0.0;

			for (int j = 0; j < dataset.getExamples().size(); j++) {
				if (learningAlgorithm.classify(models[i], dataset.getExamples().get(j)) != dataset.getExamples().get(j)
						.get(dataset.getAttributes().getClassIndex())) {
					error += dataset.getExamples().get(j).getWeight();
				}
			}
			
			//System.out.println("Error:" + error);

			// modify weight and confidence
			if (error > 0.5) {
				//T = i - 1;
				this.ensembleSize = i;
				break;
			}

			if (error == 0.0) {
				alpha[i] = 5;
			} else {
				alpha[i] = 0.5 * Math.log((1 - error) / error);
			}
			

			for (int j = 0; j < dataset.getExamples().size(); j++) {
				if (learningAlgorithm.classify(models[i], dataset.getExamples().get(j)) != dataset.getExamples().get(j)
						.get(dataset.getAttributes().getClassIndex())) {
					dataset.getExamples().get(j).setWeight(dataset.getExamples().get(j).getWeight() / (2.0 * error));
				} else {
					dataset.getExamples().get(j)
							.setWeight(dataset.getExamples().get(j).getWeight() / 2.0 * (1.0 - error));
				}
			}
		}

	}

	@Override
	public void setOptions(String[] options) throws Exception {

	}

	public int sign(double x) {
		return x == 0.0 ? 0 : (x > 0 ? 1 : -1);
	}

	public WeightedNode[] getModels() {
		return models;
	}

	public void setModels(WeightedNode[] models) {
		this.models = models;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public Classifier getLearningAlgorithm() {
		return learningAlgorithm;
	}

	public void setLearningAlgorithm(WeightedDT learningAlgorithm) {
		this.learningAlgorithm = learningAlgorithm;
	}

	public double[] getAlpha() {
		return alpha;
	}

	public void setAlpha(double[] alpha) {
		this.alpha = alpha;
	}

	/* Main Functions */

	public static void main(String[] args) {
		try {
			Evaluator evaluator = new Evaluator(new Boosting(), args);
			Performance performance = evaluator.evaluate();
			System.out.println(performance);
		} // try
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} // catch
	}

}
