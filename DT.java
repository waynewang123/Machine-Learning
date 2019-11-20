import java.io.Serializable;
import java.util.ArrayList;

public class DT extends Classifier implements Serializable, OptionHandler {

	private static final long serialVersionUID = -3745647180377140452L;

	protected Attributes attributes;

	protected Node root;

	protected boolean isEnablePruning;

	public DT() {
		this.attributes = new Attributes();
		this.root = new Node();
		this.isEnablePruning = true;
	}

	public DT(String[] options) throws Exception {
		this.attributes = new Attributes();
		this.root = new Node();
		this.isEnablePruning = true;
		this.setOptions(options);
	}

	public Performance classify(DataSet ds) throws Exception {
		Performance performance = new Performance(ds.getAttributes());
		performance.setName(ds.getName());

		for (Example example : ds.getExamples()) {
			double[] result = this.getDistribution(example);
			int type = example.get(ds.getAttributes().getClassIndex()).intValue();

			performance.add(type, result);
		}

		return performance;
	}

	public double classify(Example example) throws Exception {
		return 1.0 * Utils.maxIndex(this.getDistribution(example));
	}

	public double[] getDistribution(Example example) throws Exception {
		return this.getDistribution(root, example);
	}

	public void prune() throws Exception {
		this.prune(this.root);
	}

	public void setOptions(String[] options) throws Exception {
		for (int i = 0; i < options.length; i++) {
			if (options[i].equals("-u")) {
				this.isEnablePruning = false;
			}
		}
	}

	public void train(DataSet ds) throws Exception {
		this.attributes = ds.attributes;
		this.root = this.train_aux(ds);
		if (this.isEnablePruning) {
			prune();
		}
	}

	public double getExpectedError(Node node) {
		int n = 0;
		int x = node.getClassCounts()[Utils.maxIndex(node.getClassCounts())];

		for (int i = 0; i < node.getClassCounts().length; i++) {
			n += node.getClassCounts()[i];
		}

		return n * this.getPValue(n, n - x);
	}

	public double u25(int n, int x) {
		return this.getPValue(n, x);
	}

	public double getPValue(int n, int x) {
		double expectedError = 0.0;

		expectedError = (1.0 * x + 0.5 + (Math.pow(Utils.Z_25_PERCENT, 2) / 2)
				+ Math.sqrt((Math.pow(Utils.Z_25_PERCENT, 2)
						* (((1.0 * x + 0.5) * (1 - ((x * 1.0 + 0.5) / n))) + (Math.pow(Utils.Z_25_PERCENT, 2) / 4)))))
				/ (1.0 * n + Math.pow(Utils.Z_25_PERCENT, 2));

		return (n == 0 && x == 0) ? 0.0 : expectedError;
	}

	public boolean isAllLeaves(Node node) {
		if (node.getChildCount() == 0) {
			return false;
		} else {
			for (Node n : node.getChildren()) {
				if (n.getChildCount() > 0) {
					return false;
				}
			}
		}

		return true;
	}

	private double[] getDistribution(Node node, Example example) throws Exception {
		if (node.isLeaf()) {
			return getDistribution(node.getClassCounts());
		} else {
			Node child = node.getChild(example.get(node.getAttribute()).intValue());
			if (child.isEmpty()) {
				return this.getDistribution(node.getClassCounts());
			} else {
				return this.getDistribution(child, example);
			}
		}
	}

	private double[] getDistribution(int[] classCounts) {
		double dist[] = new double[this.attributes.getClassAttribute().size()];
		int sum = 0;

		for (int i = 0; i < classCounts.length; i++) {
			sum += classCounts[i];
		}
		for (int i = 0; i < dist.length; i++) {
			dist[i] = (1.0 * classCounts[i]) / (1.0 * sum);
		}
		return dist;
	}

	private void prune(Node node) throws Exception {
		if (node.getChildCount() == 0) {
			return;
		}

		if (node.getChildCount() > 0) {
			for (Node n : node.getChildren()) {
				this.prune(n);
			}
		}

		if (this.isAllLeaves(node)) {
			double parent = this.getExpectedError(node);
			double childern = 0.0;

			if (node.getChildCount() > 0) {
				for (int i = 0; i < node.getChildren().size(); i++) {
					if (!node.getChild(i).isEmpty()) {
						childern += this.getExpectedError(node.getChild(i));
					}
				}
			}

			if (parent < childern) {
				node.clearChildren();
			}

		}
	}

	private Node train_aux(DataSet ds) throws Exception {
		Node node = new Node();

		if (ds.isEmpty()) {
			node.setEmpty(true);
			return node;
		}
		node.setEmpty(false);
		node.setClassCounts(ds.getClassCounts());

		if (ds.homogeneous() || ds.getExamples().size() < 3) {
			return node;
		}

		ArrayList<DataSet> subsets = ds.splitOnAttribute(ds.getBestSplittingAttribute());
		node.setAttribute(ds.getBestSplittingAttribute());

		for (int i = 0; i < subsets.size(); i++) {
			Node child = this.train_aux(subsets.get(i));
			node.addChild(child);
		}

		return node;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public boolean isEnablePruning() {
		return isEnablePruning;
	}

	public void setEnablePruning(boolean isEnablePruning) {
		this.isEnablePruning = isEnablePruning;
	}

	/* Main */

	public static void main(String[] args) {
		try {
			Evaluator evaluator = new Evaluator(new DT(), args);
			Performance performance = evaluator.evaluate();
			System.out.println(performance);
		} // try
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} // catch
	} // DT::main

}