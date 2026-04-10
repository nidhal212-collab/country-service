pipeline {
    agent any
    tools {
        maven 'M2_HOME'
    }
    stages {
        stage('Checkout code') {
            steps {
                git branch: 'master', url: 'https://github.com/nidhal212-collab/country-service.git'
            }
        }
        
        stage('Build maven') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Build Dockerfile ') {
            steps {
                sh 'docker build . -t my-country-service:$BUILD_NUMBER '
               withCredentials([string(credentialsId: 'dockerPaswd', variable: 'dockerhuBpwd')]) {
                   sh 'docker login -u nidhalsd -p ${dockerhuBpwd}'
               }
                sh 'docker tag my-country-service:$BUILD_NUMBER nidhalsd/my-country-service:$BUILD_NUMBER'
                sh 'docker push nidhalsd/my-country-service:$BUILD_NUMBER'
            }
        }

        stage('Deploy microservice') {
            steps {
                sh '''
                    docker rm -f country-service || true
                    docker run -d -p 8082:8082 --name country-service my-country-service:$BUILD_NUMBER
                '''
            }
        }
    }
}