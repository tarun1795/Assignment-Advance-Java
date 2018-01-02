package com.accolite.miniau;

import java.time.LocalDate;

public class License {
	int nipr;
	int license_id;
	String state_code;
	LocalDate effective_date;
	char resident;
	String lic_class;
	LocalDate exp_date;
	char lic_status;
	
	public License() {
	
		nipr = 0;
		license_id = 0;
		state_code = "";
		effective_date = LocalDate.now();
		resident = ' ';
		lic_class = "";
		exp_date = LocalDate.now();
		lic_status = ' ';
	}
	
	@Override
	public String toString() {
		return "" + nipr + "," + license_id + "," + state_code
				+"," + resident + "," +lic_class
				+ "," + effective_date +  "," +  exp_date + "," + lic_status + "\n";
	}
	
	@Override
	public boolean equals(Object object) {
		
		boolean isEqual = true;
		if (object == null)
			isEqual = false;
		License license = (License) object;
		if (effective_date == null) {
			if (license.effective_date != null)
				isEqual = false;
		} else if (!effective_date.equals(license.effective_date))
			isEqual = false;
		if (license_id != license.license_id)
			isEqual = false;
		if (nipr != license.nipr)
			isEqual = false;
		if (state_code == null) {
			if (license.state_code != null)
				isEqual = false;
		} 
		else if (!state_code.equals(license.state_code))
			isEqual = false;
		return isEqual;
	}
	
	
}