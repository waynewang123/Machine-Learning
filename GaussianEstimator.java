/*
 * GaussianEstimator.java
 * Copyright (c) 2017 Georgetown University.  All Rights Reserved.
 */

/**
 * 
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 2.0, 1/30/17
 */

public class GaussianEstimator extends Estimator {

	protected Double sum = 0.0;

	protected Double sumsqr = 0.0;

	protected Double oneOverSqrt2PI;

	public GaussianEstimator() {
		super();
		this.oneOverSqrt2PI = 1.0 / Math.sqrt(2.0 * Math.PI);
	}

	public void add(Number x) throws Exception {
		this.n++;
		this.sum += x.doubleValue();
		this.sumsqr += Math.pow(x.doubleValue(), 2);
	}

	public Double getMean() {
		return sum / (1.0 * n);
	}

	public Double getVariance() {
		return (sumsqr - (Math.pow(sum, 2) / (1.0 * n))) / (1.0 * (n - 1));
	}

	public Double getProbability(Number x) {
		Double result = 0.0;

		Double mean = this.getMean();
		Double variance = this.getVariance();
		// result = oneOverSqrt2PI * (1.0 / Math.sqrt(variance))
		// * Math.pow(Math.E, (-1.0 * Math.pow(x.doubleValue() - mean, 2)) /
		// (2.0 * variance));
		result = oneOverSqrt2PI * (1.0 / Math.sqrt(variance))
				* Math.exp((-1.0 * Math.pow(x.doubleValue() - mean, 2)) / (2.0 * variance));
		return result;

	}

}