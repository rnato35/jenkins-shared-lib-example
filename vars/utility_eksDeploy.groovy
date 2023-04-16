def call(opts) {
    // Pull the Docker image from Docker Hub
    sh "docker pull ${opts.dockerRegistry}/${opts.dockerImageName}:${opts.dockerImageTag}"
    
    // Configure the AWS CLI with your AWS credentials
    withCredentials([string(credentialsId: 'AWS_ACCESS_KEY_ID', variable: 'aws_access_key')]) {
        sh 'aws configure set aws_access_key_id $aws_access_key'
    }
    withCredentials([string(credentialsId: 'AWS_SECRET_ACCESS_KEY', variable: 'aws_secret_key')]) {
        sh 'aws configure set aws_secret_access_key $aws_secret_key'
    }
    withCredentials([string(credentialsId: 'AWS_DEFAULT_REGION', variable: 'aws_region')]) {
        sh 'aws configure set default.region $aws_region'
        sh "aws eks update-kubeconfig --name ${opts.eksClusterName} --region $aws_region"
        sh "echo ${opts.eksClusterName}"
    }
    
    /* // Apply the Kubernetes deployment manifest to deploy the Docker image
    sh "kubectl apply -f ${opts.eksManifestFile}" */

    sh "kubectl get pods"
    sh "kubectl get namespaces"
}