package com.codetutr.services;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codetutr.entity.User;

@Service
public class RuleEngineService {
	
	private final Logger logger = LoggerFactory.getLogger(RuleEngineService.class);

	@Autowired(required=false)
    private KieContainer kieContainer;
	
    public User fireUserRule(User user) {
    	if(null == kieContainer) {
    		logger.error("Kie container is not instantiated and hence drools rule is not evaluated. please make {} in application.properties file before proceed. Also don't forget to comment out the {} in a {} file", "[initialize.kie.container.instance = true]","[spring-boot-devtools]","[pom.xml]");
    	}else {
    		KieSession kieSession = kieContainer.newKieSession("rulesSession");
    		kieSession.insert(user);
    		kieSession.fireAllRules();
    		kieSession.dispose();
    	}
    	 return user;
    }
}
