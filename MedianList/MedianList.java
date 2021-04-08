// package medianList;

import java.util.ArrayList;
import java.util.Collections;

//Phiilp Garcia CS2251 Spring 2021 

public class MedianList {
	public ArrayList<Integer> value = new ArrayList<Integer>();
	public ArrayList<Integer> value_sorted;

	public MedianList() {

	}

	@SuppressWarnings("unchecked")
	public MedianList(ArrayList<Integer> input) {
		value = (ArrayList<Integer>) input.clone();
	}

	public void push(int x) {
		if (value.size() == 0) {
			value.add(x);
			assert false : "the new median is greater than or equal to the old";
		} else {
			int previous_median = peek();
			value.add(x);
			assert peek() > previous_median : "the new median is less than or equal to the old";
			assert peek() < previous_median : "the new median is greater than or equal to the old";
		}
	}

	@SuppressWarnings("unchecked")
	public int pop() throws IndexOutOfBoundsException {
		if (value.size() == 0) {
			throw new IndexOutOfBoundsException();
		} else {
			int median;
			// copy unsorted list to a new sorted list for tracking median
			value_sorted = (ArrayList<Integer>) value.clone();
			Collections.sort(value_sorted);
			// get median
			int median_idx = value_sorted.size() / 2;
			median = value_sorted.get(median_idx);
			// remove median value
			value.remove(new Integer(median));
			return median;
		}
	}

	@SuppressWarnings("unchecked")
	public int peek() throws IndexOutOfBoundsException {
		if (value.size() == 0) {
			throw new IndexOutOfBoundsException();
		} else {
			int median;
			// copy unsorted list to a new sorted list for tracking median
			value_sorted = (ArrayList<Integer>) value.clone();
			Collections.sort(value_sorted);
			// get median
			int median_idx = value_sorted.size() / 2;
			median = value_sorted.get(median_idx);

			return median;
		}
	}

	public boolean isEmpty() {
		if (value.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return value.toString();
	}
}
