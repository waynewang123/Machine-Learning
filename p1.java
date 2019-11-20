// Name: Zhuoran Wu
// E-mail: <zw118@georgetown.edu>
// Platform: Ubuntu 16.04 LTS
//
// In accordance with the class policies and Georgetown's Honor Code,
// I certify that, with the exceptions of the class resources and those
// items noted below, I have neither given nor received any assistance
// on this project.
//

/**
 * 
 * @author Zhuoran Wu <zw118@georgetown.edu>
 * @version 1.0, 1/22/17
 */

public class p1 {

	/*
	 * A main method that takes the name of training and testing examples from
	 * the command line, reads them in, and prints them to the console.
	 */

	public static void main(String args[]) {
		try {
			TrainTestSets tts = new TrainTestSets();
			tts.setOptions(args);
			System.out.println(tts);
		} // try
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} // catch
	} // p1::main

} // p1 class
