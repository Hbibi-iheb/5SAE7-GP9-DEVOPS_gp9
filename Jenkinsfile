pipeline {
    agent any
    tools {
        maven 'M2_HOME'
    }
    environment {
        NEXUS_URL = 'http://192.168.33.10:8081/repository/sahraoui_repository/' 
        NEXUS_CREDENTIALS_ID = 'nexus-credentials' 
        EMAIL_RECIPIENT = 'sahraoui.guesmi@gmail.com' 
    }
    stages {
        stage('GIT') {
            steps {
                git branch: 'Mohamed_Sahraoui_Guesmi_5sae7_GP9',
                    url: 'https://github.com/Hbibi-iheb/5SAE7-GP9-DEVOPS_gp9.git',
                    credentialsId: 'jenkins-example-github-pat'
            }
        }
    stage('Git Operations') {
            steps {
                script {
                    // Retrieve the Git token from Jenkins credentials
                    withCredentials([string(credentialsId: 'jenkins-example-github-pat', variable: 'GIT_TOKEN')]) {
                        // Clone the repository using the Git token for authentication
                        sh 'git clone https://${GIT_TOKEN}@github.com/Sahraoui44/5SAE7-GP9-DEVOPS_gp9.git -b Mohamed_Sahraoui_Guesmi_5sae7_GP9'
                        echo 'Repository successfully cloned using Git token'
                    }
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

        stage('Report coverage: Jacoco') {
            steps {
                script {
                    sh 'mvn jacoco:report'
                }
            }
            post {
                success {
                    jacoco execPattern: '**/target/jacoco.exec',
                           classPattern: '**/target/classes',
                           sourcePattern: '**/src/main/java',
                           inclusionPattern: '**/*.class',
                           exclusionPattern: '**/*Test*'
                }
            }
        }

        stage('Maven Build') {
            steps {
                script {
                    sh "mvn package"
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
                    sh """
                      mvn deploy -DskipTests -DaltDeploymentRepository=sahraoui_repository::default::http://admin:nexus@192.168.33.10:8081/repository/sahraoui_repository/

                    """
                }
            }
        }

        stage('Monitoring Services G/P') {
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
                        sh 'mvn clean package'
                        sh 'docker build -t sahraouiguesmi/mohamesahraouiguesmi-gp9-ski:1.0.0 .'
                    } catch (e) {
                        echo "Docker build failed: ${e}"
                        currentBuild.result = 'FAILURE'
                        error("Docker image build failed")
                    }
                }
            }
        }

        stage('Dockerhub') {
            steps {
                echo 'Push Image to Docker Hub: '
                sh 'docker login -u sahraouiguesmi -p dockerhub'
                sh 'docker push sahraouiguesmi/mohamesahraouiguesmi-gp9-ski:1.0.0'
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
        success {
            echo 'Build completed successfully.'
        }
        failure {
            echo 'Build failed.'
        }
    }
}
