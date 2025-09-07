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
                script {
                    // Assuming Trivy is installed on the Jenkins agent
                    sh "trivy fs --exit-code 0 --severity HIGH,CRITICAL ."
                }
            }
        }

        stage('Image Scan') {
            steps {
                script {
                    echo "Scanning backend image..."
                    sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image ${env.DOCKERHUB_USERNAME}/chattingo-backend:${env.BUILD_NUMBER}"

                    echo "Scanning frontend image..."
                    sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image ${env.DOCKERHUB_USERNAME}/chattingo-frontend:${env.BUILD_NUMBER}"
                }
            }
        }

        stage('Push to Registry') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        echo "Logging in to Docker Hub..."
                        sh "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin"

                        echo "Pushing backend images..."
                        sh "docker push ${env.DOCKERHUB_USERNAME}/chattingo-backend:${env.BUILD_NUMBER}"
                        sh "docker push ${env.DOCKERHUB_USERNAME}/chattingo-backend:latest"

                        echo "Pushing frontend images..."
                        sh "docker push ${env.DOCKERHUB_USERNAME}/chattingo-frontend:${env.BUILD_NUMBER}"
                        sh "docker push ${env.DOCKERHUB_USERNAME}/chattingo-frontend:latest"

                        echo "Logging out from Docker Hub"
                        sh "docker logout"
                    }
                }
            }
        }

        stage('Update docker-compose with new image tags') {
            steps {
                script {
                    echo "Updating docker-compose.yml with new image tags..."
                    // Replace the BUILD_NUMBER placeholder in docker-compose.yml with the current Jenkins BUILD_NUMBER
                    sh "sed -i 's/\\$\\{BUILD_NUMBER\\\}/'${env.BUILD_NUMBER}'/g' docker-compose.yml"
                    echo "docker-compose.yml updated with build number ${env.BUILD_NUMBER}"
                }
            }
        }

        stage('Deploy') {
            steps {
                echo "Yeh stage abhi baaki hai."
            }
        }
    }
}
