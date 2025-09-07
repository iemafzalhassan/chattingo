    1 pipeline {
    2     agent any
    3     
    4     stages {
    5         stage('Git Clone') {
    6             steps {
    7                 git branch: 'main', url: 'https://github.com/ergaurav3155/chattingo.git'
    8                 // Agar repository private hai to credentials aise add karein:
    9                 // git branch: 'main', credentialsId: 'github-credentials', url: 'https://github.com/ergaurav3155/chattingo.git'
   10             }
   11         }
   12         stage('Image Build') {
   13             // Hum isko agle step mein karenge
   14         }
   15         stage('Filesystem Scan') {
   16             // Hum isko agle step mein karenge
   17         }
   18         stage('Image Scan') {
   19             // Hum isko agle step mein karenge
   20         }
   21         stage('Push to Registry') {
   22             // Hum isko agle step mein karenge
   23         }
   24         stage('Update Compose') {
   25             // Hum isko agle step mein karenge
   26         }
   27         stage('Deploy') {
   28             // Hum isko agle step mein karenge
   29         }
   30     }
   31 }

