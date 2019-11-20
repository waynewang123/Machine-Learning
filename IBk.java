
/*
 * IBk.java
 * Copyright (c) 2017 Georgetown University.  All Rights Reserved.
 */

import java.io.Serializable;

/**
 * 
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 2.0, 1/30/17
 */

public class IBk extends Classifier implements Serializable, OptionHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 520902760725173999L;

	protected DataSet dataset;

	protected Scaler scaler;

	protected int k = 3;

	/**
	 * 
	 */
	public IBk() {
		this.dataset = new DataSet();
		this.scaler = new Scaler();
	}

	/**
	 * 
	 * @param options
	 * @throws Exception
	 */
	public IBk(String[] options) {
		this.dataset = new DataSet();
		this.scaler = new Scaler();
		this.setOptions(options);
	}

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

	public double classify(Example query) throws Exception {
		double[] dist = this.getDistribution(query);

		int index = Utils.maxIndex(dist);

		return 1.0 * index;
	}

	public double[] getDistribution(Example query) throws Exception {
		if (this.dataset.getExamples().size() == 0) {
			throw new IllegalArgumentException("No Examples in the dataSet");
		}

		NominalAttribute classAttribute = (NominalAttribute) this.dataset.attributes.getClassAttribute();
		double[] distribution = new double[classAttribute.domain.size()];
		for (int i = 0; i < classAttribute.domain.size(); i++) {
			distribution[i] = 0.0;
		}

		DataSet scalered = scaler.scale(dataset);

		double[] dists = new double[k];
		int[] index = new int[k];
		double[] knn = new double[k];

		for (int i = 0; i < k; i++) {
			dists[i] = Double.MAX_VALUE;
			index[i] = 0;
			knn[i] = 0.0;
		}

		for (int i = 0; i < scalered.getExamples().size(); i++) {
			double distance = this.calculateDistance(this.scaler.scale(query), scalered.getExamples().get(i));

			int maxIndex = Utils.maxIndex(dists);
			if (distance <= dists[maxIndex]) {
				dists[maxIndex] = distance;
				index[maxIndex] = i;
			}
		}

		for (int i = 0; i < k; i++) {
			knn[i] = (double) this.dataset.getExamples().get(index[i])
					.get(this.dataset.getAttributes().getClassIndex());
		}

		for (int i = 0; i < k; i++) {
			distribution[(int) knn[i]] += 1;
		}

		for (int i = 0; i < distribution.length; i++) {
			distribution[i] = distribution[i] / (double) k;
		}

		return distribution;
	}

	public void train(DataSet dataset) throws Exception {
		this.dataset = dataset;
		this.scaler.configure(dataset);
	}

	public double calculateDistance(Example train, Example test) {

		double distance = 0.0;

		for (int i = 0; i < this.dataset.getAttributes().size() - 1; i++) {

			if (this.dataset.getAttributes().get(i) instanceof NumericAttribute) {
				distance += Math.pow(train.get(i) - test.get(i), 2);
			} else {
				if (train.get(i).equals(test.get(i))) {
					distance += 0;
				} else {
					distance += 1;
				}
			}

			// distance += Math.pow(train.get(i) - test.get(i), 2);
		}

		distance = Math.sqrt(distance);

		return distance;
	}

	/**
	 * @return
	 */
	public Classifier clone() {
		IBk ibkClone = new IBk();
		ibkClone.setK(this.k);
		return ibkClone;
	}

	/**
	 * 
	 * @param k
	 *            the number of the neighbor
	 */
	public void setK(int k) {
		this.k = k;
	}

	/**
	 * 
	 * @return
	 */
	public int getK() {
		return k;
	}

	public DataSet getDataset() {
		return dataset;
	}

	public void setDataset(DataSet dataset) {
		this.dataset = dataset;
	}

	public Scaler getScaler() {
		return scaler;
	}

	public void setScaler(Scaler scaler) {
		this.scaler = scaler;
	}

	/**
	 * @param args
	 */
	public void setOptions(String args[]) {
		if (args.length % 2 != 0) {
			try {
				throw new Exception("Illegal Arguments Numbers");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (int i = 0; i < args.length; i = i + 2) {
			if (args[i].equals("-k")) {
				this.k = Integer.valueOf(args[i + 1]);
			}
		}
	}

	public static void main(String[] args) {
		try {
			Evaluator evaluator = new Evaluator(new IBk(), args);
			Performance performance = evaluator.evaluate();
			System.out.println(performance);
		} // try
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} // catch
	} // IBk::main

}