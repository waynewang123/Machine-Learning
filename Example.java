
/*
 * Example.java
 * Copyright (c) 2011 Georgetown University.  All Rights Reserved.
 */

import java.util.ArrayList;

/**
 * Stores the attribute values of an example. Numeric values are stored as is.
 * Nominal values are stored as Doubles and are indices of the value in the
 * attributes structure.
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 1.0, 1/22/17
 */

public class Example extends ArrayList<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7972758112641853761L;

	protected double weight = 1.0;
	
	/**
	 * Explicit constructor. Constructs an Example with n values.
	 *
	 * @param n
	 *            the number of values of this example
	 */

	public Example(int n) {
		this.ensureCapacity(n);
	} // Example::Example

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

} // Example class
