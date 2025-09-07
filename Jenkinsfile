pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'ergaurav3155' // Docker Hub username
        IMAGE_BACKEND = "chattingo-backend"
        IMAGE_FRONTEND = "chattingo-frontend"
    }

    stages {

        stage('Git Clone') {
            steps {
                echo "Cloning repo..."
                git branch: 'main', url: 'https://github.com/ergaurav3155/chattingo.git'
            }
        }

        stage('Clean Old Images') {
            steps {
                echo "Removing old local Docker images if exist..."
                sh """
                    docker rmi -f ${DOCKERHUB_USERNAME}/${IMAGE_BACKEND}:latest || true
                    docker rmi -f ${DOCKERHUB_USERNAME}/${IMAGE_BACKEND}:${BUILD_NUMBER} || true
                    docker rmi -f ${DOCKERHUB_USERNAME}/${IMAGE_FRONTEND}:latest || true
                    docker rmi -f ${DOCKERHUB_USERNAME}/${IMAGE_FRONTEND}:${BUILD_NUMBER} || true
                """
            }
        }

        stage('Build Images') {
            steps {
                script {
                    echo "Building backend image..."
                    sh "docker build -t ${DOCKERHUB_USERNAME}/${IMAGE_BACKEND}:${BUILD_NUMBER} -t ${DOCKERHUB_USERNAME}/${IMAGE_BACKEND}:latest ./backend"

                    echo "Building frontend image..."
                    sh "docker build -t ${DOCKERHUB_USERNAME}/${IMAGE_FRONTEND}:${BUILD_NUMBER} -t ${DOCKERHUB_USERNAME}/${IMAGE_FRONTEND}:latest ./frontend"
                }
            }
        }

        stage('Scan Images') {
            steps {
                script {
                    echo "Scanning backend image with Trivy..."
                    sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image ${DOCKERHUB_USERNAME}/${IMAGE_BACKEND}:${BUILD_NUMBER}"

                    echo "Scanning frontend image with Trivy..."
                    sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image ${DOCKERHUB_USERNAME}/${IMAGE_FRONTEND}:${BUILD_NUMBER}"
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        echo "Logging in to Docker Hub..."
                        sh "echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin"

                        echo "Pushing backend images..."
                        sh "docker push ${DOCKERHUB_USERNAME}/${IMAGE_BACKEND}:${BUILD_NUMBER}"
                        sh "docker push ${DOCKERHUB_USERNAME}/${IMAGE_BACKEND}:latest"

                        echo "Pushing frontend images..."
                        sh "docker push ${DOCKERHUB_USERNAME}/${IMAGE_FRONTEND}:${BUILD_NUMBER}"
                        sh "docker push ${DOCKERHUB_USERNAME}/${IMAGE_FRONTEND}:latest"

                        echo "Logout from Docker Hub..."
                        sh "docker logout"
                    }
                }
            }
        }

        stage('Update Compose') {
            steps {
                echo "Update Docker Compose stage pending..."
            }
        }

        stage('Deploy') {
            steps {
                echo "Deploy stage pending..."
            }
        }
    }

    post {
        failure {
            echo "Pipeline failed! Check previous stages for errors."
        }
        success {
            echo "Pipeline completed successfully!"
        }
    }
}
