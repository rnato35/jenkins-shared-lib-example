def call(body) {

    def pipelineParams = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()
    pipelineParams.each { println(it) }

    pipelineParams.jenkinsNode = 'any'

    pipeline {
        agent any
        stages {
            stage('Docker build') {
                steps {
                    script {
                        println("Test print")
                        println(testvar1)
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