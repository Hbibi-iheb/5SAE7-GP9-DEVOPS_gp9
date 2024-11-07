pipeline {
    agent any

    environment {
        SONAR_LOGIN = "admin"
        SONAR_PASSWORD = "Azerty12345@"
        EMAIL_RECIPIENT = "dhibabdeleheb@gmail.com"
    }

    tools {
        maven 'M2_HOME'
    }

    stages {
        stage('GIT') {
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

        stage('JUnit/Mockito') {
            steps {
                script {
                    sh "mvn clean install -DskipTests"
                    sh "mvn test"
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Scanner') {
            steps {
                sh "mvn sonar:sonar -Dsonar.login=${env.SONAR_LOGIN} -Dsonar.password=${env.SONAR_PASSWORD}"
            }
        }

        stage("Nexus") {
            steps {
                sh "mvn deploy"
            }
        }

        stage('Grafana/Prometheus') {
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
              

        stage("Docker Image") {
            steps {
                sh "docker build -t abdelwahebdhib-5sae7-g9-ski-management:1.0 ."
            }
        }

        stage("Docker Hub") {
            steps {
                    sh "docker login -u abdell333 -p dckr_pat_O1QWlvActCG_zNmgH3L45n-l2RQ"
                    sh "docker tag abdelwahebdhib-5sae7-g9-ski-management:1.0 abdell333/abdelwahebdhib-5sae7-g9-ski-management:1.0"
                    sh "docker push abdell333/abdelwahebdhib-5sae7-g9-ski-management:1.0"

            }
        }

        stage("Docker Compose") {
            steps {
                sh "docker compose up -d"
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
    }
}
