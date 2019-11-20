/*
 * Utils.java
 * Copyright (c) 2012 Georgetown University.  All Rights Reserved.
 */

public class Utils {

	public static final double Z_25_PERCENT = 0.6925;

	public static int maxIndex(double[] p) {
		int index = 0;
		double num = p[0];

		for (int i = 0; i < p.length; i++) {
			if (p[i] > num) {
				num = p[i];
				index = i;
			}
		}
		return index;
	}

	public static int maxIndex(int[] p) {
		int index = 0;
		int num = p[0];

		for (int i = 0; i < p.length; i++) {
			if (p[i] > num) {
				num = p[i];
				index = i;
			}
		}
		return index;
	}

	public static int minIndex(double[] p) {
		int index = 0;
		double num = p[0];

		for (int i = 0; i < p.length; i++) {
			if (p[i] < num) {
				num = p[i];
				index = i;
			}
		}
		return index;
	}

	public static int convertDecimal(int decimal, int base) {
		int result = 0;
		int multiplier = 1;

		while (decimal > 0) {
			int residue = decimal % base;
			decimal = decimal / base;
			result = result + residue * multiplier;
			multiplier = multiplier * 10;
		}

		return result;
	}

	public static int[] convertBinary(int decimal, int bits) {
		int[] result = new int[bits];
		int binary = Utils.convertDecimal(decimal, 2);
		int[] digits = Utils.getDigits(binary);
		int k = result.length - digits.length;

		for (int i = 0; i < digits.length; i++, k++) {
			result[k] = digits[i];
		}

		return result;
	}

	public static int[] getDigits(int num) {
		String number = String.valueOf(num);
		int[] result = new int[number.length()];

		for (int i = 0; i < number.length(); i++) {
			result[i] = Character.digit(number.charAt(i), 10);
		}

		return result;
	}
}
