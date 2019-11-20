/*
 * Classifier.java
 * Copyright (c) 2017 Georgetown University.  All Rights Reserved.
 */

/**
 * 
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 2.0, 1/30/17
 */

public abstract class Classifier extends Object implements OptionHandler {

	public Classifier() {
	}

	public Classifier(String[] options) throws Exception {
	}

	public DataSet toHomogeneous(DataSet ds) throws Exception {
		return null;
	}

	public DataSet toBinary(DataSet ds) {
		return null;
	}

	public DataSet toBipolar(DataSet ds) {
		return null;
	}

	abstract public Performance classify(DataSet dataset) throws Exception;

	abstract public double classify(Example example) throws Exception;

	abstract public double[] getDistribution(Example example) throws Exception;

	abstract public void train(DataSet dataset) throws Exception;

	public void setOptions(String[] options) throws Exception {
	}

	public String toString() {
		return null;
	}

}
