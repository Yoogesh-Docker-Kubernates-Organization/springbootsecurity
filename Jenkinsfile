pipeline {
	agent any
	
	environment {
		SERVICE_NAME = "springbootsecurity"
		YAML_PATH = "src/main/resources/devops/k8s_aws"
		REPOSITORY_TAG="${DOCKERHUB_USERNAME}/${SERVICE_NAME}:latest"
	}

	stages {
	
		stage('Git Clone') {
			steps {
					script {
					    properties([
					    	parameters([
					    		booleanParam(defaultValue: true, description: 'Build Spring Boot Security', name: 'enableSpringBootSecurity'), 
					    		booleanParam(defaultValue: true, description: 'Initiate Database', name: 'enableDatabase'), 
					    		booleanParam(defaultValue: true, description: 'Deploy Istio feature', name: 'enableIstio'), 
					    		booleanParam(defaultValue: true, description: 'Deploy kibana feature', name: 'enableKibana'), 

					    		booleanParam(defaultValue: true, description: 'Build API Gateway', name: 'enableAPIGateway'), 
					    		booleanParam(defaultValue: true, description: 'Build MFE', name: 'enableReactMFE')
					    	])
					    ])
					   	echo "Build Springbootsecurity: ${params.enableSpringBootSecurity}"
			 			echo "Build API Gatway: ${params.enableAPIGateway}"
			 			echo "Build MFE: ${params.enableReactMFE}"
						cleanWs()
						if (params.enableSpringBootSecurity == 'true') {
							git credentialsId: 'GitHub', url: "https://github.com/${ORGANIZATION_NAME}/${SERVICE_NAME}"
						}
        			}
				}
		}
		
		stage('Maven Clean Install') {
			steps {
				script{
				    if (params.enableSpringBootSecurity == 'true') {
						sh "mvn clean install"
				    }
				}
			}
		}
			
		stage('Create Docker Image') {
			steps {
				script{
				    if (params.enableSpringBootSecurity == 'true') {
						sh 'docker image build -t ${REPOSITORY_TAG} .'
				    }
				}
			}
		}
			
		stage('Publish Docker Image') {
			steps {
					script {
				    	if (params.enableSpringBootSecurity == 'true') {
				    		withCredentials([string(credentialsId: 'DOCKER_PASSEWORD', variable: 'DOCKER_HUB_CREDENTIALS')]) {
								sh "docker login -u yoogesh1983 -p ${DOCKER_HUB_CREDENTIALS}"
							}
            				sh 'docker push ${REPOSITORY_TAG}'
				    	}
					}

            	}
		}
		
		
		stage('Deploy to Cluster') {

			environment {
				// Traditional kubernetes ingress gateway related variables
                enableKubernetesIngress = "false"
				manuallyInstallGrafanaPrometheus = "false"

				//canery deployment related variables.enableIstioCanery must be set to true before you want to set the other canery experiments as true to work!!
				enableIstioCanery = "false"
				enableCaneryWithStickey = "false"
				enableCaneryWithLoadBalancer = "false";
				enableFaultInjection = "false";

				//Circuit breaking feature
				enableCircuitBreaker = "false";
            }

			steps {

                   /* Installing traditional kubernetes ingress */
				   script {
					   if (env.enableKubernetesIngress == 'true') {
						   sh 'kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/static/provider/aws/deploy.yaml'
						   sh "kubectl create secret generic yoogeshcredential --from-file ${YAML_PATH}/auth/auth -n kube-system" 
					   }
				   }

					/* ConfigMap configuration */
					script {
					   if (params.enableSpringBootSecurity == 'true') {
						   sh "kubectl apply -f ${YAML_PATH}/configmap/configMap.yaml"
						   sh "kubectl apply -f ${YAML_PATH}/rbac/service-account-for-fabric8-access.yaml"
					   }
					}
					
					/* Istio Configuration */
					script {
					   if (params.enableIstio == 'true') {
						   sh "istioctl manifest apply --set profile=demo"
						   sh "kubectl label namespace default istio-injection=enabled --overwrite"
						   sh "kubectl apply -f ${YAML_PATH}/istio/gateway/istio-firewall.yaml"
					   }
					}			

					/* Database configuration */
					script {
					   if (params.enableDatabase == 'true') {
						   sh "kubectl apply -f ${YAML_PATH}/pvc/storage.yaml"
						   sh "kubectl apply -f ${YAML_PATH}/mysql/mysql.yaml"

						   echo 'Sleeping for 60 second before starting webApp....'
						   sleep(time:60,unit:"SECONDS")
					   }
					}

					/* Webapp configuration */
					script {
					   if (params.enableSpringBootSecurity == 'true') {
						   sh "kubectl apply -f ${YAML_PATH}/webapp/webApp.yaml"
					   }
					}

					/* Kibana configuration */
					script {
					   if (params.enableKibana == 'true') {
						   sh "kubectl apply -f ${YAML_PATH}/kibana/fluentd-config.yaml"
						   sh "kubectl apply -f ${YAML_PATH}/kibana/elastic-stack.yaml"
					   }
					}

					/* Istio Ingress-Gateway configuration */
					script {
						if(env.enableIstioCanery == 'true')
						{
							sh "kubectl apply -f ${YAML_PATH}/istio/canery/webapp.yaml"

							if(env.enableCaneryWithStickey == 'true')
							{
								/* Kubernetes stickey pod deployment */
								sh "kubectl apply -f ${YAML_PATH}/istio/canery/webapp-stickey.yaml"
							}
							else 
							{
								sh "kubectl apply -f ${YAML_PATH}/istio/canery/destinationRule.yaml"

								if(env.enableCaneryWithLoadBalancer == 'true')
								{
									/* if you want to experiment canery with 10% traffic */
									sh "kubectl apply -f ${YAML_PATH}/istio/canery/vs_canery_loadbalancer.yaml" 
								}
								else if(env.enableFaultInjection == 'true')
								{
									/* Applying Fault Injection at the riskey version */
									sh "kubectl apply -f ${YAML_PATH}/istio/canery/vs_fault_injection.yaml" 
								}
								else 
								{
									sh "kubectl apply -f ${YAML_PATH}/istio/canery/vs_canery_headerParam.yaml" 
								}
							}
						}
						else 
						{
							if (params.enableIstio == 'true') {
								sh "kubectl apply -f ${YAML_PATH}/istio/gateway/istio-route-webapp.yaml"
							}
						}
						if (params.enableIstio == 'true') {
							sh "kubectl apply -f ${YAML_PATH}/istio/gateway/istio-route-monitoring.yaml"
						}

						/* If you want to enable Circuit breaker feature on twm-webapp */
						if(env.enableCircuitBreaker == 'true'){
							sh "kubectl apply -f ${YAML_PATH}/istio/circuitBreaking/istio_circuit_breaking.yaml"
						}
					}

					/* Traditional Kubernetes Ingress routing configuration */
					script {
						if(env.enableKubernetesIngress == 'true')
						{
							sh "kubectl apply -f ${YAML_PATH}/webapp/ingress_webapp.yaml"
							sh "kubectl apply -f ${YAML_PATH}/kibana/ingress_kibana.yaml"
							if(env.manuallyInstallGrafanaPrometheus == 'true')
							{
								echo 'You have choosen to install Grafana and Prometheus feature manually. Make sure Grafana and Prometheus are already installed in a cluster using Helm package manager....'
								sh "kubectl apply -f ${YAML_PATH}/prometheus/ingress_prometheus_grafana.yaml"
							}
							else 
							{
								sh "kubectl apply -f ${YAML_PATH}/istio/ingress/kubernetes_ingress.yaml"
							}
						}
					}
			}
		}

		stage('Trigger Api Gateway') {
			steps {
				script {
					if(params.enableAPIGateway == 'true'){
						build job: '../../API-Gateway/master', wait: true
					}
				}
			}
		}

		stage('Trigger React MFE') {
			steps {
				script {
					if(params.enableReactMFE == 'true'){
						build job: '../reactmfe/master', wait: true
					}
				}
			}
		}

	}
}					