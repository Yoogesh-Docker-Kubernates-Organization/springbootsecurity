package com.codetutr;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.codetutr.utility.converter.HTMLToPDFConverter;
import com.itextpdf.text.DocumentException;

@SuppressWarnings("unused")
public class Main {

	public static void main(String[] args) throws DocumentException, IOException {
		
		Main run = new Main();
		
		//run.convertHtmlFileToPDF();
		
		
		System.out.println(new Date());
	}

	private void convertHtmlFileToPDF() throws IOException, DocumentException {
		HTMLToPDFConverter converter = new HTMLToPDFConverter();
		converter.convertHtmlFileToPDF();
	}

}
