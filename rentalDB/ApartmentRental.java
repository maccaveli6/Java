
 class ApartmentRental extends RentalProperty {

	
	public ApartmentRental(String seq_id, String rentalType, String rentalID, String bdRms, String rnt) {
		super(seq_id, rentalType, rentalID, bdRms, rnt, RentalDueTest.apt_increase);
	}
	@Override
	public String propertyType() {
		String temp = "Apartment";
		return temp;
	}
}
