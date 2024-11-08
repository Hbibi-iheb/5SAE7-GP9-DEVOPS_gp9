pipeline {
    agent any
    tools {
        maven 'M2_HOME'
    }
    environment {
        NEXUS_URL = 'http://192.168.33.10:8081/repository/Gabsiwael_repository/' 
        NEXUS_CREDENTIALS_ID = 'nexus-credentials' 
        EMAIL_RECIPIENT = 'wael.gabsi@esprit.tn' 
        SONAR_LOGIN = credentials('sonar-login')  // Replace with Jenkins credentials ID
        SONAR_PASSWORD = credentials('sonar-password') // Replace with Jenkins credentials ID
        DOCKER_HUB_USERNAME = credentials('dockerhub-username') // Replace with Jenkins credentials ID
        DOCKER_HUB_PASSWORD = credentials('dockerhub-password') // Replace with Jenkins credentials ID
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
                sh "mvn clean package -DskipTests"
            }
        }
        stage('JUnit/Mockito') {
            steps {
                sh "mvn test"
            }
        }
        stage('SonarQube Scanner') {
            steps {
                sh "mvn sonar:sonar -Dsonar.login=${SONAR_LOGIN} -Dsonar.password=${SONAR_PASSWORD}"
            }
        }
        stage("Nexus") {
            steps {
                sh "mvn deploy -DskipTests -DaltDeploymentRepository=Gabsiwael_repository::default::${NEXUS_URL}"
            }
        }
        stage('Report coverage: Jacoco') {
            steps {
                sh 'mvn jacoco:report'
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
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'docker login -u $DOCKER_USER -p $DOCKER_PASS'
                    sh 'docker push waelgabsi/waelgabsi-gp9-ski:1.0.0'
                }
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
