@Library('FirstShareLibrary') _

pipeline {
    agent any
    stages {
        stage('Build and Push Docker Image') {
            steps {
                script{
                BuildAndPush(
                    githubCredentialsId: 'github-token',
                    githubRepo: 'https://github.com/IbrahimAdell/Lab.git',
                    dockerhubCredentialsId: 'dockerhub-access-token',
                    dockerhubUserName: 'ahmedhussein18',
                    imageName: 'ivolve-image',
                    imageTag: 'latest'
                )
            }
                
            }
        }
    }
}
