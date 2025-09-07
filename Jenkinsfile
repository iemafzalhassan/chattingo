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
                
                echo "Checking built images..."
                sh "docker images"
            }
        }

        stage('Tag Images for Scan & Push') {
            steps {
                echo "Tagging images with build number..."
                sh "docker tag chattingo-backend:latest ${DOCKERHUB_USERNAME}/chattingo-backend:${BUILD_TAG}"
                sh "docker tag chattingo-frontend:latest ${DOCKERHUB_USERNAME}/chattingo-frontend:${BUILD_TAG}"

                echo "Verifying tagged images..."
                sh "docker images | grep ${DOCKERHUB_USERNAME}"
            }
        }

        stage('Scan Docker Images') {
            steps {
                echo "Scanning backend image with Trivy..."
                sh """
                    if docker image inspect ${DOCKERHUB_USERNAME}/chattingo-backend:${BUILD_TAG} > /dev/null 2>&1; then
                        docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image ${DOCKERHUB_USERNAME}/chattingo-backend:${BUILD_TAG}
                    else
                        echo "Backend image with tag ${BUILD_TAG} not found! Skipping Trivy scan."
                    fi
                """

                echo "Scanning frontend image with Trivy..."
                sh """
                    if docker image inspect ${DOCKERHUB_USERNAME}/chattingo-frontend:${BUILD_TAG} > /dev/null 2>&1; then
                        docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image ${DOCKERHUB_USERNAME}/chattingo-frontend:${BUILD_TAG}
                    else
                        echo "Frontend image with tag ${BUILD_TAG} not found! Skipping Trivy scan."
                    fi
                """
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

                        echo "Tagging images for Docker Hub push (latest)..."
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
