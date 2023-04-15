def call(body) {
    def agent = 'any'
    pipeline {
        agent {
            ${agent}
        }
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