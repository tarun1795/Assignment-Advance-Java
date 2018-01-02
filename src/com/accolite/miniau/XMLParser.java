package com.accolite.miniau;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLParser {
	
	public final static String workingDir = System.getProperty("user.dir");
	
	public static void main(String [] args) throws Exception
	{
		File csrDataFile = new File(workingDir+"\\res\\CSRData.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(csrDataFile);
		BufferedWriter allEntry = new BufferedWriter(new FileWriter(workingDir+"\\output\\all.csv"));
		BufferedWriter invalid1 = new BufferedWriter(new FileWriter(workingDir+"\\output\\invalid licenses.csv"));
		BufferedWriter invalid2 = new BufferedWriter(new FileWriter(workingDir+"\\output\\\\invalid license lines.csv"));
		document.getDocumentElement().normalize();
		
		//header .. 1st row in csv
		allEntry.write("nipr,License ID,Jurisdiction,Resident,License Class,License Effective Date,License Expiry Date,License Status,License Line,License Line Effective Date,License Line Expiry Date,License Line Status\n");
		invalid1.write("nipr,License ID,Jurisdiction,Resident,License Class,License Effective Date,License Expiry Date,License Status\n");
		invalid2.write("nipr,License ID,Jurisdiction,Resident,License Class,License Effective Date,License Expiry Date,License Status,License Line,License Line Effective Date,License Line Expiry Date,License Line Status\n");
	
		NodeList list = document.getElementsByTagName("CSR_Producer");
		ArrayList<License> licenseList = new ArrayList<>();
	
		for (int index = 0; index < list.getLength(); index++) {
			Node node = list.item(index);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				NodeList licenses = element.getElementsByTagName("License");
				for(int i =0;i<licenses.getLength();i++)
				{
					License license = new License();
					if(element.getAttribute("NIPR_Number") != null)
						license.nipr = Integer.parseInt(element.getAttribute("NIPR_Number"));
					
					Element lic = (Element)licenses.item(i);
					
					try {
						license.license_id = Integer.parseInt(lic.getAttribute("License_Number"));
					}
					catch(NumberFormatException e)
					{
						license.license_id = 0;
					}
					license.state_code = lic.getAttribute("State_Code");
					try {
						license.effective_date = LocalDate.parse(lic.getAttribute("License_Issue_Date"),DateTimeFormatter.ofPattern("MM/dd/yyyy"));
					}
					catch(DateTimeParseException e)
					{
						license.effective_date = LocalDate.now();
					}
					try {
						license.exp_date = LocalDate.parse(lic.getAttribute("License_Expiration_Date"),DateTimeFormatter.ofPattern("MM/dd/yyyy"));
					}
					catch(DateTimeParseException e)
					{
						license.exp_date = license.effective_date.plusYears(2);
					}
					license.lic_class = lic.getAttribute("License_Class");
					try {
						license.resident = lic.getAttribute("Resident_Indicator").charAt(0);
					}
					catch(StringIndexOutOfBoundsException e)
					{
						license.resident = ' ';
					}
					try {
					license.lic_status = lic.getAttribute("License_Status").charAt(0);
					}
					catch(StringIndexOutOfBoundsException e)
					{
						license.lic_status = ' ';
					}
					licenseList.add(license);
				}
			}
		}
	
		File licenseData = new File(workingDir+"\\res\\LicensesData.xml");
		Document document1 = null;	
		document1 = documentBuilder.parse(licenseData);
		document1.getDocumentElement().normalize();
	
		NodeList nodeListt = document1.getElementsByTagName("CSR_Producer");
		ArrayList<License> licList = new ArrayList<>();
	
		for (int temp = 0; temp < nodeListt.getLength(); temp++) {
			Node nNode = nodeListt.item(temp);
	
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
	
				NodeList licenses = eElement.getElementsByTagName("License");
				for(int i =0;i<licenses.getLength();i++)
				{
			
					Element lic = (Element)licenses.item(i);
			
					NodeList loa = lic.getElementsByTagName("LOA");
					for(int j =0;j<loa.getLength();j++)
					{
						License_LOA _loa = new License_LOA();
						try {
							_loa.nipr = Integer.parseInt(eElement.getAttribute("NIPR_Number"));
						}
						catch(NumberFormatException e)
						{
							_loa.nipr = 0;
						}
						
						try {
							_loa.license_id = Integer.parseInt(lic.getAttribute("License_Number"));
						}
						catch(NumberFormatException e)
						{
							_loa.license_id = 0;
						}
						_loa.state_code = lic.getAttribute("State_Code");
						try {
							_loa.effective_date = LocalDate.parse(lic.getAttribute("License_Issue_Date"),DateTimeFormatter.ofPattern("MM/dd/yyyy"));
						}
						catch(DateTimeParseException e)
						{
							_loa.effective_date = LocalDate.now();
						}
						try {
							_loa.exp_date = LocalDate.parse(lic.getAttribute("License_Expiration_Date"),DateTimeFormatter.ofPattern("MM/dd/yyyy"));
						}
						catch(DateTimeParseException e)
						{
							_loa.exp_date = _loa.effective_date.plusYears(2);
						}
						_loa.lic_class = lic.getAttribute("License_Class");
						try {
							_loa.resident = lic.getAttribute("Resident_Indicator").charAt(0);
						}
						catch(StringIndexOutOfBoundsException e)
						{
							_loa.resident = ' ';
						}
						try {
						_loa.lic_status = lic.getAttribute("License_Status").charAt(0);
						}
						catch(StringIndexOutOfBoundsException e)
						{
							_loa.lic_status = ' ';
						}
						Element line = (Element) loa.item(j);
						_loa.loa = line.getAttribute("LOA_Name");	
						try {
							_loa.loa_effective_date = LocalDate.parse(line.getAttribute("LOA_Issue_Date"),DateTimeFormatter.ofPattern("MM/dd/yyyy"));
						
						}
						catch(DateTimeParseException e)
						{
							_loa.loa_effective_date = LocalDate.now();
						}
						_loa.loa_exp_date = _loa.loa_effective_date.plusYears(2);
						try {
							_loa.loa_status = line.getAttribute("LOA_Status").charAt(0);
						}
						catch(StringIndexOutOfBoundsException e)
						{
							_loa.loa_status = ' ';
						}
						if(licenseList.contains(_loa))
						{
							allEntry.write(_loa.toString());
						}
						else
						{
							invalid2.write(_loa.toString());
						}
						licList.add(_loa);
					}
				}	
			}
		}
		
		for(int i=0;i<licenseList.size();i++)
		{
			License lic = licenseList.get(i);
			if (!licList.contains(lic))
			{
				invalid1.write(lic.toString());
			}
		}
	
		allEntry.close();
		invalid1.close();
		invalid2.close();
		System.out.println("CSV Files Generated into output folder");
	}
}