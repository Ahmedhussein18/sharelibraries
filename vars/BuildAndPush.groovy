// Assuming this is a shared library file vars/BuildAndPush.groovy
def call(Map params) {
    // Extract parameters
    def githubCredentialsId = params.githubCredentialsId
    def githubRepo = params.githubRepo
    def dockerhubCredentialsId = params.dockerhubCredentialsId
    def imageName = params.imageName
    def imageTag = params.imageTag ?: "latest" // Default to 'latest' if no tag provided

    // Start the pipeline
    pipeline {
        agent any  // Specify the agent to run the pipeline

        stages {
            // Clone repository
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

            // Build Docker Image
            stage('Build Docker Image') {
                steps {
                    sh "docker build -t ${imageName}:${imageTag} ."
                }
            }

            // Push Docker Image
            stage('Push Docker Image') {
                steps {
                    withCredentials([usernamePassword(credentialsId: dockerhubCredentialsId, passwordVariable: 'DOCKER_PASS', usernameVariable: 'DOCKER_USER')]) {
                        sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin"
                        sh "docker push ${imageName}:${imageTag}"
                    }
                }
            }
        }
    }
}
