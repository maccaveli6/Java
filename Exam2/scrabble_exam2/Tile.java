//package scarable_exam2;

public class Tile {
	private int value;
	private char letter;

	Tile(char letter, int val) {
		this.value = val;
		this.letter = letter;
	}

	public int getValue() {
		return this.value;
	}

	public char getLetter() {
		return this.letter;
	}

	@Override
	public String toString() {
		return this.letter + " " + this.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + letter;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (letter != other.letter)
			return false;
		if (value != other.value)
			return false;
		return true;
	}
}
