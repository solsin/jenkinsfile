@Library('common-lib') import solsin.*

pipeline {
  agent any
  
  stages {
      stage('Build') {
        steps {
          script {
            echo 'Building..'
            echo "Selected TAG: ${GIT_TAG}"
            
            if (GIT_TAG == "master") {
              // dev tag중 가장 마지막 tag 선택
              GIT_TAG = sh(
                script: "git tag -l --sort=-v:refname dev/*/bo* | head -n 1",
                returnStdout: true
              ).trim()
              echo "get latest tag: ${GIT_TAG}"
            }

            def common = new Common()
            common.checkoutWithTag(GIT_BRANCH, GIT_TAG)
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