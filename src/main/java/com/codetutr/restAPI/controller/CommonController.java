package com.codetutr.restAPI.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codetutr.utility.converter.HTMLToPDFConverter;
import com.itextpdf.text.DocumentException;

@RestController
@RequestMapping("/api/common")
public class CommonController {
	
	@GetMapping(value="/getPDF", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] convertToPDF(HttpServletResponse response) throws IOException, DocumentException {
		return new HTMLToPDFConverter().convertHtmlToPDF();
    }
}
