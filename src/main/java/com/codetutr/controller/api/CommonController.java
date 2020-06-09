package com.codetutr.controller.api;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codetutr.utility.converter.HTMLToPDFConverter;
import com.codetutr.validationHelper.LemonConstant;
import com.itextpdf.text.DocumentException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/common")
@Api(tags = {LemonConstant.SWAGGER_COMMON_DESCRIPTION})
public class CommonController {
	
	@GetMapping(value="/getPDF", produces = MediaType.APPLICATION_PDF_VALUE)
	@ApiOperation(value="Get the pdf file", notes="This url is used to get the pdf file", response=byte[].class )
    public byte[] convertToPDF(HttpServletResponse response) throws IOException, DocumentException {
		
		return new HTMLToPDFConverter().convertHtmlToPDF();
    }
}
