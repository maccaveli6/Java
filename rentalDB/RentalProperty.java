
public class RentalProperty implements Payment{
	protected int sequence_id; protected int bedrooms;
	protected char rental_type;
	protected String rental_ID;
	protected double rent; protected double hike;
	public RentalProperty (String seq_id, String rentalType, String rentalID, String bdRms, String rnt, double increase) {

			sequence_id = RentalDueTest.stringToInt(seq_id);	
			bedrooms = RentalDueTest.stringToInt(bdRms);
			
			rental_ID = rentalID;
			
			rental_type = rentalType.charAt(0);

			hike = increase;
		try {			
			rent = Double.valueOf(rnt);
		}
		catch (Exception e){
			System.out.printf("Sequence ID Error %s\n", seq_id);
		}
	}
	public String propertyType() {
		String temp = "I am a generic property";
		return temp; 
	}
	public String propertyDetails() {
		String temp = String.format("%6d%4c%10s%3s",sequence_id, rental_type,rental_ID,bedrooms);
		return temp;
	}
	public double getRent() {
		return rent;
	}
	public double getHike() {
		return hike;
	}
	public String getRentalID() {
		return rental_ID;
	}
}




interface Payment {
	default String updateRent(RentalProperty property) {
//		private double rent =  property.getRent(); 
//		private double hike = property.getHike();
		return String.format("%.2f",(1+property.getHike())*property.getRent());
	}
}
