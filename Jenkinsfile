pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'ergaurav3155' // Docker Hub username
        BUILD_TAG = "${env.BUILD_NUMBER}"   // Dynamic tag for images
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

        stage('Scan Docker Images') {
            steps {
                echo "Scanning backend image with Trivy..."
                sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image ergaurav3155/chattingo-backend:${BUILD_TAG}"

                echo "Scanning frontend image with Trivy..."
                sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image ergaurav3155/chattingo-frontend:${BUILD_TAG}"
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

                        echo "Tagging backend and frontend images..."
                        sh "docker tag chattingo-backend:latest \$DOCKER_USERNAME/chattingo-backend:\$BUILD_TAG"
                        sh "docker tag chattingo-backend:latest \$DOCKER_USERNAME/chattingo-backend:latest"
                        sh "docker tag chattingo-frontend:latest \$DOCKER_USERNAME/chattingo-frontend:\$BUILD_TAG"
                        sh "docker tag chattingo-frontend:latest \$DOCKER_USERNAME/chattingo-frontend:latest"

                        echo "Pushing backend and frontend images..."
                        sh "docker push \$DOCKER_USERNAME/chattingo-backend:\$BUILD_TAG"
                        sh "docker push \$DOCKER_USERNAME/chattingo-backend:latest"
                        sh "docker push \$DOCKER_USERNAME/chattingo-frontend:\$BUILD_TAG"
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
