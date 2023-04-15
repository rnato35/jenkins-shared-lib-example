def call(body) {

    def pipelineParams = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()
    pipelineParams.each { println(it) }

    pipeline {
        agent any
        stages {
            stage('Docker build/push') {
                steps {
                    script {
                        utility_buildAndPushDockerImage(pipelineParams)
                    }
                }
            }
            stage('Deploy to EKS') {
                steps {
                    script {
                        // Pull the Docker image from Docker Hub
                        sh "docker pull ${pipelineParams.dockerRegistry}/${pipelineParams.dockerImageName}:${pipelineParams.dockerImageTag}"
                        
                        utility_eksDeploy(pipelineParams)
                    }
                }
            }
        }
        post {
            always {
                cleanWs()
            }
        }
    }
}