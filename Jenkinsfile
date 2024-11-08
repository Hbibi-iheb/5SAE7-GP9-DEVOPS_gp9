pipeline {
    agent any
    tools {
        maven 'M2_HOME'
    }
    environment {
        NEXUS_URL = 'http://192.168.33.10:8081/repository/Gabsiwael_repository/' 
        NEXUS_CREDENTIALS_ID = 'nexus-credentials' 
        EMAIL_RECIPIENT = 'wael.gabsi@esprit.tn' 
         SONAR_LOGIN = "admin"
        SONAR_PASSWORD = "Admin123@Admin123@"
    }
    stages {
        stage('GIT') {
            steps {
                git branch: 'GABSI_WAEL_5sae7_GP9',
                    url: 'https://github.com/Hbibi-iheb/5SAE7-GP9-DEVOPS_gp9.git'
                     
                   
            }
        }
  stage('Maven Build') {
            steps {
                script {
                    sh "mvn package "
                }
            }
        }
    

        /*stage('JUnit/Mockito') {
            steps {
                script {
                    sh "mvn clean install -DskipTests"
                    sh "mvn test"
                }
            }
        }
           stage('SonarQube Scanner') {
            steps {
                sh "mvn sonar:sonar -Dsonar.login=${env.SONAR_LOGIN} -Dsonar.password=${env.SONAR_PASSWORD}"
            }
        }
  stage('Nexus Deployment') {
            steps {
                script {
                    sh """
                      mvn deploy -DskipTests -DaltDeploymentRepository=sahraoui_repository::default::http://admin:nexus@192.168.33.10:8081/repository/wael_repository/

                    """
                }
            }
        } */

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
                        sh 'docker build -t waelgabsi/waelgabsi-gp9-ski:1.0.0 .'
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
                sh 'docker login -u waelgabsi -p dockerhub'
                sh 'docker push waelgabsi/waelgabsi-gp9-ski:1.0.0'
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh 'docker-compose up -d'
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
