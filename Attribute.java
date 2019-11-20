/*
 * Attribute.java
 * Copyright (c) 2011 Georgetown University.  All Rights Reserved.
 */

/**
 * Stores information for an attribute. An attribute has a name.
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 1.0, 1/22/17
 */

public class Attribute extends Object {

	/** this attribute's name */
	protected String name;

	/** Default constructor. */

	public Attribute() {
		this.name = "";
	}

	/**
	 * Explicit constructor that sets the name of this attribute.
	 *
	 * @param name
	 *            the name of this attribute
	 */

	public Attribute(String name) {
		this.name = name;
	} // Attribute::Attribute

	/**
	 * Gets the name of this attribute.
	 *
	 * @return a string storing the name
	 */

	public String getName() {
		return this.name;
	} // Attribute::getName

	/**
	 * Gets the size of this attribute's domain.
	 *
	 * @return an int storing the size of the domain
	 */

	public int size() {
		return 0;
	} // Attribute::size

	/**
	 * Sets the name of this attribute to the specified name.
	 *
	 * @param name
	 *            the name of this attribute
	 */

	public void setName(String name) {
		this.name = name;
	} // Attribute::setName

	/**
	 * Returns a string representation of this attribute.
	 *
	 * @return a string representation of this attribute
	 */

	public String toString() {
		if (this instanceof NumericAttribute) {
			return ((NumericAttribute) this).toString();
		} else if (this instanceof NominalAttribute) {
			return ((NominalAttribute) this).toString();
		} else {
			return "@attribute " + this.name;
		}
	} // Attribute::toString

	/*
	 * A main method for testing.
	 *
	 * @param args command-line arguments
	 */

	public static void main(String args[]) {
		System.out.println("Attribute Test");
	} // Attribute::main

} // Attribute class
