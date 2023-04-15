def call(body) {
    def jenkinsNode = 'any'
    pipeline {
        agent "${jenkinsNode}"
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