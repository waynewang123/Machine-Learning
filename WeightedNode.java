import java.util.ArrayList;

public class WeightedNode {

	private int attribute;

	private double[] weightedClassCounts;

	private ArrayList<WeightedNode> children;

	private boolean isEmpty;

	public WeightedNode() {
		this.attribute = 0;
		this.children = new ArrayList<WeightedNode>();
		this.weightedClassCounts = new double[0];
	}

	public WeightedNode(int attribute) {
		this.attribute = 0;
		this.children = new ArrayList<WeightedNode>();
		this.weightedClassCounts = new double[0];
	}

	public void addChild(WeightedNode child) {
		this.children.add(child);
	}

	public WeightedNode getChild(int index) {
		return this.children.get(index);
	}

	public void deleteChild(int index) {
		this.children.remove(index);
	}

	public void clearChildren() {
		this.children.clear();
	}

	public int getChildCount() {
		return this.children.size();
	}

	public boolean isLeaf() {
		return this.children.size() == 0 ? true : false;
	}

	/* Getter and Setter */

	public int getAttribute() {
		return attribute;
	}

	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}

	public double[] getWeightedClassCounts() {
		return weightedClassCounts;
	}

	public void setWeightedClassCounts(double[] weightedClassCounts) {
		this.weightedClassCounts = weightedClassCounts;
	}

	public ArrayList<WeightedNode> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<WeightedNode> children) {
		this.children = children;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	@Override
	public String toString() {
		StringBuilder node = new StringBuilder();

		node.append("[" + this.attribute + "--");
		for (int i = 0; i < weightedClassCounts.length; i++) {
			node.append(weightedClassCounts[i]);
			if (i < weightedClassCounts.length - 1) {
				node.append(":");
			}
		}
		node.append("]" + "\n");

		for (int i = 0; i < this.children.size(); i++) {
			node.append("  (" + i + ")" + this.children.get(i).toString());
		}

		return node.toString();
	}
}
