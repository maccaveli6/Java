
public class Tile implements Comparable<Tile> {
	// intance variables
	private char letter;
	private int value;

	private int count_setter = 0;

	// constructor
	public Tile(char letter, int value) {
		this.letter = letter;
		this.value = value;
	}

	public char getLetter() {
		return letter;
	}

	public int getValue() {
		return value;
	}

	public void setLetter(char new_letter) {
		this.letter = new_letter;
	}

	public void setValue(int new_value) {
		this.value = new_value;
	}

	@Override
	public String toString() {
		return "The letter " + letter + " is worth " + value + " points.";
	}

	@Override
	public boolean equals(Object obj) {
		Tile other = (Tile) obj;
		if (letter == other.getLetter() && value == other.getValue())
			return true;
		else
			return false;
	}

	public int compareTo(Tile tile) {
		if (letter == tile.letter)
			return 0;
		else if (letter < tile.letter)
			return 1;
		else
			return -1;
	}
}