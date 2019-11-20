import java.util.ArrayList;

public class Node {

	private int attribute;

	private int[] classCounts;

	private ArrayList<Node> children;

	private boolean isEmpty;

	public Node() {
		this.attribute = 0;
		this.children = new ArrayList<Node>();
		this.classCounts = new int[0];
	}

	public Node(int attribute) {
		this.attribute = attribute;
		this.children = new ArrayList<Node>();
		this.classCounts = new int[0];

	}

	public void addChild(Node child) {
		this.children.add(child);
	}

	public Node getChild(int index) {
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

	public ArrayList<Node> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Node> children) {
		this.children = children;
	}

	public int getAttribute() {
		return attribute;
	}

	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}

	public int[] getClassCounts() {
		return classCounts;
	}

	public void setClassCounts(int[] classCounts) {
		this.classCounts = classCounts;
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
		for (int i = 0; i < classCounts.length; i++) {
			node.append(classCounts[i]);
			if (i < classCounts.length - 1) {
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
