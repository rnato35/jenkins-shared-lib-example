def call(body) {

    def pipelineParams = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()
    pipelineParams.each { println(it) }

    pipeline {
        agent any
        stages {
            stage('Install Docker') {
            steps {
                sh 'curl -fsSL https://get.docker.com -o get-docker.sh'
                sh 'sh get-docker.sh'
            }
        }
            stage('Docker build') {
                steps {
                    script {
                        utility_buildAndPushDockerImage(pipelineParams.dockerImageName, pipelineParams.dockerImageTag, pipelineParams.dockerfilePath)
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