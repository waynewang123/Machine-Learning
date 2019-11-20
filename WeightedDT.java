import java.io.Serializable;
import java.util.ArrayList;

public class WeightedDT extends Classifier implements Serializable, OptionHandler {

	private static final long serialVersionUID = 7922966402618672444L;

	protected Attributes attributes;

	protected WeightedNode root;

	public WeightedDT() {
		this.attributes = new Attributes();
		this.root = new WeightedNode();
	}

	public WeightedDT(String[] args) throws Exception {
		this.attributes = new Attributes();
		this.root = new WeightedNode();
		this.setOptions(args);
	}

	@Override
	public Performance classify(DataSet dataset) throws Exception {
		Performance performance = new Performance(dataset.getAttributes());

		for (Example example : dataset.getExamples()) {
			double[] result = this.getDistribution(example);
			int type = example.get(dataset.getAttributes().getClassIndex()).intValue();

			performance.add(type, result);
		}

		return performance;
	}

	public double classify(WeightedNode node, Example example) throws Exception {
		return 1.0 * Utils.maxIndex(this.getDistribution(node, example));
	}
	
	@Override
	public double classify(Example example) throws Exception {
		return 1.0 * Utils.maxIndex(this.getDistribution(example));
	}

	@Override
	public double[] getDistribution(Example example) throws Exception {
		return this.getDistribution(root, example);
	}

	@Override
	public void train(DataSet dataset) throws Exception {
		this.attributes = dataset.getAttributes();
		this.root = this.train_aux(dataset);
	}

	@Override
	public void setOptions(String[] options) throws Exception {

	}

	public boolean isAllLeaves(WeightedNode node) {
		if (node.getChildCount() == 0) {
			return false;
		} else {
			for (WeightedNode n : node.getChildren()) {
				if (n.getChildCount() > 0) {
					return false;
				}
			}
		}

		return true;
	}

	public double[] getDistribution(WeightedNode node, Example example) throws Exception {
		if (node.isLeaf()) {
			return getDistribution(node.getWeightedClassCounts());
		} else {
			WeightedNode child = node.getChild(example.get(node.getAttribute()).intValue());
			if (child.isEmpty()) {
				return this.getDistribution(node.getWeightedClassCounts());
			} else {
				return this.getDistribution(child, example);
			}
		}
	}

	public double[] getDistribution(double[] classCounts) {
		double dist[] = new double[this.attributes.getClassAttribute().size()];
		double sum = 0;

		for (int i = 0; i < classCounts.length; i++) {
			sum += classCounts[i];
		}
		for (int i = 0; i < dist.length; i++) {
			dist[i] = (1.0 * classCounts[i]) / (1.0 * sum);
		}
		return dist;
	}

	private WeightedNode train_aux(DataSet ds) throws Exception {
		WeightedNode node = new WeightedNode();

		if (ds.isEmpty()) {
			node.setEmpty(true);
			return node;
		}
		node.setEmpty(false);
		node.setWeightedClassCounts(ds.getWeightedClassCounts());

		if (ds.homogeneous() || ds.getExamples().size() < 3) {
			return node;
		}

		ArrayList<DataSet> subsets = ds.splitOnAttribute(ds.getBestSplittingAttribute());
		node.setAttribute(ds.getBestSplittingAttribute());
		
		for (int i = 0; i < subsets.size(); i++) {
			if (subsets.get(i).getExamples().size() != ds.getExamples().size()) {
				WeightedNode child = this.train_aux(subsets.get(i));
				node.addChild(child);
			}
		}

		return node;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public WeightedNode getRoot() {
		return root;
	}

	public void setRoot(WeightedNode root) {
		this.root = root;
	}

	/* Main Functions */

	public static void main(String[] args) {
		try {
			Evaluator evaluator = new Evaluator(new WeightedDT(), args);
			Performance performance = evaluator.evaluate();
			System.out.println(performance);
		} // try
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} // catch
	}

}
