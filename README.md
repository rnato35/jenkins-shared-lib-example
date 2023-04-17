# Jenkins Shared Library

This repository contains the Jenkins Shared Library utilized [here](https://github.com/rnato35/jenkins-pipeline-example).

Inside the 'vars' directory, you will find the following files:

- `pipeline_docker_k8s_deploy.groovy`: Main pipeline file.
- `utility_buildAndPushDockerImage.groovy`: Groovy  utility to build and push Docker images.
- `utility_eksDeploy.groovy`: Groovy Utility to deploy a k8s manifest file.

## Pipeline Stages

### Docker Build/Push

In this stage, we call the `buildAndPushDockerImage` utility, which retrieves secrets and variables to log in to Docker Hub and build/push the required image.

### Update manifest

In this stage, we receive a K8s manifest file as a variable and dynamically update its configuration utilizing placeholders.

### Deploy to EKS

In this stage, we call the `eksDeploy` utility, which pulls the required Docker Image from the specified registry, configures the AWS environment, and applies the required manifest.

### Post Execution
As a last step, the pipeline removes any Docker credentials stored in the `.docker/config.json` file and cleans up the Workspace.