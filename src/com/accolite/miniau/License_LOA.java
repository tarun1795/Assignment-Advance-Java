package com.accolite.miniau;

import java.time.LocalDate;

public class License_LOA extends License{
	String loa ;
	LocalDate loa_effective_date;
	LocalDate loa_exp_date;
	char loa_status;
	
	public License_LOA() {
		super();
	}

	@Override
	public String toString() {
		return "" + nipr + "," + license_id + "," + state_code
				+"," + resident + "," +lic_class
				+ "," + effective_date +  "," +  exp_date + "," + lic_status + ","+ loa + "," + loa_effective_date + ","
				+ loa_exp_date + "," 
				+ loa_status + "\n";
	}
	
}