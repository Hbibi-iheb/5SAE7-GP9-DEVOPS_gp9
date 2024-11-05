pipeline {
    agent any
    tools {
        maven 'M2_HOME' // Ensure this matches your Jenkins configuration
    }
    environment {
        NEXUS_URL = 'http://192.168.33.10:8081/repository/sahraoui_repository/' // Nexus repository URL
        NEXUS_CREDENTIALS_ID = 'nexus-credentials' // Update with your actual Jenkins credentials ID for Nexus
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

        stage('Mockito Test ') {
            steps {
                sh 'mvn test'
            }
        }
    }
    post {
        always {
            junit '**/target/surefire-reports/*.xml'
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
            // Using double quotes for the shell command
            sh """
                mvn deploy -DskipTests -DaltDeploymentRepository=sahraoui_repository::default::http://admin:nexus@192.168.33.10:8081/repository/sahraoui_repository/
            """
        }
    }
}

        stage('Monitoring Services G/P') {
    steps {
        script {
            // Check and start Prometheus container if not already running
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

            // Check and start Grafana container if not already running
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
                        sh 'mvn clean package -DscriptTests=true'
                        sh 'docker build -t sahraouiguesmi/ski-devops:1.0.0 .'
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
                echo 'Push Image to dockerhub : ';
                sh 'docker login -u sahraouiguesmi -p dockerhub';
                sh 'docker push sahraouiguesmi/ski-devops:1.0.0';
            }
        }

     stage('Deploy with Docker Compose') {
            steps {
                sh 'docker-compose up -d'
            }
        } 
        
    }
}


        
