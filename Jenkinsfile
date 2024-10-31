pipeline {
    agent any

    environment {
                SONAR_LOGIN = "admin"
                SONAR_PASSWORD = "Azerty12345@"
          }
    tools {
        maven 'M2_HOME'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'Dhib_Abdelwaheb_5sae7_GP9',
                    url: 'https://github.com/Hbibi-iheb/5SAE7-GP9-DEVOPS_gp9.git'
            }
        }

        stage('Build') {
            steps {
                    sh "mvn package"
            }
        }

        stage('Test') {
                    steps {
                            sh "mvn test"
                    }
                }

        stage('SonarQube Scanner') {
            steps{
                sh "mvn sonar:sonar -Dsonar.login=${env.SONAR_LOGIN} -Dsonar.password=${env.SONAR_PASSWORD}"
            }
        }
    }
}
