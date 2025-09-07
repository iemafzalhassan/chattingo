pipeline {
    agent any
    libraries {
        lib('shared-library')
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