pipeline {
    agent any

    tools {
        maven 'M2_HOME' // Ensure M2_HOME is correctly set in Jenkins
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
                            sh "mvn package"
                    }
                }

        stage('SonarQube Scanner') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    script {
                        // Perform SonarQube analysis
                        sh "mvn sonar:sonar"
                    }
                }
            }
        }

        stage('Nexus Deployment') {
            steps {
                script {
                    // Deploy the artifact to Nexus
                    sh 'mvn deploy'
                }
            }
        }
    }
}
