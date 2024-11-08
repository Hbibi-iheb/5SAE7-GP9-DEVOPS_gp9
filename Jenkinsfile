pipeline {
    agent any
    tools {
        maven 'M2_HOME'
    } 
    environment {
        EMAIL_RECIPIENT = 'iheb.hbibi01@gmail.com' 
    }
    stages {
        stage('GIT') {
            steps {
                git branch: 'Hbibi_iheb_5sae7_GP9',
                    url: 'https://github.com/Hbibi-iheb/5SAE7-GP9-DEVOPS_gp9.git'
                    credentialsId: 'jenkins-example-github-pat'
            }
        }

        stage('build and test') {
            steps {
                script {
                    sh "mvn clean install -X -DscriptTests=true"
                    sh "mvn test"
                }
            }
        }

        stage('maven build') {
            steps {
                script {
                    sh "mvn package -DscriptTests=true"
                }
            }
        }

        stage('JUnit/Mockito') {
            steps {
                script {
                    sh "mvn clean install -DskipTests"
                    sh "mvn test"
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Scanner') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    script {
                        sh "mvn sonar:sonar"
                    }
                }
            }
        }

        stage('nexus') {
            steps {
                script {
                    sh 'mvn deploy'
                }
            }
        }

        stage('Grafana/Prometheus') {
            steps {
                script {
                    sh '''
                    if [ "$(docker ps -q -f name=prometheus)" ]; then
                        echo "Prometheus is already running."
                    else
                        if [ "$(docker ps -a -q -f name=prometheus)" ]; then
                            docker start prometheus
                            echo "Started existing Prometheus container."
                        else
                            docker run -d --name prometheus prom/prometheus
                            echo "Started a new Prometheus container."
                        fi
                    fi
                    '''
                    sh '''
                    if [ "$(docker ps -q -f name=grafana)" ]; then
                        echo "Grafana is already running."
                    else
                        if [ "$(docker ps -a -q -f name=grafana)" ]; then
                            docker start grafana
                            echo "Started existing Grafana container."
                        else
                            docker run -d --name grafana grafana/grafana
                            echo "Started a new Grafana container."
                        fi
                    fi
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    try {
                        sh 'mvn clean package -DscriptTests'
                        sh 'docker build -t iheb141/ski-devops:1.0.0 .'
                    } catch(e) {
                        echo "Docker build failed: ${e}"
                        currentBuild.result = 'FAILURE'
                        error("Docker image build failed")
                    }
                }
            }
        }

        stage('Deploy Docker Image') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                        sh 'docker login -u iheb141 -p ${dockerhubpwd}'
                    }
                    sh 'docker push iheb141/ski-devops:1.0.0'
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh 'docker-compose up -d'
            }
        }

        stage('Email') {
            steps {
                script {
                    def subject = "Build ${currentBuild.currentResult}: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
                    def body = "Le build ${currentBuild.currentResult} pour le projet ${env.JOB_NAME} a été exécuté. Consultez les détails sur Jenkins : ${env.BUILD_URL}."
                    mail to: "${EMAIL_RECIPIENT}", subject: subject, body: body
                }
            }
        }
   }
    
    post {
        always {
            junit '**/target/surefire-reports/*.xml'
        }
    }
}
