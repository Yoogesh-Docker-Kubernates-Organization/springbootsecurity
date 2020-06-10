FROM tomcat:9-jdk11-openjdk-slim 

MAINTAINER Yoogesh Sharma "yoogesh1983@gmail.com"

EXPOSE 8080

RUN rm -rf /usr/local/webapp/*  


COPY .target/springbootsecurity-1.0.war /usr/local/tomcat/webapps/springbootsecurity.war


CMD ["catalina.sh", "run"]