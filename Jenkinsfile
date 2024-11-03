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
                    // Perform clean install with tests and log more detail
                    sh "mvn clean install -X -DscriptTests=true"
                }
            }
        }

        stage('Maven Package') {
            steps {
                script {
                    // Package the application, skipping tests if already run
                    sh "mvn package -DscriptTests=true"
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    script {
                        // Run SonarQube analysis
                        sh "mvn sonar:sonar"
                    }
                }
            }
        }

        stage('Nexus Deployment') {
            steps {
                script {
                    // Deploy to Nexus repository
                    sh "mvn deploy"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    try {
                        // Build Docker image
                        sh "docker build -t sahraouiguessmi/ski-devops:1.0.0 ."
                    } catch (Exception e) {
                        echo "Docker build failed: ${e}"
                        currentBuild.result = 'FAILURE' 
                        error("Docker image build failed")
                    }
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                        // Login to Docker Hub
                        sh "docker login -u sahraouiguesmi -p ${dockerhubpwd}"
                    }
                    // Push Docker image
                    sh "docker push sahraouiguessmi/ski-devops:1.0.0"
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                // Deploy services using Docker Compose
                sh "docker-compose up -d"
            }
        }
    }
}
