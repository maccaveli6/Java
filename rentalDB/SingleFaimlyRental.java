
public class SingleFaimlyRental extends RentalProperty {

	public SingleFaimlyRental(String seq_id, String rentalType, String rentalID, String bdRms, String rnt) {
		super(seq_id, rentalType, rentalID, bdRms, rnt, RentalDueTest.house_increase);
		
	}
	@Override
	public String propertyType() {
		String temp = "House";
		return temp;
	}
}
