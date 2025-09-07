pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'ergaurav3155'      // Docker Hub username
        BUILD_TAG = "${env.BUILD_NUMBER}"        // Dynamic build tag
    }

    stages {
        stage('Git Clone') {
            steps {
                git branch: 'main', url: 'https://github.com/ergaurav3155/chattingo.git'
            }
        }

        stage('Build Docker Images') {
            steps {
                echo "Building all images using Docker Compose..."
                sh "docker-compose build"
            }
        }

        stage('Tag Images for Scan') {
            steps {
                echo "Tagging images with build number for Trivy scan..."
                sh "docker tag chattingo-backend:latest ${DOCKERHUB_USERNAME}/chattingo-backend:${BUILD_TAG}"
                sh "docker tag chattingo-frontend:latest ${DOCKERHUB_USERNAME}/chattingo-frontend:${BUILD_TAG}"
            }
        }

        stage('Scan Docker Images') {
            steps {
                echo "Scanning backend image with Trivy..."
                sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image ${DOCKERHUB_USERNAME}/chattingo-backend:${BUILD_TAG}"

                echo "Scanning frontend image with Trivy..."
                sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image ${DOCKERHUB_USERNAME}/chattingo-frontend:${BUILD_TAG}"
            }
        }

        stage('Push Images to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', 
                                                     passwordVariable: 'DOCKER_PASSWORD', 
                                                     usernameVariable: 'DOCKER_USERNAME')]) {

                        echo "Logging in to Docker Hub..."
                        sh "echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin"

                        echo "Tagging images for Docker Hub push..."
                        sh "docker tag chattingo-backend:latest \$DOCKER_USERNAME/chattingo-backend:latest"
                        sh "docker tag chattingo-frontend:latest \$DOCKER_USERNAME/chattingo-frontend:latest"

                        echo "Pushing backend and frontend images..."
                        sh "docker push \$DOCKER_USERNAME/chattingo-backend:${BUILD_TAG}"
                        sh "docker push \$DOCKER_USERNAME/chattingo-backend:latest"
                        sh "docker push \$DOCKER_USERNAME/chattingo-frontend:${BUILD_TAG}"
                        sh "docker push \$DOCKER_USERNAME/chattingo-frontend:latest"

                        echo "Logging out from Docker Hub..."
                        sh "docker logout"
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo "Deploying services using Docker Compose..."
                sh "docker-compose up -d"
            }
        }
    }

    post {
        always {
            echo "Cleaning up unused Docker resources..."
            sh "docker system prune -f"
        }
        success {
            echo "Pipeline completed successfully!"
        }
        failure {
            echo "Pipeline failed. Check logs above for details."
        }
    }
}
