package com.codetutr;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.util.CollectionUtils;

import com.codetutr.utility.converter.HTMLToPDFConverter;
import com.itextpdf.text.DocumentException;

@SuppressWarnings("unused")
public class Main {

	public static void main(String[] args) throws DocumentException, IOException {
		
		Main run = new Main();
		
		//run.convertHtmlFileToPDF();
		//run.getDifferentTimeZoneTime();

		Calendar orderSubmittedCalender = Calendar.getInstance();
		orderSubmittedCalender.setTimeInMillis(1601009848000L); //1601013448000L
		setTimeZone(orderSubmittedCalender, "US/Central");
		//setTimeZone(orderSubmittedCalender, "America/New_York");
		
		System.out.println(orderSubmittedCalender.get(Calendar.DAY_OF_YEAR));
		System.out.println(orderSubmittedCalender);
		System.out.println(new Date(orderSubmittedCalender.getTimeInMillis()));
		
		Calendar orderSubmittedCalender1 = Calendar.getInstance();
		orderSubmittedCalender1.setTimeInMillis(1601042400000L);
		//setTimeZone(orderSubmittedCalender1, "US/Central");
		//setTimeZone(orderSubmittedCalender1, "America/New_York");
		
		System.out.println(orderSubmittedCalender1.get(Calendar.DAY_OF_YEAR));
		System.out.println(orderSubmittedCalender1);
		System.out.println(new Date(orderSubmittedCalender1.getTimeInMillis()));
	}


	private void convertHtmlFileToPDF() throws IOException, DocumentException {
		HTMLToPDFConverter converter = new HTMLToPDFConverter();
		converter.convertHtmlFileToPDF();
	}
	
	private void getDifferentTimeZoneTime() {
		Calendar orderSubmittedCalender = Calendar.getInstance();
		orderSubmittedCalender.setTimeInMillis(1596549600000L - 10800000L);
		
		setTimeZone(orderSubmittedCalender, "America/Los_Angeles");
		System.out.println(orderSubmittedCalender);
		
		setTimeZone(orderSubmittedCalender, "US_Arizona");
		System.out.println(orderSubmittedCalender);
	}
	
	private static void setTimeZone(Calendar cal, String timeZone) {
		TimeZone tz = TimeZone.getTimeZone(timeZone);
		cal.setTimeZone(tz);
	}

}
