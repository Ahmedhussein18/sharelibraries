def call(Map params) {
    def githubCredentialsId = params.githubCredentialsId
    def githubRepo = params.githubRepo
    def dockerhubCredentialsId = params.dockerhubCredentialsId
    def imageName = params.imageName
    def imageTag = params.imageTag ?: "latest" 

    stage('Clone Repository') {
    steps {
        withCredentials([usernamePassword(credentialsId: githubCredentialsId, usernameVariable: 'GIT_USER', passwordVariable: 'GIT_PASS')]) {
            git(
                branch: 'main',
                credentialsId: githubCredentialsId,
                url: githubRepo
            )
        }
    }
}

 stage('Build Docker Image') {
        sh "docker build -t ${imageName}:${imageTag} ."
    }

stage('Push Docker Image') {
        withCredentials([usernamePassword(credentialsId: dockerhubCredentialsId, passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
            sh "echo \${DOCKER_PASS} | docker login -u \${DOCKER_USER} --password-stdin"
            sh "docker push ${imageName}:${imageTag}"
        }
    }
}