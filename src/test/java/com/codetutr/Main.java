package com.codetutr;

import java.io.IOException;
import java.util.Date;

import com.codetutr.utility.converter.HTMLToPDFConverter;
import com.itextpdf.text.DocumentException;

@SuppressWarnings("unused")
public class Main {

	public static void main(String[] args) throws DocumentException, IOException {
		
		//convertHtmlFileToPDF();
		//convertServerTimeToDate();
	}

	private static void convertServerTimeToDate() {
		Date date = new Date(System.currentTimeMillis());
		System.out.println(date);
	}

	private static void convertHtmlFileToPDF() throws IOException, DocumentException {
		HTMLToPDFConverter converter = new HTMLToPDFConverter();
		converter.convertHtmlFileToPDF();
	}

}
