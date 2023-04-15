def buildAndPushDockerImage(String imageName, String imageTag, String dockerfilePath) {
    withCredentials([usernamePassword(credentialsId: 'dockerhub-id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
        script {
            // Authenticate with Docker Hub
            sh "echo '{\"auths\":{\"${registry}\":{\"username\":\"${DOCKERHUB_USERNAME}\",\"password\":\"${DOCKERHUB_PASSWORD}\"}}}' | docker config create --spec - docker-hub-config"

            // Build and push the Docker image
            sh "docker build -t ${imageName}:${imageTag} -f ${dockerfilePath} ."
            sh "docker push ${imageName}:${imageTag}"
        }
    }
}