pipeline {
    agent any
    tools {
        maven 'M2_HOME'
    }
    environment {
        NEXUS_URL = 'http://192.168.33.10:8081/repository/sahraoui_repository/'
        NEXUS_CREDENTIALS_ID = 'nexus-credentials'
    }
    stages {
        stage('Debug Environment') {
            steps {
                script {
                    echo "M2_HOME is set to: ${env.M2_HOME}"
                    echo "NEXUS_URL is set to: ${env.NEXUS_URL}"
                }
            }
        }
        
        // Uncomment other stages one by one to identify issues
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
                        sh "mvn sonar:sonar -Dsonar.login=sqa_535f0f66aa5a63f4e06f2e361c98bf6fc42f1b3c"
                    }
                }
            }
        }

       stage('Nexus Deployment') {
    steps {
        script {
            withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                // Make sure to check if variables are being set correctly
                echo "Using Nexus User: ${NEXUS_USER}"
                sh "mvn clean deploy -DskipTests --settings /path/to/settings.xml -Dusername=${NEXUS_USER} -Dpassword=${NEXUS_PASS}"
            }
        }
    }
}





        stage('Build Docker Image') {
            steps {
                script {
                    try {
                        sh 'mvn clean package -DscriptTests=true'
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
                withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                    sh 'docker login -u sahraouiguesmi -p ${dockerhubpwd}'
                }
                sh 'docker push sahraouiguessmi/ski-devops:1.0.0'
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh 'docker-compose up -d'
            }
        }
    }
}
