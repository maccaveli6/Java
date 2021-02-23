import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RentalDueTest {
	private static int length; private static int[] apt_idx = new int[99]; private static int[] house_idx = new int[99];
	private static int apt_counter=0; private static int house_counter=0; 	
	private static RentalProperty[] rentals = new RentalProperty[99]; //private static RentalProperty[] rentals = new RentalProperty[99];
	private static final int fields = 5; //how many columns in rentalDB
	public static double apt_increase = .08; public static double house_increase = .04; //rate increases apartment/singleFamuly 
	private static String filename = "rentalDB.txt";
	
	public static void main(String[] args) {
		
		loadData();
		finalizeData();
	}
	
	
	
	private static void loadData() {
		int i = 0; 
		try {
			Scanner in = new Scanner(new File(filename));
			while (in.hasNextLine()) {
				String input = in.nextLine();
				String[] line = splitStringHelper(input);
//				Check to see if the property is single resident or apartment
				if (line[1].equals("S")) {
					rentals[i] = new SingleFaimlyRental(line[0],line[1],line[2],line[3],line[4]);
					house_idx[house_counter] = i; 
					house_counter++;
					
				}
				else {
					rentals[i] = new ApartmentRental(line[0],line[1],line[2],line[3],line[4]);
					apt_idx[apt_counter] = i;
					apt_counter++;
				}
				i++;
			}
			length = i;
			in.close();

		}
		catch (Exception e){
			System.out.println("OH NO Something went wrong");
		}
	}
	private static String[] splitStringHelper(String input){
		String[] line = new String[fields];
		int i = 0;
		//remove leading whitespace
		String[] word = input.split(" ");
		for(int j = 0 ; j <word.length; j++) {
			if(word[j].equals("")) {
				continue;
			}
			else {
				String temp = word[j];
				line[i] = temp;
				i++;
			}
		}
		for(int j = 0 ; j <line.length; j++) {
			
		}
	return line;
	}

	
	private static void finalizeData() {
		Hashtable<String, String> apts = new Hashtable<String, String>(); Hashtable<String, String> houses = new Hashtable<String, String>();
		String[] house_id_list = new String[house_counter]; String[] apt_id_list = new String[apt_counter];
//		List<String> house_id_list_exp = new ArrayList<>(); List<String> apt_id_list_exp = new ArrayList<>(); //experiment with List class and Collections FUNCTIONAL
		//***file printout***
		try(FileWriter f = new FileWriter(filename, false)){
			
			for (int i = 0; i < length; i++) {
				String newRent = rentals[i].updateRent(rentals[i]);
				String temp = String.format("%s%10s\n",rentals[i].propertyDetails(), newRent);
				f.write(temp);
			}
			f.close();
		}
			catch (IOException e){
				System.out.println("There was an error writing to file");
			}

		
		//***Print to Screen***
		
		//load dictionaries for hashing sorted results
		// is there a more efficient way?
		
		for (int i = 0; i < house_counter; i++) {
			houses.put(rentals[house_idx[i]].getRentalID(),rentals[house_idx[i]].updateRent(rentals[house_idx[i]])); //Load Dictionary
			house_id_list[i] = rentals[house_idx[i]].getRentalID(); //load house id list as dict reference
//			house_id_list_exp.add(rentals[house_idx[i]].getRentalID()); //Experimental code for List FUNCTIONAL
		}
		 
		for (int i = 0; i < apt_counter; i++) {
			apts.put(rentals[apt_idx[i]].getRentalID(),rentals[apt_idx[i]].updateRent(rentals[apt_idx[i]]));//Load Dictionary
			apt_id_list[i] = rentals[apt_idx[i]].getRentalID();//load apt id list as dict reference 
//			apt_id_list_exp.add(rentals[apt_idx[i]].getRentalID());	//Experimental code for List FUNCTIONAL
		}
		
		//Experimental code for List sorting FUNCTIONAL
//		Collections.sort(house_id_list_exp);
//		Collections.sort(apt_id_list_exp);



		
		sortHelper(house_id_list);
		sortHelper(apt_id_list);
		

		System.out.println("\n\nSingle-Family Rental Summary:");
		System.out.println("House ID Number          Rental Due");
		System.out.println("===============          ==========");

		printHelper(house_id_list, houses);
//		System.out.print("\n\n List Values:\n");//Experimental code for List FUNCTIONAL
//		printHelper(house_id_list_exp, houses);//Experimental code for List FUNCTIONAL

		System.out.println("\nApartment Rental Summary:");
		System.out.println("Apartment ID No.         Rental Due");
		System.out.println("================         ==========");

		printHelper(apt_id_list, apts);
//		System.out.print("\n\nList Values\n");//Experimental code for List FUNCTIONAL
//		printHelper(apt_id_list_exp, apts);//Experimental code for List FUNCTIONAL
	}
	private static void sortHelper (String[] id_list) { //insertion sort algo
		String current_address;
		int i = 1; 
		while (i < id_list.length) {
			current_address = id_list[i];
			int j = i - 1; int k = i; 
			while (j >= 0) {
				if (id_list[j].compareTo(current_address) > 0) {
					
					id_list[k] = id_list[j]; 
					
					j--;
					k--;
				}
				else {
					break;
				}
			}
			id_list[j+1] = current_address; //added +1 to offset j-- in while loop
			i++;
		}
	}
	private static void printHelper(String[] id_list, Hashtable<String, String> dict) {
		for (int i = 0; i < id_list.length; i++) {
			String temp =  "$" + dict.get(id_list[i]);
			System.out.printf("%10s%25s\n",id_list[i], temp);
		}
	}
//	private static void printHelper(List<String> id_list, Hashtable<String, String> dict) { //Overload for Lists experiment FUNCTIONAL
//		for (int i = 0; i < id_list.size(); i++) {
//			String temp =  "$" + dict.get(id_list.get(i));
//			System.out.printf("%10s%25s\n",id_list.get(i), temp);
//		}
//	}
	public static int stringToInt(String str) {
		try {
			int i = Integer.parseInt(str);
			return i;
			
			
		}
		catch (Exception e) {
			System.out.printf("Error while converting %s to Int\n",str);
			return 0;
		}

	}
}
