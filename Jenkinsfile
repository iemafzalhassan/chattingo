pipeline {
    agent any

    environment {
        DOCKERHUB_USERNAME = 'gaurav3155' // Apna Docker Hub username
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
                    echo "Running Trivy filesystem scan..."
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

        stage('Push to Docker Hub') {
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

        stage('Update docker-compose') {
            steps {
                script {
                    echo "Updating docker-compose.yml with new image tags..."
                    // Correctly escape BUILD_NUMBER placeholder for sed
                    sh "sed -i 's/\\\${BUILD_NUMBER}/${env.BUILD_NUMBER}/g' docker-compose.yml"
                    echo "docker-compose.yml updated with build number ${env.BUILD_NUMBER}"
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    echo "Deploying services to Hostinger VPS..."
                    withCredentials([sshUserPrivateKey(credentialsId: 'ssh-pass', keyFileVariable: 'SSH_KEY')]) {
                        // Replace 'your_remote_user' and 'your_remote_host' with your actual SSH user and VPS IP/hostname.
                        // 'StrictHostKeyChecking=no' is used for automation but consider its security implications.
                        sh "scp -o StrictHostKeyChecking=no -i ${SSH_KEY} docker-compose.yml root@72.60.111.65:/opt/chattingo/"
                        sh "ssh -o StrictHostKeyChecking=no -i ${SSH_KEY} root@72.60.111.65 \"cd /opt/chattingo && docker-compose pull && docker-compose up -d\"" 
                    }
                    echo "Deployment complete."
                }
            }
        }
    }
}
