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
		run.getDifferentTimeZoneTime();
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
