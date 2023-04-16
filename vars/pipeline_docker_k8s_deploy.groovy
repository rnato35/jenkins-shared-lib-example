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

                        def placeholders = manifestContents.readLines().findAll { it.contains('_PLACEHOLDER') }.collect { it.replaceAll(/.*(_PLACEHOLDER.*)/, '$1') }
                        placeholders = placeholders.collect { it.replaceAll(/_PLACEHOLDER:/, '').replaceAll(/"/, '').trim() }
                        echo "Found Placeholders: ${placeholders}"

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