package com.codetutr.restAPI.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codetutr.entity.User;
import com.codetutr.restAPI.response.TWMResponse;
import com.codetutr.restAPI.response.TWMResponseFactory;
import com.codetutr.utility.converter.HTMLToPDFConverter;
import com.itextpdf.text.DocumentException;

@RestController
@RequestMapping("/api/common")
public class CommonController extends AbstractRestController {

	@Autowired(required=false)
	JobLauncher jobLauncher;

	@Autowired(required=false)
	Job processJob;
	
	@Autowired
	Environment env;

	@GetMapping(value = "/getPDF", produces = MediaType.APPLICATION_PDF_VALUE)
	public byte[] convertToPDF(HttpServletResponse response) throws IOException, DocumentException {
		return new HTMLToPDFConverter().convertHtmlToPDF();
	}

	@GetMapping(value = "/batch", produces = MediaType.APPLICATION_JSON_VALUE)
	public TWMResponse<List<User>> startBatchJob(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		if(processJob == null || jobLauncher == null) {
			throw new RuntimeException("Batch Job could not be trigerred with a profile " + Arrays.asList(env.getActiveProfiles()) + ".Please change the profile to either [Mock], [SpringDataJPA] or [SpringEmJPA]");
		}
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution result = jobLauncher.run(processJob, jobParameters);
        if (result.getStatus().toString().equals("FAILED")){
        	throw new RuntimeException("Batch JOB failed !! Your database might already contains the users you are trying to run as a batch. Please Make sure and re-run it again!!");
        }
        
        List<User> allUsers = userService.getAllUsers();
        return TWMResponseFactory.getResponse(allUsers, request);
	}
}
