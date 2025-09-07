pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'ergaurav3155' // Tumhara Docker Hub username
    }

    stages {
        stage('Git Clone') {
            steps {
                git branch: 'main', url: 'https://github.com/ergaurav3155/chattingo.git'
            }
        }

        stage('Build & Push Images') {
            steps {
                script {
                    // Backend image tags
                    def backendTag = "${env.DOCKERHUB_USERNAME}/chattingo-backend:${env.BUILD_NUMBER}"
                    def backendTagLatest = "${env.DOCKERHUB_USERNAME}/chattingo-backend:latest"

                    echo "Building backend image..."
                    sh "docker build -t ${backendTag} -t ${backendTagLatest} ./backend"

                    // Frontend image tags
                    def frontendTag = "${env.DOCKERHUB_USERNAME}/chattingo-frontend:${env.BUILD_NUMBER}"
                    def frontendTagLatest = "${env.DOCKERHUB_USERNAME}/chattingo-frontend:latest"

                    echo "Building frontend image..."
                    sh "docker build -t ${frontendTag} -t ${frontendTagLatest} ./frontend"

                    // Docker Hub login and push
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        echo "Logging in to Docker Hub..."
                        sh "echo \$DOCKER_PASSWORD | docker login -u \$DOCKER_USERNAME --password-stdin"

                        echo "Pushing backend images..."
                        sh "docker push ${backendTag}"
                        sh "docker push ${backendTagLatest}"

                        echo "Pushing frontend images..."
                        sh "docker push ${frontendTag}"
                        sh "docker push ${frontendTagLatest}"

                        echo "Logging out from Docker Hub..."
                        sh "docker logout"
                    }
                }
            }
        }

        stage('Scan Images') {
            steps {
                script {
                    echo "Scanning backend image..."
                    sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image ${env.DOCKERHUB_USERNAME}/chattingo-backend:${env.BUILD_NUMBER}"

                    echo "Scanning frontend image..."
                    sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image ${env.DOCKERHUB_USERNAME}/chattingo-frontend:${env.BUILD_NUMBER}"
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
