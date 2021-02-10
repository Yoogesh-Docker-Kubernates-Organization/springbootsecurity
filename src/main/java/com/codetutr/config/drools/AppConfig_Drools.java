
package com.codetutr.config.drools;

import java.util.ArrayList;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import com.codetutr.utility.UtilityHelper;

public class AppConfig_Drools {
	
	private static final String RESOURCE_PATH = "src/main/resources/";
	private static final String DROOLS_FOLDER = "META-INF/drools/com/yoogesh/rules/";
	
	@Bean
	@ConditionalOnProperty("initialize.kie.container.standard.way")
	public KieContainer kieContainer() {
		return KieServices.Factory.get().getKieClasspathContainer();
	}
	
	 @Bean
	 @ConditionalOnProperty("initialize.kie.container.spring.boot.way")
     public KieContainer kieContainerBootWay() {
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
    
}
