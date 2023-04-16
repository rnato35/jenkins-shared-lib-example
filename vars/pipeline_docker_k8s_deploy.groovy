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

                        def manifestContents = readFile(file: pipelineParams.eksManifestFile)

                        sh("cat ${pipelineParams.eksManifestFile}")
                        def pattern = ~/^.*_PLACEHOLDER$/
                        def placeholders = manifestContents.findAll(pattern)

                        def placeholderVariables = placeholders.collect { placeholder ->
                        placeholder.substring(0, placeholder.size() - '_PLACEHOLDER'.size())
                        }

                        println(placeholderVariables)

                    }
                }
            }
            stage('Deploy to EKS') {
                steps {
                    script {
                        //utility_eksDeploy(pipelineParams)
                        echo "test"
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