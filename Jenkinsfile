pipeline {
    agent any
    
    stages {
        stage('Git Clone') {
            steps {
                // 'main' branch se code clone karega
                git branch: 'main', url: 'https://github.com/ergaurav3155/chattingo.git'
                // Agar repository private hai to credentialsId ka upyog karein
                // git branch: 'main', credentialsId: 'github-credentials', url: 'https://github.com/ergaurav3155/chattingo.git'
            }
        }
        stage('Image Build') {
            steps {
                echo "Yeh stage abhi baaki hai."
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
                echo "Yeh stage abhi baaki hai."
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
