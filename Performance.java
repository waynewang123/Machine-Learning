import java.util.ArrayList;

/*
 * Performance.java
 * Copyright (c) 2017 Georgetown University.  All Rights Reserved.
 */

/**
 * 
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 2.0, 1/30/17
 */

public class Performance extends Object {

	private String name;
	
	private Attributes attributes;

	private int[][] confusionMatrix;

	private int corrects = 0;

	private double sum = 0.0;

	private double sumSqr = 0.0;

	private int c; // number of classes
	// c * c matrix number of the class type (class label)

	private int n = 0; // number of predictions

	private int m = 0; // number of additions

	private ArrayList<Double> N;

	private ArrayList<Double> P;

	private double sumAUC = 0.0;

	private double sumAUCSqr = 0.0;

	/**
	 * 
	 * @param attributes
	 * @throws Exception
	 */
	public Performance(Attributes attributes) throws Exception {
		this.attributes = attributes;
		if (this.attributes.get(this.attributes.getClassIndex()) instanceof NominalAttribute) {
			this.c = ((NominalAttribute) this.attributes.get(this.attributes.getClassIndex())).domain.size();
		}
		this.confusionMatrix = new int[c][c];

		for (int i = 0; i < this.c; i++) {
			for (int j = 0; j < this.c; j++) {
				this.confusionMatrix[i][j] = 0;
			}
		}

		this.N = new ArrayList<Double>();

		this.P = new ArrayList<Double>();

	}

	public void add(int actual, int prediction) {
		this.n++;
		this.confusionMatrix[actual][prediction]++;

		if (Integer.compare(actual, prediction) == 0) {
			corrects++;
		}
	}

	public void add(double actual, double prediction) {
		this.add(Double.valueOf(actual).intValue(), Double.valueOf(prediction).intValue());
	}

	public void add(Performance p) throws Exception {
		if (this.c != p.getC()) {
			throw new IllegalArgumentException("the class number different for two performance");
		}

		this.m++;
		this.n += p.getN();
		this.add(p.getConfusionMatrix());
		this.corrects += p.getCorrects();
		this.sum += p.getAccuracy();
		this.sumSqr += Math.pow(p.getAccuracy(), 2);
		this.sumAUC += p.getAUC();
		this.sumAUCSqr += Math.pow(p.getAUC(), 2);

		for (Double neg : p.getArrayN()) {
			this.N.add(neg);
		}

		for (Double pos : p.getP()) {
			this.P.add(pos);
		}
	}

	public double getAccuracy() {
		return (m < 2 && n != 0) ? corrects / (1.0 * n) : this.getMean();
	}

	public double getMean() {
		return m == 0 ? 0.0 : sum / (1.0 * m);
	}

	public double getSD() {
		return m == 0 ? 0.0 : Math.sqrt((sumSqr - (Math.pow(sum, 2) / (1.0 * m))) / (1.0 * (m - 1)));
	}

	public String toString() {
		StringBuilder str = new StringBuilder();

		if (this.name != null) {
			str.append(this.name + "\n");
		}
		
		if (this.attributes.get(this.attributes.getClassIndex()) instanceof NominalAttribute) {
			str.append(
					((NominalAttribute) this.attributes.get(this.attributes.getClassIndex())).domain.toString() + "\n");
		}

		str.append("Accuracy: " + this.getAccuracy() + "\n");
		str.append("Mean: " + this.getMean() + "\n");
		str.append("SD: " + this.getSD() + "\n");
		str.append("AUC: " + this.getAUC() + "\n");
		str.append("AUCSD:" + this.getSDAUC() + "\n");

		return str.toString();
	}

	public void add(int[][] matrix) {
		for (int i = 0; i < this.confusionMatrix.length; i++) {
			for (int j = 0; j < this.confusionMatrix[i].length; j++) {
				this.confusionMatrix[i][j] += matrix[i][j];
			}
		}
	}

	public void add(int actual, double[] pr) throws Exception {
		this.add(actual, Utils.maxIndex(pr));

		switch (actual) {
		case 0:
			this.P.add(pr[0]);
			break;
		case 1:
			this.N.add(pr[0]);
			break;
		default:
			break;
		}
	}

	public double getAUC() {
		if (m > 1) {
			return (this.sumAUC / (1.0 * this.m));
		}

		double auc = 0.0;

		for (Double n : this.N) {
			for (Double p : this.P) {
				auc += this.I(n, p);
			}
		}

		return (this.N.size() > 0 && this.P.size() > 0) ? auc / (this.N.size() * this.P.size()) : 0.0;
	}

	public double getSDAUC() {
		return m == 0 ? 0.0 : Math.sqrt((sumAUCSqr - (Math.pow(sumAUC, 2) / (1.0 * m))) / (1.0 * (m - 1)));
	}

	private double I(double n, double p) {
		if (n < p) {
			return 1.0;
		} else if (n == p) {
			return 0.5;
		} else {
			return 0.0;
		}
	}

	/* Getter and Setter */

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public int[][] getConfusionMatrix() {
		return confusionMatrix;
	}

	public void setConfusionMatrix(int[][] confusionMatrix) {
		this.confusionMatrix = confusionMatrix;
	}

	public int getCorrects() {
		return corrects;
	}

	public void setCorrects(int corrects) {
		this.corrects = corrects;
	}

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public double getSumSqr() {
		return sumSqr;
	}

	public void setSumSqr(double sumSqr) {
		this.sumSqr = sumSqr;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public ArrayList<Double> getP() {
		return P;
	}

	public void setP(ArrayList<Double> p) {
		this.P = p;
	}

	public void setArrayN(ArrayList<Double> n) {
		this.N = n;
	}

	public ArrayList<Double> getArrayN() {
		return this.N;
	}

	public double getSumAUC() {
		return sumAUC;
	}

	public void setSumAUC(double sumAUC) {
		this.sumAUC = sumAUC;
	}

	public double getSumAUCSqr() {
		return sumAUCSqr;
	}

	public void setSunAUCSqr(double sumAUCSqr) {
		this.sumAUCSqr = sumAUCSqr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSumAUCSqr(double sumAUCSqr) {
		this.sumAUCSqr = sumAUCSqr;
	}
	
	
}
