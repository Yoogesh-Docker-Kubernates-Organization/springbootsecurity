pipeline {
   agent any

   environment {
     SERVICE_NAME = "springbootsecurity"
     REPOSITORY_TAG="${DOCKERHUB_USERNAME}/${SERVICE_NAME}:latest"
   }

   stages {

      stage('Git Clone') {
         steps {
            cleanWs()
            git credentialsId: 'GitHub', url: "https://github.com/${ORGANIZATION_NAME}/${SERVICE_NAME}"
         }
      }

      stage('Maven Clean Install') {
         steps {
            sh "mvn clean install"
         }
      }

      stage('Create Docker Image') {
         steps {
           sh 'docker image build -t ${REPOSITORY_TAG} .'
         }
      }

      stage('Publish Docker Image') {
         steps {
            withCredentials([string(credentialsId: 'DOCKER_PASSEWORD', variable: 'DOCKER_HUB_CREDENTIALS')]) {
               sh "docker login -u yoogesh1983 -p ${DOCKER_HUB_CREDENTIALS}"
            }
            sh 'docker push ${REPOSITORY_TAG}'
         }
      }

      stage('Deploy to Cluster') {
         steps {
            sh 'envsubst < ${WORKSPACE}/deploy.yaml | kubectl apply -f -'
         }
      }
   }
}
