// package medianList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

// Phiilp Garcia CS2251 Spring 2021

public class MedianListTest {

	public static void main(String[] args) {
		int NUM_ITERS = 10;
		// test loop added to complete validation

		for (int i = 0; i < NUM_ITERS; i++) {
			System.out.println("Testing for correct median " + testCorrectMedian());
			System.out.println("Testing for correct string " + testCorrectString());
		}

		// test assertions in pop

		ArrayList<Integer> test_assertion = new ArrayList<Integer>();
		test_assertion.add(1);
		test_assertion.add(2);
		test_assertion.add(3);
		test_assertion.add(4);

		// Uncomment to test assertion for less than
		// MedianList test = new MedianList(test_assertion);
		// test.push(1);

		// Comment assertion for less than above and Uncomment below to test assertion
		// for greater than
		// test_assertion.add(5);
		// MedianList test = new MedianList(test_assertion);
		// test.push(6);
	}

	public static boolean isMedian(int x, ArrayList<Integer> values) {
		Collections.sort(values);
		int correct_median = values.get(values.size() / 2);
		if (x == correct_median) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean testCorrectMedian() {
		// create a test array
		int reported_median;
		Random random = new Random();
		int MAX_SIZE = 100;
		int MAX_ELEMENT = 1000;
		// create a new array of random size < MAX_SIZE elements
		int array_size = random.nextInt(MAX_SIZE);
		ArrayList<Integer> test_array = new ArrayList<Integer>(array_size);
		for (int i = 0; i < array_size - 1; i++) {
			test_array.add(random.nextInt(MAX_ELEMENT));
		}
		// get reported median from class MedianList
		MedianList test_list = new MedianList(test_array);
		try {
			reported_median = test_list.peek();
			// compute correct median for the test array
			return isMedian(reported_median, test_array);
		} catch (Exception e) {
			System.out.println("The test array should be empty, is this true? " + test_array.isEmpty());
			return true;
		}
	}

	public static boolean testCorrectString() {
		// create a test array
		Random random = new Random();
		int MAX_SIZE = 100;
		int MAX_ELEMENT = 1000;
		// create a new array of random size < MAX_SIZE elements
		int array_size = random.nextInt(MAX_SIZE);
		ArrayList<Integer> test_array = new ArrayList<Integer>(array_size);
		for (int i = 0; i < array_size - 1; i++) {
			test_array.add(random.nextInt(MAX_ELEMENT));
		}
		// get reported string from class MedianList
		MedianList test_list = new MedianList(test_array);
		// compare the two strings
		if (test_array.toString().equals(test_list.toString())) {
			return true;
		} else {
			return false;
		}
	}
}
