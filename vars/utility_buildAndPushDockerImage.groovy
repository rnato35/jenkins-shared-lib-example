def call(String imageName, String imageTag, String dockerfilePath) {
    withCredentials([usernamePassword(credentialsId: 'dockerhub-id', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
        script {
            // Authenticate with Docker Hub
            sh "echo \${DOCKERHUB_PASSWORD} | docker login --username=\${DOCKERHUB_USERNAME} --password-stdin"

                        sh "ls -ltra && pwd && echo \${WORKSPACE}"

            // Build and push the Docker image
            sh "docker build -t ${imageName}:${imageTag} -f ${WORKSPACE}${dockerfilePath} ."
            sh "docker push ${imageName}:${imageTag}"

        }
    }
}