
/*
 * NominalAttribute.java
 * Copyright (c) 2011 Georgetown University.  All Rights Reserved.
 */

import java.util.ArrayList;

/**
 * Stores information for a nominal attribute. A nominal attribute has has a
 * name and a domain.
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 1.0, 1/22/17
 */

public class NominalAttribute extends Attribute {

	/** a list of strings for the domain of nominal attributes */
	ArrayList<String> domain;

	/* a Boolean indicating whether the nominal domain is sorted */
	// boolean sorted = false;

	/** Default constructor. */

	public NominalAttribute() {
		super("");
		this.domain = new ArrayList<String>();
	}

	/**
	 * Explicit constructor. Creates a nominal attribute with the specified
	 * name.
	 *
	 * @param name
	 *            the name of this data set
	 */

	public NominalAttribute(String name) {
		super(name);
		this.domain = new ArrayList<String>();

	} // NominalAttribute::NominalAttribute

	/**
	 * Adds a new nominal value to the domain of this nominal attribute.
	 *
	 * @param value
	 *            the attribute's new domain value
	 */

	public void addValue(String value) {
		this.domain.add(value);
	} // NominalAttribute::addValue

	/**
	 * Gets the size of this nominal attribute's domain.
	 *
	 * @return an int storing the size of the domain
	 */

	public int size() {
		return this.domain.size();
	} // NominalAttribute::size

	/**
	 * Returns the value of this nominal attribute at the specified index.
	 *
	 * @param index
	 *            the attribute value's index
	 * @return the attribute value at the specified index
	 */

	public String getValue(int index) {
		return index == -1.0 ? this.domain.get(0) : this.domain.get(index);
	} // NominalAttribute::getValue

	/**
	 * Returns the index of the specified value index for this nominal
	 * attribute.
	 *
	 * @param value
	 *            the attribute's value
	 * @return the index of the specified value
	 * @throws Exception
	 *             if the value is not in the domain
	 */

	public int getIndex(String value) throws Exception {
		int index = -1;

		for (String name : domain) {
			if (name.equals(value)) {
				index = domain.indexOf(name);
			}
		}

		if (index == -1) {
			throw new Exception(this.name);
		}

		return index;
	} // NominalAttribute::getIndex

	/**
	 * Returns a string representation of this NominalAttribute.
	 *
	 * @return a string representation of this NominalAttribute
	 */

	public String toString() {
		String nominalAttributeString = "@attribute ";
		nominalAttributeString += this.name;
		for (String attr : domain) {
			nominalAttributeString += (" " + attr);
		}
		nominalAttributeString += "\n";

		return nominalAttributeString;
	} // NominalAttribute::toString

	/**
	 * Returns whether the value is valid for a nominal attribute
	 *
	 * @param value
	 *            the value for testing
	 * @return true if the value is valid; false otherwise
	 */

	public boolean validValue(String value) {
		if (this.domain.indexOf(value) > -1) {
			return true;
		} else {
			return false;
		}
	} // NominalAttribute::validValue

	/*
	 * A main method for testing.
	 *
	 * @param args command-line arguments
	 */

	public static void main(String args[]) {
		System.out.println("NominalAttribute Test");
	} // NominalAttribute::main

} // NominalAttribute class
