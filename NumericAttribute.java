/*
 * NumericAttribute.java
 * Copyright (c) 2011 Georgetown University.  All Rights Reserved.
 */

/**
 * Stores information for a numeric attribute. A numeric attribute has a name.
 * Its domain is the real numbers.
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 1.0, 1/22/17
 */

public class NumericAttribute extends Attribute {

	/** Default constructor. */

	public NumericAttribute() {
		super("");
	} // NumericAttribute::NumericAttribute

	/**
	 * Explicit constructor. Creates a numeric attribute set of the specified
	 * name.
	 *
	 * @param name
	 *            the name of this data set
	 */

	public NumericAttribute(String name) {
		super(name);
	} // NumericAttribute::NumericAttribute

	/**
	 * Returns a string representation of this NumericAttribute.
	 *
	 * @return a string representation of this NumericAttribute
	 */

	public String toString() {
		return "@attribute " + this.name + " numeric\n";
	} // NumericAttribute::toString

	/**
	 * Returns whether the specified value is valid for a numeric attribute.
	 *
	 * @param value
	 *            the value for testing
	 * @return true if the value is valid; false otherwise
	 */

	public boolean validValue(Double value) {
		if (value instanceof Double) {
			return true;
		} else {
			return false;
		}
	} // NumericAttribute::validValue

	/*
	 * A main method for testing.
	 *
	 * @param args command-line arguments
	 */

	public static void main(String args[]) {
	} // NumericAttribute::main

} // NumericAttribute class
