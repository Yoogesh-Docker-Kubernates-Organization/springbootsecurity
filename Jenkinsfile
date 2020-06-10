pipeline {
   agent any

   environment {
     SERVICE_NAME = "springbootsecurity"
     REPOSITORY_TAG="${DOCKERHUB_USERNAME}/${SERVICE_NAME}:version_${BUILD_ID}"
   }

   stages {
      stage('Preparation') {
         steps {
            cleanWs()
            git credentialsId: 'GitHub', url: "https://github.com/${ORGANIZATION_NAME}/${SERVICE_NAME}"
         }
      }
      stage('Build') {
         steps {
            sh "mvn clean package"
         }
      }

      stage('Build and Push Image') {
         steps {
           sh 'docker image build -t ${REPOSITORY_TAG} ./src/main/resources/devops/docker/'
         }
      }
   }
}
