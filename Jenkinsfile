pipeline {
    agent any
    libraries {
        lib('shared-library@main')
    }
    stages {
        stage('Git Clone') {
            steps {
                script {
                    chattingoStages.gitClone()
                }
            }
        }
    }
}