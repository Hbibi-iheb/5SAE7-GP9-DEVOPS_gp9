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
        script {
            // Remove conflicting containers if they already exist
            sh '''
            if [ "$(docker ps -aq -f name=docker-compose-mysql-db)" ]; then
                docker rm -f docker-compose-mysql-db || true
            fi
            if [ "$(docker ps -aq -f name=docker-compose-spring-boot)" ]; then
                docker rm -f docker-compose-spring-boot || true
            fi

            # Start the containers
            docker-compose up -d
            '''
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
                docker start prometheus
                echo "Started Prometheus."
            fi
            '''


            sh '''
            if [ "$(docker ps -q -f name=grafana)" ]; then
                echo "Grafana is already running."
            else
                docker start grafana
                echo "Started Grafana."
            fi
            '''
        }
    }
}
