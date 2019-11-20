
/*
 * NaiveBayes.java
 * Copyright (c) 2017 Georgetown University.  All Rights Reserved.
 */

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 2.0, 1/30/17
 */

public class NaiveBayes extends Classifier implements Serializable, OptionHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1613941433488079034L;

	protected Attributes attributes;

	protected CategoricalEstimator classDistribution;

	protected ArrayList<ArrayList<Estimator>> classConditionalDistributions;

	protected int numClass = 0;

	public NaiveBayes() {
		this.numClass = 0;
		this.attributes = new Attributes();
		this.classDistribution = new CategoricalEstimator();
		this.classConditionalDistributions = new ArrayList<ArrayList<Estimator>>();
	}

	public NaiveBayes(String[] options) throws Exception {
		this.setOptions(options);
	}

	public Performance classify(DataSet dataSet) throws Exception {
		Performance performance = new Performance(dataSet.getAttributes());
		performance.setName(dataSet.getName());

		for (Example example : dataSet.getExamples()) {
			double result = this.classify(example);
			double type = example.get(dataSet.getAttributes().getClassIndex());

			performance.add(type, result);
		}
		return performance;
	}

	public double classify(Example example) throws Exception {
		return 1.0 * Utils.maxIndex(this.getDistribution(example));
	}

	public double[] getDistribution(Example example) throws Exception {
		double[] dist = new double[this.attributes.getClassAttribute().size()];
		double sum = 0.0;
		for (int i = 0; i < dist.length; i++) {
			dist[i] = 0.0;
		}

		// Calculate

		for (int i = 0; i < this.attributes.getClassAttribute().size(); i++) {
			double label = 1.0;
			for (int j = 0; j < this.attributes.size() - 1; j++) {
				label = label * this.classConditionalDistributions.get(i).get(j).getProbability(example.get(j));
			}
			dist[i] = (double) this.classDistribution.dist.get(i) / this.classDistribution.n * label;
			sum += dist[i];
		}

		for (int i = 0; i < dist.length; i++) {
			dist[i] = dist[i] / sum;
		}

		return dist;
	}

	public NaiveBayes clone() {
		NaiveBayes bayes = new NaiveBayes();
		bayes.attributes = this.attributes;
		bayes.numClass = 0;
		return bayes;
	}

	public void setOptions(String[] options) {
		if (options.length % 2 != 0) {
			try {
				throw new Exception("Illegal Arguments Numbers");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void train(DataSet dataset) throws Exception {
		this.attributes = dataset.getAttributes();
		this.numClass = ((NominalAttribute) dataset.getAttributes().get(dataset.getAttributes().getClassIndex()))
				.size();
		this.classDistribution = new CategoricalEstimator(this.attributes.getClassAttribute().size());

		for (int i = 0; i < this.attributes.getClassAttribute().size(); i++) {
			ArrayList<Estimator> ests = new ArrayList<Estimator>();
			for (int j = 0; j < dataset.attributes.size() - 1; j++) {
				if (dataset.attributes.get(j) instanceof NominalAttribute) {
					NominalAttribute nominal = (NominalAttribute) dataset.attributes.get(j);
					ests.add(new CategoricalEstimator(nominal.size()));
				} else {
					ests.add(new GaussianEstimator());
				}
			}
			this.classConditionalDistributions.add(ests);
		}

		// train

		for (int i = 0; i < dataset.getExamples().size(); i++) {
			this.classDistribution.add(dataset.getExamples().get(i).get(this.attributes.getClassIndex()));
			for (int j = 0; j < this.attributes.size() - 1; j++) {
				this.classConditionalDistributions
						.get(dataset.getExamples().get(i).get(this.attributes.getClassIndex()).intValue()).get(j)
						.add((dataset.getExamples().get(i).get(j)));
			}
		}
	}

	public static void main(String[] args) {
		try {
			Evaluator evaluator = new Evaluator(new NaiveBayes(), args);
			Performance performance = evaluator.evaluate();
			System.out.println(performance);
		} // try
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} // catch
	} // IBk::main
}