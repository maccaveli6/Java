
public class Box implements Holder {

	private int SIZE_OF = 1; // made small to demonstrate resize within given context
	private Object[] contents = new Object[SIZE_OF];

	public void add(Object obj) {// Put obj in the Holder
		int idx = getSize();

		if (contents.length == idx) {
			contents = resizeBox();
		}
		contents[idx] = obj;
	}

	public void remove(Object obj) throws Exception { // Remove obj from the Holder or throw an exception if there is no
														// such obj in the Holder.
		int idx = 0;
		int location = 0;
		boolean found = false;
		while (idx < contents.length) {
			if (contents[idx] == obj) {
				found = true;
				location = idx;
			}
			if (contents[idx] == null) {
				break;
			} else {
				idx++;
			}
		}
		// remove element
		if (found) {
			while (location < idx - 1) {
				contents[location] = contents[location + 1];
				location++;
			}
			contents[idx - 1] = null;
		} else {
			throw new Exception("Item is not in list");
		}
	}

	public boolean contains(Object obj) { // Does Holder already hold obj?
		int idx = 0;
		while (idx < contents.length) {
			if (contents[idx] == obj) {
				return true;
			}
			if (contents[idx] == null) {
				break;
			} else {
				idx++;
			}
		}
		return false;
	}

	public boolean isEmpty() { // Is Holder empty?
		int idx = getSize();
		if (idx == 0) {
			return true;
		} else {
			return false;
		}

	}

	private Object[] resizeBox() {
		Object[] temp = new Object[contents.length * 2];

		for (int i = 0; i < contents.length; i++) {
			temp[i] = contents[i];
		}
		return temp;
	}

	private int getSize() {
		int idx = 0;
		while (idx < contents.length) {
			if (contents[idx] == null) {
				break;
			} else {
				idx++;
			}
		}
		return idx;
	}
}