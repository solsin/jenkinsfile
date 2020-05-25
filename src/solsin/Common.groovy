// src/solsin/Common.groovy
package solsin

def checkoutSCM(String branch, jobName) {
  def timeStamp = Calendar.getInstance().getTime().format('YYYY/MM/dd/',TimeZone.getTimeZone('CST'))
  def VERSION_NUMBER = timeStamp+"/${jobName}/"+currentBuild.number
  echo VERSION_NUMBER
    
	git branch: branch, credentialsId: 'glyde-codecommit-admin', url: 'https://git-codecommit.ap-northeast-2.amazonaws.com/v1/repos/glyde-mall-develop'
	sh "git tag -a ${VERSION_NUMBER} -m 'tagged by jenkins'"
  withCredentials([
      usernamePassword(credentialsId: 'glyde-codecommit-admin', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')
  ]) {
      sh "git push https://${USERNAME}:${PASSWORD}@git-codecommit.ap-northeast-2.amazonaws.com/v1/repos/glyde-mall-develop --tags"
  }
}

def checkoutWithTag(String specificBranch, String tag) {
  def branchName = ""
  if (specificBranch == null || specificBranch.length() == 0) {
    // specificBranch가 빈값이 아닌 경우 해당 브랜치로 checkout을 받는다
    git branch: specificBranch, credentialsId: 'glyde-codecommit-admin', url: 'https://git-codecommit.ap-northeast-2.amazonaws.com/v1/repos/glyde-mall-develop'                        
  } else {
    // specificBranch가 빈값인 경우 tag에 해당하는 branch가 존재하지 않으면 새로 생성하고 있으면 그걸 그대로 사용한다.
    def newBranchName = tag
    if (tag.startsWith("dev/")) {
      newBranchName = "stg/"+tag.substring(4, tag.length())
    }

    EXISTING_REMOTE_BRANCH = sh(
      script: "git branch -r --list origin/${newBranchName}",
      returnStdout: true
    ).trim()
    EXISTING_LOCAL_BRANCH = sh(
      script: "git branch --list ${newBranchName}",
      returnStdout: true
    ).trim()
    
    if (EXISTING_LOCAL_BRANCH.length() == 0) {
      //create local branch
      sh "git checkout -b ${newBranchName} ${tag}"                                
    } else {
      git branch: EXISTING_BRANCH, credentialsId: 'glyde-codecommit-admin', url: 'https://git-codecommit.ap-northeast-2.amazonaws.com/v1/repos/glyde-mall-develop'
    }
  }

  withCredentials([
      usernamePassword(credentialsId: 'glyde-codecommit-admin', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')
  ]) {
      sh "git push https://${USERNAME}:${PASSWORD}@git-codecommit.ap-northeast-2.amazonaws.com/v1/repos/glyde-mall-develop --set-upstream origin"
  }
}


return this