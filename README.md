# What is the Application about? </br>
- This is a Spring-Boot application that uses Spring-Security</br>
- This application can be run from a <b>commandLine</b> or from <b>eclipse</b> using embedded server at Port <b>8888</b> and can also be run at external webServer like <b>tomcat</b> at port <b>8080</b> </br>
- This project is created on a java version 11, so the version of java to run this project must be java 11 or more

# How to run the Application? </br>
Do the following once the compile is done and war is created inside a target folder:</br>

<b>When running from a command line:</b> </br>
> set path=C:\Program Files\Java\jdk-13.0.1\bin </br>
> java -jar -DenvTarget=local springbootsecurity.war </br>

The application can now be browsed at http://localhost:8888/springbootsecurity/dispatcher/ </br>

<b>When running from eclipse with embedded tomcat:</b> </br>
> Right click to the <b>DispatcherServletInitializer.java</b></br>
> Run As -> Java Application</br>

The application can now be browsed at http://localhost:8888/springbootsecurity/dispatcher/ </br>

<b>When deploying to the external tomcat:</b> </br>
> copy the <b>SpringBootSecurity.war</b> from a target folder and put inside the <b>webapp</b> folder of tomcat server</br>
> Start the server by double clicking on a <b>startup.bat</b> inside a <b>bin</b> folder of tomcat</br>

<b>Do the following changes for log if you are running MAC OS: </b> </br>
> In logback-spring.xml, comment out the Windows specific line and include below snippet:

	<!-- Use this property for Mac OS -->
	<property name="FILE_LOCATION" value="/usr/local/opt/logs/springbootsecurity" /> 
	
    
> In log4j.local, Give Mac path according to your requirement: </br>

	#Modify the [appName] with your WAS application name and un-comment the line below.
	log4j.appender.file.File=\\usr\\local\\opt\\logs\\SpringBootSecurity_local.log
	
> In log4j.dev, Give Mac path according to your requirement: </br>

	#Modify the [appName] with your WAS application name and un-comment the line below.
	log4j.appender.file.File=\\usr\\local\\opt\\logs\\SpringBootSecurity_dev.log
	
> In log4j.prod, Give Mac path according to your requirement: </br>

	#Modify the [appName] with your WAS application name and un-comment the line below.
	log4j.appender.file.File=\\usr\\local\\opt\\logs\\SpringBootSecurity_prod.log
	
<b>If you are running on a Kubernetes Cluster and want to enable ConfigMap: </b> </br>
> In pom.xml, include below dependency which is currently commented out:

	<dependency>
		  <groupId>org.springframework.cloud</groupId>
		   <artifactId>spring-cloud-starter-kubernetes-config</artifactId>
		   <version>${spring.cloud.kubernetes.version}</version>
	</dependency>

The application can now be browsed at http://localhost:8080/springbootsecurity/ </br>
Remember you are not providing <b>/dispatcher</b> here</br>

# How to build and run application from a docker container? </br>
Do the following changes to the pom.xml:

> Enable below line which is disabled currently:

	<executions>
		<execution>
			<phase>package</phase>
			<goals>
				<goal>build</goal>
			</goals>
		</execution>
		<execution>
			<id>mydeploy</id>
			<phase>deploy</phase>
			<goals>
				<goal>push</goal>
			</goals>
		</execution>
	</executions> 

> Also Give your username/password of docker account below at pom.xml:

	<authConfig>
		<username>YOUR_DOCKER_USER_NAME</username>
		<password>YOUR_DOCKER_PASSWORD</password>
	</authConfig>

> If running on a kubernates cluster, enable below line which is disabled currently and disabled the line which is enabled currently (in persistence-dev.properties file):

	#jdbc.url=jdbc:mysql://localhost:3306/myDatabase
	jdbc.url=jdbc:mysql://yoogesh-database/myDatabase
	
Also make sure the username and password is aligned with <b>mySql.yaml</b>:

	jdbc.username=root
	jdbc.password=password
	
We need to create and deploy the code at EC2 instance at <a href="https://aws.amazon.com/">Amazon</a> by doing following:

> Create an ubuntu EC2 instance </br>
> Install Java, Git, Maven and Docker on it </br>

Move <strong>springbootsecurity</strong> project inside /home/ubuntu/ directory of the EC2 instance from your computer Using <b>winscp</b> or clone from git below : </br>

>ubuntu@ip-172-31-35-23:~$ git clone https://github.com/yoogesh1983/Springboot-Docker-Based-Projects.git

Go up to the pom.xml folder of <strong>springbootsecurity</strong> and run below commad to deploy image into a docker hub: </br>

> ubuntu@ip-172-31-35-23:~/Springboot-Docker-Based-Projects/springbootsecurity$ sudo mvn clean deploy  

Similarly, the docker image can be run using below command by going up to the <b>ubuntu</b> folder:

> ubuntu@ip-172-31-35-23:~$ sudo docker container run -p 80:8080 -it yoogesh1983/springbootsecurity </br>

The application can now be available at http://10.15.0.15/springbootsecurity/ assuming ip of the container is 10.15.0.15</br>
