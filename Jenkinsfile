pipeline {
    agent any
    tools {
        maven 'M2_HOME'
    }
    stages {
        stage('GIT') {
            steps {
                git branch: 'Mohamed_Sahraoui_Guesmi_5sae7',
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
                 
                    sh "mvn sonar:sonar -Dsonar.login=sqa_535f0f66aa5a63f4e06f2e361c98bf6fc42f1b3c"
                 }


                }
                
            }
        }
       stage('Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: NEXUS_CREDENTIALS_ID, usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh """
                        mvn deploy:deploy-file -Durl=${NEXUS_URL} -DrepositoryId=deploymentRepo \
                        -DgroupId=tn.esprit.spring -DartifactId=gestion-station-ski -Dversion=1.0-SNAPSHOT \
                        -Dpackaging=jar -Dfile=target/gestion-station-ski-1.0-SNAPSHOT.jar \
                        -DgeneratePom=true -Dusername=${NEXUS_USERNAME} -Dpassword=${NEXUS_PASSWORD}
                    """
                }
            }
        }
    }
}

              
          stage('Build Docker Image') {
            steps {
                script {
                    try{
                    sh 'mvn clean package -DscriptTests'
                    sh 'docker build -t sahraouiguessmi/ski-devops:1.0.0 .'
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
                    sh 'docker login -u sahraouiguesmi -p ${dockerhubpwd}'
                 }  
                 sh 'docker push sahraoui44/ski-devops:1.0.0'
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
