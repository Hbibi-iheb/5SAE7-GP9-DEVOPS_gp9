pipeline {
    agent any
    tools {
        maven 'M2_HOME'
    }
    stages {
        stage('GIT') {
            steps {
                git branch: 'Mohamed_Sahraoui_Guesmi_5sae7',
                    url: 'https://github.com/Hbibi-iheb/5SAE7-GP9-DEVOPS_gp9.git',
                    credentialsId: 'jenkins-example-github-pat'
            }
        }
   
        stage('Build and Test') {
            steps {
                script {
                    sh "mvn clean install -X -DscriptTests=true"
                    sh "mvn test"
                }
            }
        }

        stage('Maven Build') {
            steps {
                script {
                    sh "mvn package -DscriptTests=true"
                }
            }
        }

        stage('SonarQube Scanner') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    script {
                        withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                            sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN} -X"
                        }
                    }
                }
            }
        }

        stage('Nexus') {
            steps {
                script {
                    sh 'mvn deploy'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    try {
                        sh 'mvn clean package -DscriptTests'
                        sh 'docker build -t sahraouiguessmi/ski-devops:1.0.0 .'
                    } catch (e) {
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
                        sh 'docker login -u sahraouiguessmi -p ${dockerhubpwd}'
                    }
                    sh 'docker push sahraoui44/ski-devops:1.0.0'
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh 'docker-compose up -d'
            }
        } 
    }
}
