
/*
 * DataSet.java
 * Copyright (c) 2017 Georgetown University.  All Rights Reserved.
 */

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Implements a class for a data set for machine-learning methods.
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 2.0, 1/30/17
 */

public class DataSet implements OptionHandler {

	/** the name of this data set */
	protected String name;

	/** the attributes of this data set */
	protected Attributes attributes = null;

	/** the examples of this data set */
	protected Examples examples = null;

	/** default random seed */
	protected long seed = 2026875034;

	/** a random number generator */
	protected Random random;

	protected int folds = 10;

	protected int[] partitions = null;

	/**
	 * Default constructor.
	 */
	public DataSet() {
		this.name = "";
		this.attributes = new Attributes();
		this.examples = new Examples(attributes);
		this.random = new Random(seed);
	} // DataSet::DataSet

	/**
	 * Explicit constructor.
	 *
	 * @param options
	 *            the options for this data set
	 */
	public DataSet(String[] options) {
		this.name = "";
		this.attributes = new Attributes();
		this.examples = new Examples(attributes);
		this.setOptions(options);
		this.random = new Random(this.seed);
	} // DataSet::DataSet

	/**
	 * Explicit constructor.
	 *
	 * @param attributes
	 *            the attributes for this data set
	 */
	public DataSet(Attributes attributes) {
		this.name = "";
		this.attributes = attributes;
		this.examples = new Examples(attributes);
		this.random = new Random(seed);
	} // DataSet::DataSet

	/**
	 * Clone one Dataset without Attributes and Examples
	 */
	public DataSet clone() {
		DataSet ds = new DataSet();

		try {
			ds.setFolds(this.folds);
			ds.setName(ds.name);
			ds.setSeed(ds.seed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ds;
	}

	/**
	 * Adds the examples of the specified data set to this data set.
	 *
	 * @param dataset
	 *            the data set to be added
	 */
	public void add(DataSet dataset) {
		for (Example example : dataset.getExamples()) {
			this.examples.add(example);
		}
	} // DataSet::add

	/**
	 * Adds the specified example to this data set.
	 *
	 * @param example
	 *            the example to be added
	 */
	public void add(Example example) {
		this.examples.add(example);
	} // DataSet::add

	/**
	 * Gets the attributes of this DataSet object.
	 *
	 * @return the attributes of this data set
	 */
	public Attributes getAttributes() {
		return this.attributes;
	} // DataSet::getAttributes

	/**
	 * Gets the examples of this data set.
	 *
	 * @return the examples of this data set
	 */
	public Examples getExamples() {
		return this.examples;
	} // DataSet::getExamples

	/**
	 * Gets the seed for this data set.
	 *
	 * @return the seed for this data set
	 */
	public long getSeed() {
		return this.seed;
	} // DataSet::getSeed

	/**
	 * Returns true if this data set has numeric attributes; returns false
	 * otherwise.
	 *
	 * @return true if this data set has numeric attributes
	 */
	public boolean getHasNumericAttributes() {
		return this.attributes.getHasNumericAttributes();
	} // DataSet::getHasNumericAttributes

	/**
	 * Loads a data set from the specified file.
	 *
	 * @param filename
	 *            the file from which to read
	 * @throws Exception
	 *             if the file is not found or if a parsing exception occurs
	 */
	public void load(String filename) throws Exception {
		try {
			this.parse(new Scanner(new BufferedReader(new FileReader(filename))));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	} // DataSet::load

	/**
	 * Parses a data set from the specified scanner, which consists of parsing
	 * the data set's header, attributes, and examples.
	 *
	 * @param scanner
	 *            a scanner containing the data set's tokens
	 * @throws Exception
	 *             if a parsing exception occurs
	 */
	private void parse(Scanner scanner) throws Exception {
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.startsWith("@dataset")) {
				this.name = line.split(" ")[1];
			} else if (line.startsWith("@attribute")) {
				Attribute attribute = AttributeFactory
						.make(new Scanner(new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8))));
				this.attributes.add(attribute);
			} else if (line.startsWith("@examples")) {

			} else if (line.trim().equals("")) {

			} else {
				// Processing the example
				this.examples.parse(new Scanner(new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8))));
			}
		}
	} // DataSet::parse

	/**
	 * Sets the options for this data set. <code>-s</code> sets the random seed
	 * for this data set.
	 *
	 * @param options
	 *            the arguments
	 */
	public void setOptions(String[] options) {

		for (int i = 0; i < options.length; i = i + 2) {
			if (options[i].equals("-s")) {
				this.seed = Long.parseLong(options[i + 1]);
			}
			if (options[i].equals("-x")) {
				this.folds = Integer.parseInt(options[i + 1]);
			}
		}

	} // DataSet::setOptions

	/**
	 * Sets the random number seed for this data set. This also seeds the random
	 * number generator.
	 *
	 * @param seed
	 *            the seed
	 */
	public void setSeed(long seed) {
		this.seed = seed;
	} // DataSet::setSeed

	/**
	 * Returns a string representation of the data set in a format identical to
	 * that of the file format.
	 *
	 * @return a string containing data set
	 */
	public String toString() {
		String dataSetString = "";

		dataSetString += "@dataset ";
		dataSetString += this.name;
		dataSetString += "\n\n";
		dataSetString += this.attributes.toString();
		dataSetString += "\n";
		dataSetString += this.examples.toString();

		return dataSetString;
	} // DataSet::toString

	public String toScaleString() {
		String dataSetString = "";

		dataSetString += "@dataset ";
		dataSetString += this.name;
		dataSetString += "\n\n";
		dataSetString += this.attributes.toScaleString();
		dataSetString += "\n";
		dataSetString += this.examples.toScaleString();

		return dataSetString;
	}

	/**
	 * 
	 * @param p
	 *            the index of test set
	 * @return
	 * @throws Exception
	 */
	public TrainTestSets getCVSets(int p) throws Exception {
		if (this.partitions == null) {
			throw new IllegalArgumentException("Partition[] has not been initilized.");
		}
		if (p >= this.folds) {
			throw new IllegalArgumentException("p should not be bigger than folds");
		}

		TrainTestSets tts = new TrainTestSets();
		DataSet train = new DataSet(attributes);
		DataSet test = new DataSet(attributes);
		train.setFolds(folds);
		test.setFolds(folds);

		for (int i = 0; i < this.examples.size(); i++) {
			if (this.partitions[i] == p) {
				test.add(this.examples.get(i));
			} else {
				train.add(this.examples.get(i));
			}
		}
		tts.setTestingSet(test);
		tts.setTrainingSet(train);

		return tts;
	}

	public void buildCVPartition(int folds) {
		this.partitions = new int[this.examples.size()];
		// random value for partition
		for (int i = 0; i < this.examples.size(); i++) {
			partitions[i] = this.random.nextInt(folds);
		}
	}

	/**
	 * 
	 * @return the number of the folds
	 */
	public int getFolds() {
		return this.folds;
	}

	/**
	 * 
	 * @param folds
	 * @throws Exception
	 */
	public void setFolds(int folds) throws Exception {
		this.folds = folds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*-------------------------------------------------------------------*/
	// P3 Declarations and Methods
	/*-------------------------------------------------------------------*/

	/**
	 *
	 */
	public boolean isEmpty() {
		return this.getExamples().size() == 0 ? true : false;
	} // DataSet::isEmpty

	/**
	 *
	 */
	public double entropy(int attribute) throws Exception {
		if (this.isEmpty()) {
			return 0.0;
		}

		double[] num = new double[this.attributes.get(attribute).size()];

		double entropy = 0.0;

		double sumWeight = 0.0;

		for (int i = 0; i < this.getExamples().size(); i++) {
			sumWeight += this.getExamples().get(i).getWeight();
		}

		for (int i = 0; i < this.getExamples().size(); i++) {
			for (int j = 0; j < this.attributes.get(attribute).size(); j++) {
				if (this.getExamples().get(i).get(attribute) == (1.0 * j)) {
					// num[j] += 1.0;
					num[j] += this.getExamples().get(i).getWeight();
				}
			}
		}

		for (int i = 0; i < this.attributes.get(attribute).size(); i++) {
			// double pi = num[i] / (1.0 * this.getExamples().size());
			double pi = num[i] / sumWeight;
			if (pi != 0.0) {
				entropy += (-1 * pi * Math.log(pi) / Math.log(2));
			}
		}

		return entropy;
	} // DataSet::entropy

	/**
	 *
	 */
	public double gainRatio(int attribute) throws Exception {
		return this.splitInformation(attribute) == 0 ? 0.0 : this.gain(attribute) / this.splitInformation(attribute);
	} // DataSet::gainRatio

	/**
	 *
	 */
	public int getBestSplittingAttribute() throws Exception {
		double max = Double.MIN_VALUE;
		int index = 0;

		for (int i = 0; i < this.attributes.size() - 1; i++) {
			if (this.gainRatio(i) > max) {
				max = this.gainRatio(i);
				index = i;
			}
		}

		return index;
	} // DataSet::getBestSplittingAttribute

	/**
	 *
	 */
	public ArrayList<DataSet> splitOnAttribute(int attribute) throws Exception {
		ArrayList<DataSet> subsets = new ArrayList<DataSet>();

		for (int i = 0; i < this.attributes.get(attribute).size(); i++) {
			subsets.add(new DataSet(this.attributes));
		}

		for (int i = 0; i < this.getExamples().size(); i++) {
			subsets.get(this.examples.get(i).get(attribute).intValue()).add(this.getExamples().get(i));
		}

		return subsets;
	} // DataSet::splitOnAttribute

	/**
	 *
	 */
	public boolean homogeneous() throws Exception {
		if (this.isEmpty()) {
			return true;
		}

		double classLabel = this.getExamples().get(0).get(this.attributes.getClassIndex());
		for (int i = 0; i < this.getExamples().size(); i++) {
			if (this.getExamples().get(i).get(this.attributes.getClassIndex()) != classLabel) {
				return false;
			}
		}
		return true;
	} // DataSet::homogeneous

	/**
	 *
	 */
	public int[] getClassCounts() throws Exception {
		int[] classCounts = new int[this.attributes.getClassAttribute().size()];

		for (int i = 0; i < this.getExamples().size(); i++) {
			classCounts[this.getExamples().get(i).get(this.attributes.getClassIndex()).intValue()]++;
		}

		return classCounts;
	} // DataSet::getClassCounts

	/**
	 *
	 */
	public int getMajorityClassLabel() throws Exception {
		return Utils.maxIndex(this.getClassCounts());
	} // DataSet::getMajorityClassLabel

	/**
	 *
	 */
	public double entropy() throws Exception {
		return this.entropy(this.attributes.getClassIndex());
	} // DataSet::entropy

	/**
	 *
	 */
	public double gain(int attribute) throws Exception {
		double gain = 0.0;

		ArrayList<DataSet> subsets = this.splitOnAttribute(attribute);

		double sumWeights = 0.0;
		for (int i = 0; i < this.getExamples().size(); i++) {
			sumWeights += this.getExamples().get(i).getWeight();
		}
		
		for (int i = 0; i < subsets.size(); i++) {
			double subWeights = 0.0;
			for (int j = 0; j < subsets.get(i).getExamples().size(); j++) {
				subWeights += subsets.get(i).getExamples().get(j).getWeight();
			}
//			gain += (((subsets.get(i).getExamples().size() * 1.0) / (this.getExamples().size() * 1.0))
//					* subsets.get(i).entropy());
			
			gain += (((subWeights * 1.0) / (sumWeights * 1.0))
					* subsets.get(i).entropy());
		}

		return (this.entropy() - gain);
	} // DataSet::gain

	/**
	 *
	 */
	public double splitInformation(int attribute) throws Exception {
		double spliptInfo = 0.0;

		ArrayList<DataSet> subsets = this.splitOnAttribute(attribute);

		double datasetWeights = 0.0;

		for (int i = 0; i < this.getExamples().size(); i++) {
			datasetWeights += this.getExamples().get(i).getWeight();
		}

		for (int i = 0; i < subsets.size(); i++) {
			double subsetWeights = 0.0;
			for (int j = 0; j < subsets.get(i).getExamples().size(); j++) {
				subsetWeights += subsets.get(i).getExamples().get(j).getWeight();
			}
			// double pi = (1.0 * subsets.get(i).getExamples().size()) / (1.0 *
			// this.getExamples().size());
			double pi = subsetWeights / datasetWeights;
			if (pi != 0.0) {
				spliptInfo += (-1 * pi * Math.log(pi) / Math.log(2));
			}
		}

		return spliptInfo;
	}

	/*-------------------------------------------------------------------*/
	public TrainTestSets getHoldOutSets(double p) {
		if (p <= 0.0 || p >= 1) {
			throw new IllegalArgumentException("Wrong P value.");
		}

		Random random = new Random();
		DataSet train = new DataSet(attributes);
		train.setName(name);
		DataSet test = new DataSet(attributes);
		test.setName(name);

		for (int i = 0; i < examples.size(); i++) {
			if (random.nextDouble() < p) {
				train.add(examples.get(i));
			} else {
				test.add(examples.get(i));
			}
		}

		return new TrainTestSets(train, test);
	}

	/*-------------------------------------------------------------------*/
	// P5 Declarations and Methods
	/*-------------------------------------------------------------------*/

	public DataSet getBootstrapSamples() {
		DataSet ds = new DataSet(this.attributes);
		ds.setName(getName());

		int number = this.getExamples().size();
		Random random = new Random();

		for (int i = 0; i < number; i++) {
			ds.add(this.examples.get(random.nextInt(number)));
		}

		return ds;
	}

	public double[] getWeightedClassCounts() throws Exception {
		double[] classCounts = new double[this.attributes.getClassAttribute().size()];

		for (int i = 0; i < this.getExamples().size(); i++) {
			classCounts[this.getExamples().get(i).get(this.attributes.getClassIndex()).intValue()] += this.getExamples()
					.get(i).getWeight();
		}

		return classCounts;
	} // DataSet::getClassCounts

	/* Setter and Getter */

	public int[] getPartitions() {
		return partitions;
	}

	public void setPartitions(int[] partitions) {
		this.partitions = partitions;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public void setExamples(Examples examples) {
		this.examples = examples;
	}

	/**
	 * A main method for testing.
	 *
	 * @param args
	 *            command-line arguments
	 */
	public static void main(String[] args) {
		System.out.println("DataSet Test");

		DataSet dataSet = new DataSet();
		try {

			for (String s : args) {
				System.out.println(s);
			}

			dataSet.load("src/lenses.mff");
			System.out.println(dataSet.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // DataSet::main

} // DataSet class
