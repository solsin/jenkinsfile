// src/solsin/Common.groovy
package solsin

def checkoutBranch(String gitHost, String branch, jobName) {
  def timeStamp = Calendar.getInstance().getTime().format('YYYY/MM/dd',TimeZone.getTimeZone('CST'))
  def VERSION_NUMBER = "dev/"+timeStamp+"/${jobName}-"+currentBuild.number
  echo VERSION_NUMBER
    
	git branch: branch, credentialsId: GIT_CREDENTIAL, url: 'https://'+gitHost
	sh "git tag -a ${VERSION_NUMBER} -m 'tagged by jenkins'"
  withCredentials([
      usernamePassword(credentialsId: GIT_CREDENTIAL, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')
  ]) {
      USERNAME = USERNAME.replaceAll("@", "%40")
      sh "git push https://${USERNAME}:${PASSWORD}@${gitHost} --tags"
  }
}

def checkoutWithTag(String gitHost, String specificBranch, String tag) {
  def branchName = ""
  if (specificBranch == null || specificBranch.length() == 0) {
    // specificBranch가 빈값이 아닌 경우 해당 브랜치로 checkout을 받는다
    git branch: specificBranch, credentialsId: GIT_CREDENTIAL, url: 'https://'+gitHost                        
  } else {
    // specificBranch가 빈값인 경우 tag에 해당하는 branch가 존재하지 않으면 새로 생성하고 있으면 그걸 그대로 사용한다.
    def newBranchName = tag
    if (tag.startsWith("dev/")) {
      newBranchName = "stg/"+tag.substring(4, tag.length())
    }
    
    sh "git fetch --all"
    
    EXISTING_REMOTE_BRANCH = sh(
      script: "git branch -r --list origin/${newBranchName}",
      returnStdout: true
    ).trim()
    EXISTING_LOCAL_BRANCH = sh(
      script: "git branch --list ${newBranchName}",
      returnStdout: true
    ).trim()
    
    if (EXISTING_LOCAL_BRANCH.length() == 0 && EXISTING_REMOTE_BRANCH.length() == 0) {
      //create local branch from dev tag
      sh "git checkout -b ${newBranchName} ${tag}"      
    } else if (EXISTING_LOCAL_BRANCH.length() == 0 && EXISTING_REMOTE_BRANCH.length() > 0) {
      //create local branch from remote branch
      sh "git checkout -b ${newBranchName} ${EXISTING_REMOTE_BRANCH}"
    } else if (EXISTING_LOCAL_BRANCH.length()) {
      // using local branch
      sh "git checkout ${newBranchName}"
    }    
    
    // else {
    //  git branch: newBranchName, credentialsId: 'glyde-codecommit-admin', url: 'https://git-codecommit.ap-northeast-2.amazonaws.com/v1/repos/glyde-mall-develop'
    //}
  }

  withCredentials([
      usernamePassword(credentialsId: GIT_CREDENTIAL, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')
  ]) {
      USERNAME = USERNAME.replaceAll("@", "%40")
      sh "git push https://${USERNAME}:${PASSWORD}@${gitHost} --set-upstream origin"
  }
}


return this