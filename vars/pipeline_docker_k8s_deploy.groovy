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
            stage('Update manifest') {
                steps {
                    script {
                        // Replacing placeholders with actual values
                        sh "sed -i 's/dockerRegistry_PLACEHOLDER/${pipelineParams.dockerRegistry}/g' ${pipelineParams.eksManifestFile}"
                        sh "sed -i 's/dockerImageName_PLACEHOLDER/${pipelineParams.dockerImageName}/g' ${pipelineParams.eksManifestFile}"
                        sh "sed -i 's/dockerImageTag_PLACEHOLDER/${pipelineParams.dockerImageTag}/g' ${pipelineParams.eksManifestFile}"

                        println("--- UPDATED K8S MANIFEST ---")
                        sh "cat ${pipelineParams.eksManifestFile}"
                    }
                }
            }
            stage('Deploy to EKS') {
                steps {
                    script {
                        //utility_eksDeploy(pipelineParams)
                    }
                }
            }
        }
        post {
            always {
                println(WORKSPACE)
                sh("pwd && ls -ltra")
                cleanWs()
            }
        }
    }
}