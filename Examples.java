
/*
 * Examples.java
 * Copyright (c) 2011 Georgetown University.  All Rights Reserved.
 */

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Stores examples for data sets for machine learning.
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 1.0, 1/22/17
 */

public class Examples extends ArrayList<Example> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 162096146940674526L;

	/** the attributes structure for these examples */
	private Attributes attributes;

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	/**
	 * Explicit constructor.
	 *
	 * @param attributes
	 *            the attributes for this set of examples
	 */

	public Examples(Attributes attributes) {
		super();
		this.attributes = attributes;
	} // Examples::parse

	/**
	 * Given the attributes structure, parses the tokens in the scanner, makes
	 * Examples, and adds them to this Examples object.
	 *
	 * @param scanner
	 *            a Scanner containing the examples' tokens
	 * @throws Exception
	 *             if an index is out of bounds or if a parse error occurs
	 */

	public void parse(Scanner scanner) throws Exception {
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			Example example = new Example(this.attributes.size());
			String[] parts = line.split(" ");

			for (int i = 0; i < parts.length; i++) {
				if (this.attributes.get(i) instanceof NumericAttribute) {
					example.add(Double.parseDouble(parts[i]));
				} else if (this.attributes.get(i) instanceof NominalAttribute) {
					example.add(Double
							.parseDouble(String.valueOf(((NominalAttribute) attributes.get(i)).getIndex(parts[i]))));
				}
			}

			this.add(example);
		}
	} // Examples::parse

	/**
	 * Returns a string representation of this Examples object.
	 *
	 * @return a string representation of this Examples object
	 */

	public String toString() {
		String examplesString = "@examples\n\n";

		for (Example example : this) {
			for (int i = 0, j = 0; i < example.size(); i++, j++) {
				if (this.attributes.get(i) instanceof NumericAttribute) {
					examplesString += (example.get(j) + " ");
				} else if (this.attributes.get(i) instanceof NominalAttribute) {
					examplesString += ((NominalAttribute) this.attributes.get(i)).getValue(example.get(j).intValue())
							+ " ";
				} else {
					examplesString += " ";
				}
			}
			examplesString += "\n";
		}

		return examplesString;
	} // Examples::toString

	public String toScaleString() {
		String examplesString = "@examples\n\n";

		for (Example example : this) {
			for (int i = 0, j = 0; i < example.size(); i++, j++) {
				if (this.attributes.get(i) instanceof NumericAttribute) {
					examplesString += (example.get(j) + " ");
				} else if (this.attributes.get(i) instanceof NominalAttribute) {
					examplesString += (example.get(j).intValue()) + " ";
				} else {
					examplesString += " ";
				}
			}
			examplesString += "\n";
		}

		return examplesString;
	}

} // Examples class
