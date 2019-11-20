
/*
 * Attributes.java
 * Copyright (c) 2011 Georgetown University.  All Rights Reserved.
 */

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Stores information for attributes for data sets for machine learning.
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 1.0, 1/22/17
 */

public class Attributes {

	/** a list of attributes */
	private ArrayList<Attribute> attributes;

	/**
	 * a flag indicating that the data set has one or more numeric attributes
	 */
	private boolean hasNumericAttributes = false;

	/** stores the position of the class label */
	private int classIndex;

	/** Default constructor. */
	Attributes() {
		this.attributes = new ArrayList<Attribute>();
		this.classIndex = -1;
	}

	/**
	 * Adds a new attribute to this set of attributes.
	 *
	 * @param attribute
	 *            the attribute's name
	 */
	public void add(Attribute attribute) {
		if (attribute instanceof NumericAttribute) {
			this.hasNumericAttributes = true;
		}
		this.classIndex++;
		this.attributes.add(attribute);
	} // Attributes::add

	/**
	 * Returns the index of the class label.
	 *
	 * @return the index of the class label
	 */
	public int getClassIndex() {
		return this.classIndex;
	} // Attributes::getClassIndex

	/**
	 * Returns true if this set of attributes has one or more numeric
	 * attributes; returns false otherwise.
	 *
	 * @return true if this set of attributes has numeric attributes
	 */
	public boolean getHasNumericAttributes() {
		return this.hasNumericAttributes;
	} // Attributes::getHasNumericAttributes

	/**
	 * Returns the ith attribute in this set of attributes.
	 *
	 * @param i
	 *            the index of the specified attribute
	 * @return the ith Attribute
	 */
	public Attribute get(int i) {
		return this.attributes.get(i);
	} // Attributes::get

	/**
	 * Returns the class attribute.
	 *
	 * @return the class attribute
	 */
	public Attribute getClassAttribute() {
		return this.attributes.get(classIndex);
	} // Attributes::getClassAttribute

	/**
	 * Returns the attribute's index.
	 *
	 * @param name
	 *            the attribute's name
	 * @return the attribute's position in the names array
	 * @throws Exception
	 *             if the attribute does not exist
	 */
	public int getIndex(String name) throws Exception {

		int index = -1;

		for (Attribute attribute : attributes) {
			if (attribute.getName().equals(name)) {
				index = attributes.indexOf(attribute);
			}
		}

		if (index == -1) {
			throw new Exception();
		}

		return index;

	} // Attributes::getIndex

	/**
	 * Returns the number of attributes.
	 *
	 * @return the number of attributes
	 */
	public int size() {
		return this.attributes.size();
	} // Attributes::size

	/**
	 * Parses the attribute declarations in the specified scanner. By
	 * convention, the last attribute is the class label after parsing.
	 *
	 * @param scanner
	 *            a Scanner containing the data set's tokens
	 * @throws Exception
	 *             if a parse exception occurs
	 */
	public void parse(Scanner scanner) throws Exception {
		Attribute attribute = AttributeFactory.make(scanner);
		this.attributes.add(attribute);
	} // Attributes::parse

	/**
	 * Sets the class index for this set of attributes.
	 *
	 * @param classIndex
	 *            the new class index
	 * @exception Exception
	 *                if the class index is out of of bounds
	 */
	public void setClassIndex(int classIndex) throws Exception {
		this.classIndex = classIndex;
	} // Attributes::setClassIndex

	/**
	 * Returns a string representation of this Attributes object.
	 *
	 * @return a string representation of this Attributes object
	 */
	public String toString() {
		String attrubutesString = "";

		for (Attribute attribute : attributes) {
			attrubutesString += attribute.toString();
		}

		return attrubutesString;
	} // Attributes::toString

	public String toScaleString() {
		return toString();
	}

	public ArrayList<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<Attribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * A main method for testing.
	 *
	 * @param args
	 *            command-line arguments
	 */

	public static void main(String args[]) {

		DataSet a = new DataSet(new String[] { "-t", "src/bikes.mff" });
		System.out.println(a.getAttributes().getClassIndex());

		System.out.println("Attributes Test");
	} // Attributes::main

} // Attributes class
