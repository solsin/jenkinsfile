@Library('common-lib') import solsin.*

pipeline {
  agent any
  
  stages {
      stage('Build') {
        echo 'Building..'
        echo "Selected TAG: ${GIT_TAG}"
        if (GIT_TAG == "master") {
          GIT_TAG = sh(
            script: "git tag -l --sort=-v:refname dev/*/bo* | head -n 1",
            returnStdout: true
          ).trim()
          echo "get latest tag: ${GIT_TAG}"
        }
        
        steps {
          script {
              def common = new Common()
              //common.checkoutSCM('master', 'dev')
          }                
        }
      }
      stage('Test') {
        steps {
            echo 'Testing..'
        }
      }
      stage('Deploy') {
        steps {
            echo 'Deploying....'
        }
      }
  }
}