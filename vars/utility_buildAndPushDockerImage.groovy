def call(String imageName, String imageTag, String dockerfilePath) {
    withCredentials([usernamePassword(credentialsId: 'dockerhub-id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
        script {
            // Authenticate with Docker Hub
            sh "echo \${DOCKERHUB_PASSWORD} | docker login --username=\${DOCKERHUB_USERNAME} --password-stdin"

            // Build and push the Docker image
            sh "docker build -t ${imageName}:${imageTag} -f ${dockerfilePath} ."
            sh "docker push ${imageName}:${imageTag}"
        }
    }
}