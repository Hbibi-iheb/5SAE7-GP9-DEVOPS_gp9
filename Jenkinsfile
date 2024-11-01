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
        stage("Nexus") {
                          steps{
                                sh "mvn deploy"
                          }
         }
        stage("Docker Image") {
                  steps{
                        sh "docker build -t abdelwahebdhib-5sae7-g9-ski-management:1.0 ."
                  }
            }

            stage("Docker Hub") {
                    steps{
                          sh "docker login -u abdell333 -p dckr_pat_O1QWlvActCG_zNmgH3L45n-l2RQ"
                          sh "docker tag abdelwahebdhib-5sae7-g9-ski-management:1.0 abdell333/abdelwahebdhib-5sae7-g9-ski-management:1.0"
                          sh "docker push abdell333/abdelwahebdhib-5sae7-g9-ski-management:1.0"
                    }
            }

            stage("Docker Compose") {
                  steps{
                       // sh "docker compose up -d"
                  }
            }
    }
}
