
/*
 * Scaler.java
 * Copyright (c) 2017 Georgetown University.  All Rights Reserved.
 */

import java.util.ArrayList;

/**
 * 
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 2.0, 1/30/17
 */

/**
 * <b>Examples:</b> <br>
 * <code>
 * DataSet dsUnScalered; <br>
 * DataSet dsScalered; <br>
 * Scaler s = new Scaler(); <br>
 * dsScalered = s.scale(dsUnScalered);
 * </code>
 * 
 * @author Oliver
 *
 */
public class Scaler extends Object {

	private Attributes attributes;

	private ArrayList<Double> mins;

	private ArrayList<Double> maxs;

	/**
	 * Default Constructor. Do not forget to use
	 * <code>Scaler.configure(DataSet ds)</code>
	 */
	public Scaler() {
		this.attributes = new Attributes();
	}

	/**
	 * Only use as test
	 * 
	 * @param dataSet
	 * @throws Exception
	 */
	public Scaler(DataSet dataSet) throws Exception {
		this.configure(dataSet);
	}

	public void configure(DataSet ds) throws Exception {
		this.attributes = ds.getAttributes();

		this.mins = new ArrayList<Double>(this.attributes.size());
		this.maxs = new ArrayList<Double>(this.attributes.size());
		for (int i = 0; i < this.attributes.size(); i++) {
			this.mins.add(Double.MAX_VALUE);
			this.maxs.add(Double.MIN_VALUE);
		}

		for (int i = 0; i < ds.getExamples().size(); i++) {
			for (int j = 0; j < attributes.size(); j++) {
				if (ds.getExamples().get(i).get(j) <= this.mins.get(j)) {
					this.mins.set(j, ds.getExamples().get(i).get(j));
				}
				if (ds.getExamples().get(i).get(j) >= this.maxs.get(j)) {
					this.maxs.set(j, ds.getExamples().get(i).get(j));
				}
			}
		}
	}

	public DataSet scale(DataSet ds) throws Exception {
		DataSet dataSet = new DataSet(attributes);
		dataSet.setName(ds.getName());

		for (Example example : ds.getExamples()) {
			dataSet.add(scale(example));
		}

		return dataSet;
	}

	public Example scale(Example example) throws Exception {
		Example ex = new Example(example.size());

		for (int i = 0; i < example.size(); i++) {
			ex.add(example.get(i));
			if (this.attributes.getAttributes().get(i) instanceof NumericAttribute) {
				ex.set(i, calScale(example.get(i), i));
			}
		}

		return ex;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public Double calScale(Double value, int index) {
		Double scale = 0.0;
		Double min = mins.get(index);
		Double max = maxs.get(index);

		if (value > max) {
			return new Double(1);
		}
		if (value < min) {
			return new Double(0);
		}

		scale = (value - min) / (max - min);

		return scale;
	}

	/**
	 * 
	 * @return
	 */
	public Attributes getAttributes() {
		return attributes;
	}

	/**
	 * 
	 * @param attributes
	 */
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Double> getMins() {
		return mins;
	}

	/**
	 * 
	 * @param mins
	 */
	public void setMins(ArrayList<Double> mins) {
		this.mins = mins;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Double> getMaxs() {
		return maxs;
	}

	/**
	 * 
	 * @param maxs
	 */
	public void setMaxs(ArrayList<Double> maxs) {
		this.maxs = maxs;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Scaler Test");
	} // Scaler::main
}