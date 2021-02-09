
package com.codetutr.config.drools;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.springframework.context.annotation.Bean;

public class AppConfig_Drools {
	
	/**
	 * You must comment out below dependency from pom.xml for this to work.otherwise, the rule will not get fired
	 * 
	 *	<!-- Development tools related -->
	    <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>	
	 * 
	 */
	@Bean
	public KieContainer kieContainer() {
		return KieServices.Factory.get().getKieClasspathContainer();
	}
	
	
	/*
	private static final String RESOURCE_PATH = "src/main/resources/";
	private static final String DROOLS_FOLDER = "drools/";
	
	 @Bean
     public KieContainer kieContainer11() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        for(String file:new ArrayList<String>(UtilityHelper.listAllFilesInsideDirectory(RESOURCE_PATH + DROOLS_FOLDER))){
        	if(file.toLowerCase().contains(".drl")){
        		kieFileSystem.write(ResourceFactory.newClassPathResource(DROOLS_FOLDER + file));
        	}
        }
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();

        return kieServices.newKieContainer(kieModule.getReleaseId());
    }
    */
}
