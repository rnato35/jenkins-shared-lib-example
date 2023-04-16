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
                        
                        def fileContent = readFile("${WORKSPACE}/${pipelineParams.eksManifestFile}")

                        def placeholders = []
                        def matcher = (fileContent =~ \w+_PLACEHOLDER\b)
                        matcher.each {
                            placeholders.add(it[0])
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