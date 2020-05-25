@Library('common-lib') import solsin.*

pipeline {
  agent any
  
  stages {
      stage('checkout') {
        steps {
          script {
            echo 'Checkout..'
            
            def common = new Common()
            if (ENV_NAME == "dev") {
              common.checkoutBranch(GIT_HOST, "master", JOB_NAME)                  
            } else if (ENV_NAME == "stg") {
              echo "Selected TAG: ${GIT_TAG}"
              if (GIT_TAG == "master") {
                // dev tag중 가장 마지막 tag 선택
                GIT_TAG = sh(
                  script: "git tag -l --sort=-v:refname dev/*/bo* | head -n 1",
                  returnStdout: true
                ).trim()
                echo "get latest tag: ${GIT_TAG}"
              }
              common.checkoutWithTag(GIT_HOST, GIT_BRANCH, GIT_TAG)
            }
          }                
        }
      }
      stage('Build') {
        steps {
            echo 'Build..'
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