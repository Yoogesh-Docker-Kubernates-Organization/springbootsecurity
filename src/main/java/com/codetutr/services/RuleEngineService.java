package com.codetutr.services;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codetutr.entity.User;

@Service
public class RuleEngineService {

	@Autowired(required = false)
	private KieContainer kieContainer;

	public User fireUserRule(User user) {
		if (null == kieContainer) {
			user.setPassword(String.format(
					"Kie container is not instantiated and hence drools rule is not evaluated. please make %s in application.properties file before proceed. Also don't forget to comment out the %s in a %s file as you may not get the expected result if it is not commented out",
					"[initialize.kie.container.instance = true]", "[spring-boot-devtools]", "[pom.xml]"));
		} else {
			KieSession kieSession = kieContainer.newKieSession("rulesSession");
			kieSession.insert(user);
			kieSession.fireAllRules();
			kieSession.dispose();
		}
		return user;
	}
}
