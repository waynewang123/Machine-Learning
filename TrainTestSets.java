
// Name: Zhuoran Wu
// E-mail: <zw118@georgetown.edu>
// Platform: Ubuntu 16.04 LTS
//
// In accordance with the class policies and Georgetown's Honor Code,
// I certify that, with the exceptions of the class resources and those
// items noted below, I have neither given nor received any assistance
// on this project.
//

/*
 * TrainTestSets.java
 * Copyright (c) 2011 Georgetown University.  All Rights Reserved.
 */

/**
 * Implements a class for storing training and testing sets for machine-learning
 * methods.
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 1.0, 1/22/17
 */

public class TrainTestSets implements OptionHandler {

	/** the training examples */
	protected DataSet train;

	/** the testing examples */
	protected DataSet test;

	/** Default constructor. */

	public TrainTestSets() {
		this.train = new DataSet();
		this.test = new DataSet();
	}

	/**
	 * Explicit constructor that processes the specified arguments.
	 *
	 * @param options
	 *            the arguments for this train/test set
	 * @throws Exception
	 *             if the file is not found or if a parsing exception occurs
	 */

	public TrainTestSets(String[] options) throws Exception {
		this.train = new DataSet();
		this.test = new DataSet();
		this.setOptions(options);
	} // TrainTestSets::TrainTestSets

	/**
	 * Explicit constructor that sets the training and testing sets to the
	 * specified data sets.
	 *
	 * @param train
	 *            the training set
	 * @param test
	 *            the testing set
	 */

	public TrainTestSets(DataSet train, DataSet test) {
		this.train = train;
		this.test = test;
	} // TrainTestSets::TrainTestSets

	/**
	 * Returns the training set of this train/test set.
	 *
	 * @return a training set
	 */

	public DataSet getTrainingSet() {
		return this.train;
	} // TrainTestSets::getTrainingSet

	/**
	 * Returns the testing set of this train/test set.
	 *
	 * @return a testing set
	 */

	public DataSet getTestingSet() {
		return this.test;
	} // TrainTestSets::getTestingSet

	/**
	 * Sets the training set of this train/test set to the specified data set.
	 *
	 * @param train
	 *            the specified training set
	 */

	public void setTrainingSet(DataSet train) {
		this.train = train;
	} // TrainTestSets::setTrainingSet

	/**
	 * Sets the testing set of this train/test set to the specified data set.
	 *
	 * @param test
	 *            the specified testing set
	 */

	public void setTestingSet(DataSet test) {
		this.test = test;
	} // TrainTestSets::setTestingSet

	/**
	 * Sets the options for this train/test set. The <code>-t</code> option
	 * loads the data set with the specified file name as the training set. The
	 * <code>-T</code> option loads the data set with the specified file name as
	 * the testing set.
	 *
	 * @param options
	 *            the arguments
	 * @throws Exception
	 *             if the file is not found or if a parsing exception occurs
	 */

	public void setOptions(String[] options) throws Exception {

		for (int i = 0; i < options.length; i = i + 2) {
			if (options[i].equals("-t")) {
				// Train Data
				this.train = new DataSet();
				this.train.load(options[i + 1]);
			}

			if (options[i].equals("-T")) {
				// Test Data
				this.test = new DataSet();
				this.test.load(options[i + 1]);
			}

			if (options[i].equals("-s") || options[i].equals("-x")) {
				String[] option = new String[2];
				option[0] = options[i];
				option[1] = options[i + 1];
				this.train.setOptions(option);
				this.test.setOptions(option);
			}
		}

	} // TrainTestSets::setOptions

	/**
	 * Returns a string representation of this train/test set in a format
	 * similar to that of the file format. Includes the testing examples if
	 * present.
	 *
	 * @return a string containing the attributes information and examples
	 */

	public String toString() {
		String twoDataSet = "";

		if (train.name != "") {
			twoDataSet += train.toString();
		}

		twoDataSet += "\n\n";

		if (test.name != "") {
			twoDataSet += test.toString();
		}
		return twoDataSet;
	} // TrainTestSets::toString

} // TrainTestSets class
