pipeline {
    agent any
    tools {
        maven 'M2_HOME'
    }
    stages {
        stage('GIT') {
            steps {
                git branch: 'Hbibi_iheb_5sae7_GP9',
                    url: 'https://github.com/Hbibi-iheb/5SAE7-GP9-DEVOPS_gp9.git',
                    credentialsId: 'jenkins-example-github-pat'
            }
        }
   
       stage('build and test ')
        { steps{
            script{
            sh " mvn clean install -X -DscriptTests=true"
            sh " mvn test"}
        }
        }
    stage('maven build') {
steps {
    script {
        sh "mvn package -DscriptTests=true"
    }
}
    }
    stage('SonarQube Scanner') {
            steps {
                
                    withSonarQubeEnv('sonarqube') {
                 script{
                 
                    sh "mvn sonar:sonar"
                 }


                }
                
            }
        }
        stage('nexus'){
            steps {
                script {
                  sh ' mvn deploy  '
                }
            }
        }
              
          stage('Build Docker Image') {
            steps {
                script {
                    try{
                    sh 'mvn clean package -DscriptTests'
                    sh 'docker build -t iheb141/ski-devops:1.0.0 .'
                    } catch(e){
                     echo "Docker build failed: ${e}"
                        currentBuild.result = 'FAILURE' 
                        error("Docker image build failed")
                    }
                }
            
        }
         }
               stage('Deploy Docker Image') {
            steps {
             
                script {
                 withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                    sh 'docker login -u iheb141 -p ${dockerhubpwd}'
                 }  
                 sh 'docker push iheb141/ski-devops:1.0.0'
                }
            }
        }
         stage('Deploy with Docker Compose') {
            steps {
                sh 'docker-compose up -d'
            }
        } 
        
    }
}
