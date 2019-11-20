/*
 * Evaluator.java
 * Copyright (c) 2017 Georgetown University.  All Rights Reserved.
 */

/**
 * 
 *
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 2.0, 1/30/17
 */

public class Evaluator implements OptionHandler {

	private int folds = 10;

	private Classifier classifier;

	private TrainTestSets tts;

	private double holdOut = 0.0;

	public Evaluator(Classifier classifier, String[] options) throws Exception {
		this.classifier = classifier;
		this.setOptions(options);
	}

	public int getFolds() {
		return folds;
	}

	public Performance evaluate() throws Exception {

		if (this.tts.test.getExamples().size() == 0 && this.holdOut == 0.0) {
			// BuildCVPartition
			this.tts.getTrainingSet().buildCVPartition(folds);
			// CrossValidation
			Performance performance = new Performance(tts.getTrainingSet().getAttributes());
			performance.setName(tts.getTrainingSet().getName());

			for (int i = 0; i < this.folds; i++) {
				this.tts.getTrainingSet().setFolds(folds);
				this.tts.getTestingSet().setFolds(folds);
				TrainTestSets ttsnew = tts.getTrainingSet().getCVSets(i);

				this.classifier.train(ttsnew.getTrainingSet());
				performance.add(this.classifier.classify(ttsnew.test));
			}

			return performance;
		} else if (this.holdOut > 0.0) {
			// -t -p
			this.tts = tts.getTrainingSet().getHoldOutSets(holdOut);
			classifier.train(tts.getTrainingSet());
			Performance performance = new Performance(tts.getTrainingSet().getAttributes());
			performance.setName(tts.getTrainingSet().getName());
			performance = this.classifier.classify(tts.test);

			return performance;
		} else {
			// -t -T
			classifier.train(tts.getTrainingSet());
			Performance performance = new Performance(tts.getTrainingSet().getAttributes());
			performance.setName(tts.getTrainingSet().getName());
			performance = this.classifier.classify(tts.test);

			return performance;
		}
	}

	public void setFolds(int folds) throws Exception {
		this.folds = folds;
	}

	public void setOptions(String args[]) throws Exception {
		if (args.length < 1) {
			throw new IllegalArgumentException("The args are null.");
		}
		this.tts = new TrainTestSets(args);
		this.classifier.setOptions(args);

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-x")) {
				this.folds = Integer.valueOf(args[i + 1]);
			}
			if (args[i].equals("-p")) {
				this.holdOut = Double.valueOf(args[i + 1]);
			}
		}
	}

	public Classifier getClassifier() {
		return classifier;
	}

	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}

	public TrainTestSets getTts() {
		return tts;
	}

	public void setTts(TrainTestSets tts) {
		this.tts = tts;
	}

}
