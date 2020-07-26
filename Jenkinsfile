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

			environment {
                enableKubernetesIngress = "false"
				enableIstioCanery = "false"
				enableKubernetesStickey = "true"
				enableGrafanaAnddPrometheusAtIstio = "true"
				grafanaPrometheusInstalledManually = "false"
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
					sh "kubectl apply -f ${YAML_PATH}/configmap/configMap.yaml"
					sh "kubectl apply -f ${YAML_PATH}/rbac/service-account-for-fabric8-access.yaml"
					
					/* Istio Configuration */
					sh "istioctl manifest apply --set profile=demo"
					sh "kubectl label namespace default istio-injection=enabled --overwrite"
					sh "kubectl apply -f ${YAML_PATH}/istio/gateway/istio-firewall.yaml"			

					/* Database configuration */
					sh "kubectl apply -f ${YAML_PATH}/pvc/storage.yaml"
					sh "kubectl apply -f ${YAML_PATH}/mysql/mysql.yaml"

					echo 'Sleeping for 60 second before starting webApp....'
					sleep(time:60,unit:"SECONDS")
					
					/* Webapp configuration */
					sh "kubectl apply -f ${YAML_PATH}/webapp/webApp.yaml"
					script {
						if(env.enableIstioCanery == 'true' || env.enableKubernetesStickey == 'true'){
							sh "kubectl apply -f ${YAML_PATH}/istio/canery/webapp.yaml"
						}
					}

					/* Kibana configuration */
					sh "kubectl apply -f ${YAML_PATH}/kibana/fluentd-config.yaml"
					sh "kubectl apply -f ${YAML_PATH}/kibana/elastic-stack.yaml"

					/* Istio Ingress-Gateway configuration*/
					script {
						if(env.enableIstioCanery == 'true'){
							/* Canery Deployment (if you want to experiment canery with 10% traffic) */
							sh "kubectl apply -f ${YAML_PATH}/istio/canery/destinationRule.yaml"
							sh "kubectl apply -f ${YAML_PATH}/istio/canery/vs_loadbalancer.yaml" 
						}
						else if(env.enableKubernetesStickey == 'true'){
							/* Kubernetes stickey pod deployment */
							sh "kubectl apply -f ${YAML_PATH}/istio/stickey/webapp-stickey.yaml"
						}
						else {
							sh "kubectl apply -f ${YAML_PATH}/istio/gateway/istio-route-webapp.yaml"
						}
					}

					/* Traditional Kubernetes Ingress routing configuration */
					script {
						if(env.enableKubernetesIngress == 'true'){
							sh "kubectl apply -f ${YAML_PATH}/webapp/ingress_webapp.yaml"
							sh "kubectl apply -f ${YAML_PATH}/kibana/ingress_kibana.yaml"
							if(env.enableGrafanaAnddPrometheusAtIstio == 'true'){
								sh "kubectl apply -f ${YAML_PATH}/istio/ingress/kubernetes_ingress.yaml"
							}
							else if(env.grafanaPrometheusInstalledManually == 'true'){
								/* If you need Grafana and Premetheus feature without using Istio the use this. But before this make sure Grafana and Prometheus installed manually using Helm */
								sh "kubectl apply -f ${YAML_PATH}/prometheus/ingress_prometheus_grafana.yaml"
							}
						}
					}
			}
		}
	}
}
						
					
						
			
		
		
	

