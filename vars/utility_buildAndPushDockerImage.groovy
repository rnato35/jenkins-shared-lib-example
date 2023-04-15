def call(opts) {
    withCredentials([usernamePassword(credentialsId: 'dockerhub-id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
        script {
            // Authenticate with Docker Hub
            sh "echo \${DOCKERHUB_PASSWORD} | docker login --username=\${DOCKERHUB_USERNAME} --password-stdin"

            // Build and push the Docker image
            sh "docker build -t $opts.dockerRegistry/$opts.dockerImageName:$opts.dockerImageTag -f $WORKSPACE/$opts.dockerfilePath ."
            sh "docker push $opts.dockerRegistry/$opts.dockerImageName:$opts.dockerImageTag"
        }
    }
}