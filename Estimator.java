/*
 * Estimator.java
 * Copyright (c) 2017 Georgetown University.  All Rights Reserved.
 */

/**
 * 
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 2.0, 1/30/17
 */

public abstract class Estimator extends Object {

	protected int n = 0; // number of samples

	public Estimator() {
	}

	abstract public void add(Number x) throws Exception;

	public Integer getN() {
		return this.n;
	}

	abstract public Double getProbability(Number x);
}
