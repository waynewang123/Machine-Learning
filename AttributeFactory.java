
/*
 * AttributeFactory.java
 * Copyright (c) 2017 Georgetown University.  All Rights Reserved.
 */

import java.util.Scanner;

/**
 * A factory for making NominalAttributes and NumericAttributes from a scanner.
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 1.0, 1/22/17
 */

public class AttributeFactory extends Object {

	/**
	 * Processes a single attribute declaration, consisting of the keyword
	 * <code>&amp;attribute</code>, name, and either the keyword
	 * <code>numeric</code> or a list of nominal values.
	 * 
	 * @param scanner
	 *            a scanner containing the attribute's tokens
	 * @return the constructed attribute
	 * @throws Exception
	 *             if a parse exception occurs
	 */

	public static Attribute make(Scanner scanner) throws Exception {

		String attribute = scanner.nextLine();
		String[] parts = attribute.split(" ");
		if (parts[2].equals("numeric")) {
			return new NumericAttribute(parts[1]);
		} else {
			NominalAttribute nominal = new NominalAttribute(parts[1]);

			for (int i = 2; i < parts.length; i++) {
				nominal.addValue(parts[i]);
			}

			return nominal;
		}
	} // AttributeFactory::make

} // AttributeFactory class
