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

                        def placeholders = []
                        sh "grep -o '\"docker[^\\\"]*_PLACEHOLDER\"' ${pipelineParams.eksManifestFile} | sort -u | tr -d '\"'".eachLine {
                            placeholders.add(it.trim())
                        }
                        println(placeholders)

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