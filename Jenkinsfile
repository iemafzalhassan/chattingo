pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'ergaurav3155' // Apna Docker Hub username
    }
    
    stages {
        stage('Git Clone') {
            steps {
                git branch: 'main', url: 'https://github.com/ergaurav3155/chattingo.git'
            }
        }

        stage('Image Build') {
            steps {
                script {
                    echo "Building backend image..."
                    dir('backend') {
                        sh "docker build -t ${env.DOCKERHUB_USERNAME}/chattingo-backend:${env.BUILD_NUMBER} ."
                        sh "docker build -t ${env.DOCKERHUB_USERNAME}/chattingo-backend:latest ."
                    }

                    echo "Building frontend image..."
                    dir('frontend') {
                        sh "docker build -t ${env.DOCKERHUB_USERNAME}/chattingo-frontend:${env.BUILD_NUMBER} ."
                        sh "docker build -t ${env.DOCKERHUB_USERNAME}/chattingo-frontend:latest ."
                    }
                }
            }
        }

        stage('Filesystem Scan') {
            steps {
                echo "Yeh stage abhi baaki hai."
            }
        }

        stage('Image Scan') {
            steps {
                echo "Yeh stage abhi baaki hai."
            }
        }

        stage('Push to Registry') {
            steps {
                script {
                    // Docker Hub par securely login karein credentials plugin ka upyog karke
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        echo "Logging in to Docker Hub..."
                        sh "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin"
                        
                        echo "Pushing backend image..."
                        sh "docker push ${env.DOCKERHUB_USERNAME}/chattingo-backend:${env.BUILD_NUMBER}"
                        sh "docker push ${env.DOCKERHUB_USERNAME}/chattingo-backend:latest"

                        echo "Pushing frontend image..."
                        sh "docker push ${env.DOCKERHUB_USERNAME}/chattingo-frontend:${env.BUILD_NUMBER}"
                        sh "docker push ${env.DOCKERHUB_USERNAME}/chattingo-frontend:latest"
                        
                        echo "Logout from Docker Hub (optional but good practice)"
                        sh "docker logout"
                    }
                }
            }
        }

        stage('Update Compose') {
            steps {
                echo "Yeh stage abhi baaki hai."
            }
        }

        stage('Deploy') {
            steps {
                echo "Yeh stage abhi baaki hai."
            }
        }
    }
}
