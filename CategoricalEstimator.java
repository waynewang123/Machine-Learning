
/*
 * CategoricalEstimator.java
 * Copyright (c) 2017 Georgetown University.  All Rights Reserved.
 */

import java.util.ArrayList;

/**
 * 
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 2.0, 1/30/17
 */

public class CategoricalEstimator extends Estimator {

	protected ArrayList<Integer> dist;

	public CategoricalEstimator() {
		this.dist = new ArrayList<Integer>();
	}

	public CategoricalEstimator(Integer k) {
		this.dist = new ArrayList<Integer>();
		for (int i = 0; i < k; i++) {
			this.dist.add(0);
		}
	} // number of categories

	public void add(Number x) throws Exception {
		this.n++;
		this.dist.set(x.intValue(), this.dist.get(x.intValue()) + 1);
	}

	public Double getProbability(Number x) {
		return ((1.0) * (this.dist.get(x.intValue()) + 1)) / ((this.n + this.dist.size()) * (1.0));
	}
}
