pipeline {
    agent any

    tools {
        maven 'M2_HOME'
    }

    environment {
        IMAGE_NAME = 'my-country-service'
        DOCKERHUB_REPO = 'nidhalsd/my-country-service'
    }

    stages {
        stage('Checkout code') {
            steps {
                git branch: 'main', url: 'https://github.com/nidhal212-collab/country-service.git'
            }
        }

        stage('Build maven') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Docker login') {
            steps {
                withCredentials([string(credentialsId: 'docker', variable: 'dockerPaswd')]) {
                    sh 'echo "$dockerPaswd" | docker login -u nidhalsd --password-stdin'
                }
            }
        }

        stage('Build Docker image') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} .'
            }
        }

        stage('Tag and Push image') {
            steps {
                sh 'docker tag ${IMAGE_NAME}:${BUILD_NUMBER} ${DOCKERHUB_REPO}:${BUILD_NUMBER}'
                sh 'docker push ${DOCKERHUB_REPO}:${BUILD_NUMBER}'
            }
        }

        stage('Deploy microservice') {
            steps {
                sh '''
                    docker rm -f country-service || true
                    docker pull ${DOCKERHUB_REPO}:${BUILD_NUMBER}
                    docker run -d -p 8082:8082 --name country-service ${DOCKERHUB_REPO}:${BUILD_NUMBER}
                '''
            }

        }
        stage('Deploy to kubernetes') {
            steps {
                script {
                    withKubeConfig(caCertificate: '', clusterName: '', contextName: '', credentialsId: 'kubeconfigFile', namespace: '', restrictKubeConfigAccess: false, serverUrl: '') {
                        sh 'kubectl apply -f deployment.yaml'
                        sh 'kubectl apply -f service.yaml'
                    }
                }
            }
    }
}