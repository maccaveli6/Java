//package scarable_exam2;
// Philip Garcia CS251 Spring 2021

// Part F: Trying to print the Tile object directly prints the package name followed by the object type (Tile) and the hex address the object is located (@15db9742)
// Part H: The default equals performs a shallow comparison of the two objects and will return false, unless those objects are at the same address in memory.  
// in the case of t1.equals(t2), t1 is located at scarable_exam2.Tile@15db9742 while t2 is located at scarable_exam2.Tile@6d06d69c.  
// Therefore, t1.equals(t2) returns false.
// Part I:  The overridden equals method performs a deep comparison of each value for tiles t1 and t2.  Now, t1.equals(t2) returns true, saying the values in t1 equal those in t2

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("testTile PartD:");
		testTilePartD(); // testTile method from partD
		// tileCompare(); // verify default/updated .equals() method
		System.out.println("testTile Final:");
		testTile(); // testTile method from partG
	}

	private static void printTilePartD(Tile tile) {
		System.out.println("Letter " + tile.getLetter() + " Value " + tile.getValue());
	}

	private static void printTile(Tile tile) {
		System.out.println(tile.toString());
	}

	private static void testTilePartD() {
		Tile testTile = new Tile('Z', 10);
		printTilePartD(testTile);
	}

	private static void testTile() {
		Tile testTile = new Tile('Z', 10);
		printTile(testTile);
	}

	private static void tileCompare() {
		Tile t1 = new Tile('Z', 10);
		Tile t2 = new Tile('Z', 10);
		System.out.println("tile 1 is " + t1.toString());
		System.out.println("tile 2 is " + t2.toString());
		System.out.println("t1 = t2 " + t1.equals(t2));
		Tile t3 = new Tile('A', 1);
		System.out.println("tile 1 is " + t1.toString());
		System.out.println("tile 3 is " + t3.toString());
		System.out.println("t1 = t3 " + t3.equals(t1));
	}

}
