import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class KMeans extends Classifier implements Serializable, OptionHandler {

	private static final long serialVersionUID = -7029659235038361920L;

	private Attributes attributes;

	private Scaler scaler;

	private int k = 2;

	private boolean converged = false;

	private int maxIterations = 50000;

	private ArrayList<ArrayList<Integer>> cluster;

	public KMeans() {
		this.attributes = new Attributes();
		this.scaler = new Scaler();
		this.cluster = new ArrayList<ArrayList<Integer>>();
	}

	public KMeans(String[] args) throws Exception {
		this.attributes = new Attributes();
		this.scaler = new Scaler();
		this.cluster = new ArrayList<ArrayList<Integer>>();
		this.setOptions(args);
	}

	@Override
	public Performance classify(DataSet dataset) throws Exception {
		Performance performance = new Performance(dataset.getAttributes());
		performance.setName(dataset.getName());

		for (Example example : dataset.getExamples()) {
			double result = this.classify(example);
			double type = example.get(dataset.getAttributes().getClassIndex());

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void train(DataSet dataset) throws Exception {
		for (int i = 0; i < this.k; i++) {
			this.cluster.add(new ArrayList<Integer>());
		}

		this.scaler.configure(dataset);
		DataSet scalered = scaler.scale(dataset);
		Random random = new Random(dataset.getSeed());
		this.attributes = dataset.getAttributes();

		double[][] oldCenterPoints = new double[this.k][this.attributes.size() - 1];
		double[][] newCenterPoints = new double[this.k][this.attributes.size() - 1];
		double[] distance = new double[this.k];

		int[] selected = new int[this.k];
		for (int i = 0; i < selected.length; i++) {
			selected[i] = random.nextInt(dataset.getExamples().size());
			for (int j = 0; j < oldCenterPoints[i].length; j++) {
				oldCenterPoints[i][j] = scalered.getExamples().get(selected[i]).get(j);
			}
		}

		this.converged = false;
		int iteration = 0;

		while (!this.converged && iteration < this.maxIterations) {
			iteration++;

			for (int i = 0; i < scalered.getExamples().size(); i++) {
				for (int j = 0; j < this.k; j++) {
					distance[j] = calculateDistance(getVector(scalered.getExamples().get(i)), oldCenterPoints[j]);
					this.cluster.get(Utils.minIndex(distance)).add(i);
				}
			}

			for (int i = 0; i < this.k; i++) {
				for (int j = 0; j < this.cluster.get(i).size(); j++) {
					for (int m = 0; m < oldCenterPoints[i].length; m++) {
						if (this.cluster.get(i).isEmpty()) {
							newCenterPoints[i][m] += 0.0;
						} else {
							newCenterPoints[i][m] += this.cluster.get(i).get(m);
						}
					}
				}
			}

			for (int i = 0; i < this.k; i++) {
				for (int j = 0; j < newCenterPoints[i].length; j++) {
					newCenterPoints[i][j] /= this.cluster.get(i).size();
				}
			}

			if (this.isSameCenterPotins(oldCenterPoints, newCenterPoints)) {
				this.converged = true;
			} else {
				for (int i = 0; i < this.k; i++) {
					this.cluster.set(i, new ArrayList<Integer>());
				}
				this.setCenterPoints(oldCenterPoints, newCenterPoints);
			}

		}
	}

	@Override
	public void setOptions(String[] args) {
		for (int i = 0; i < args.length; i = i + 2) {
			if (args[i].equals("-r")) {
				this.k = Integer.valueOf(args[i + 1]);
			}
		}
	}

	public double J() {
		return 0.0;
	}

	public void setCenterPoints(double[][] points1, double[][] points2) {
		if (points1.length != points2.length && points1[0].length != points2[0].length) {
			throw new IllegalArgumentException("Points matrix have differect dimension and length.");
		}

		for (int i = 0; i < points1.length; i++) {
			for (int j = 0; j < points2.length; j++) {
				points1[i][j] = points2[i][j];
			}
		}
	}

	public boolean isSameCenterPotins(double[][] points1, double[][] points2) {
		if (points1.length != points2.length && points1[0].length != points2[0].length) {
			throw new IllegalArgumentException("Points matrix have differect dimension and length.");
		}

		for (int i = 0; i < points1.length; i++) {
			for (int j = 0; j < points2.length; j++) {
				if (points1[i][j] != points2[i][j]) {
					return false;
				}
			}
		}

		return true;
	}

	public double[] getVector(Example example) {
		double[] vecotr = new double[example.size() - 1];

		for (int i = 0; i < vecotr.length; i++) {
			vecotr[i] = example.get(i);
		}

		return vecotr;
	}

	public double calculateDistance(double[] example1, double[] example2) {
		double distance = 0.0;

		if (example1.length != example2.length) {
			throw new IllegalArgumentException("Two example vectors have different length.");
		}

		for (int i = 0; i < example1.length; i++) {
			if (attributes.get(i) instanceof NumericAttribute) {
				distance += Math.pow(example1[i] - example2[i], 2);
			} else {
				if (example1[i] == example2[i]) {
					distance += 0;
				} else {
					distance += 1;
				}
			}
		}
		distance = Math.sqrt(distance);

		return distance;
	}

	public Scaler getScaler() {
		return scaler;
	}

	public void setScaler(Scaler scaler) {
		this.scaler = scaler;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	/* Main Functions */

	public static void main(String[] args) {
		try {
			Evaluator evaluator = new Evaluator(new KMeans(), args);
			Performance performance = evaluator.evaluate();
			System.out.println(performance);
		} // try
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} // catch
	}

}
