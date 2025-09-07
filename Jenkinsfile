def chattingoStages

pipeline {
    agent any
    stages {
        stage('Load Scripts') {
            steps {
                script {
                    chattingoStages = load 'shared-library/vars/chattingoStages.groovy'
                }
            }
        }
        stage('Git Clone') {
            steps {
                script {
                    chattingoStages.gitClone()
                }
            }
        }
    }
}