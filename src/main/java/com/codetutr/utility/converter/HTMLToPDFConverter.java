package com.codetutr.utility.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class HTMLToPDFConverter {
	
	private final Logger logger = LoggerFactory.getLogger(HTMLToPDFConverter.class);
	
	private static final String SourceFileName = "src/main/resources/static/html/source.html";
	private static final String targetFileName = "src/main/resources/static/pdf/target.pdf";
	private static final String HTML_CONTENT = "<html>\r\n" + 
			"<head>\r\n" + 
			"<style type=\"text/css\">\r\n" + 
			"body {\r\n" + 
			"	margin: 0;\r\n" + 
			"	padding: 0;\r\n" + 
			"}\r\n" + 
			"</style>\r\n" + 
			"</head>\r\n" + 
			"<body>\r\n" + 
			"   <div>\r\n" + 
			"	<span style=\"font-size:50px; color:red;\">Welcome!!</span>\r\n" + 
			"   </div>\r\n" + 
			"</body>\r\n" + 
			"</html>";
	
	public byte[] convertHtmlToPDF() throws IOException, DocumentException {
		
		if(new Random().nextInt(10) < 5) {
			logger.info("Trying to convert HTML-CONTENT to PDF file.....");
			convertHtmlContentToPDF();
		}
		else {
			logger.info("Trying to convert HTML-FILE to PDF file.....");
			convertHtmlFileToPDF();
		}
		
		logger.info("Successfully converted to PDF file.Trying to return pdf stream....");
		return convertPDFFiletoPDFStream();
	}
	
	
	public void convertHtmlContentToPDF() throws IOException {
		HtmlConverter.convertToPdf(HTML_CONTENT, new FileOutputStream(targetFileName));
	}
	
	public void convertHtmlFileToPDF() throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(targetFileName));
		document.open();
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(SourceFileName));
		document.close();
	}
	
	public byte[] convertPDFFiletoPDFStream() throws IOException {
		File file = new File(targetFileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        return IOUtils.toByteArray(fileInputStream);
	}
}
