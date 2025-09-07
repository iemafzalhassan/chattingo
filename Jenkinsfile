pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'ergaurav3155'
        DOCKERHUB_CREDENTIALS_ID = 'dockerhub-credentials'
    }

    stages {

        stage('Git Clone') {
            steps {
                git branch: 'main', url: 'https://github.com/ergaurav3155/chattingo.git'
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    // Backend
                    def backendTag = "${env.DOCKERHUB_USERNAME}/chattingo-backend:${env.BUILD_NUMBER}"
                    def backendLatest = "${env.DOCKERHUB_USERNAME}/chattingo-backend:latest"
                    echo "Building backend image..."
                    sh "docker build -t ${backendTag} -t ${backendLatest} ./backend"

                    // Frontend
                    def frontendTag = "${env.DOCKERHUB_USERNAME}/chattingo-frontend:${env.BUILD_NUMBER}"
                    def frontendLatest = "${env.DOCKERHUB_USERNAME}/chattingo-frontend:latest"
                    echo "Building frontend image..."
                    sh "docker build -t ${frontendTag} -t ${frontendLatest} ./frontend"
                }
            }
        }

        stage('Scan Docker Images') {
            steps {
                script {
                    echo "Scanning backend image..."
                    sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image ${env.DOCKERHUB_USERNAME}/chattingo-backend:${env.BUILD_NUMBER}"

                    echo "Scanning frontend image..."
                    sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image ${env.DOCKERHUB_USERNAME}/chattingo-frontend:${env.BUILD_NUMBER}"
                }
            }
        }

        stage('Login & Push to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: env.DOCKERHUB_CREDENTIALS_ID, passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        echo "Logging in to Docker Hub..."
                        sh "echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin"

                        echo "Pushing backend images..."
                        sh "docker push ${env.DOCKER_USERNAME}/chattingo-backend:${env.BUILD_NUMBER}"
                        sh "docker push ${env.DOCKER_USERNAME}/chattingo-backend:latest"

                        echo "Pushing frontend images..."
                        sh "docker push ${env.DOCKER_USERNAME}/chattingo-frontend:${env.BUILD_NUMBER}"
                        sh "docker push ${env.DOCKER_USERNAME}/chattingo-frontend:latest"

                        echo "Logging out from Docker Hub..."
                        sh "docker logout"
                    }
                }
            }
        }

        stage('Deploy Application') {
            steps {
                script {
                    echo "Deploying application using Docker Compose..."
                    // Agar aap locally deploy karna chahte ho:
                    // sh "docker-compose -f docker-compose.yml up -d --build"

                    echo "Deployment stage complete. Customize this for your environment."
                }
            }
        }
    }

    post {
        success {
            echo "CI/CD pipeline completed successfully! Images pushed and deployment done."
        }
        failure {
            echo "Pipeline failed! Check logs for details."
        }
    }
}
