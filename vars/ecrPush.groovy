def call(Map config = [:]) {

	def required = ["REGION", "ECR_REPO_URI", "AWS_CREDENTIALS_ID"]
    required.each { key ->
        if (!config[key]) {
            error "❌ IMAGE REGISTRY: Missing required parameter '${key}'"
        }
    }

    def region        = config.REGION
    def ecrRepoUri    = config.ECR_REPO_URI
    def credentialsId = config.AWS_CREDENTIALS_ID

    withAWS(credentials: credentialsId, region: "${region}") {  // Plugin: AWS steps
        sh """
            echo "Tagging docker image"
            docker tag ${component}:${imageTag} ${ecrRepoUri}/${projectName}-${component}:${imageTag}

            echo "Logging into ECR"
            set +x
            aws ecr get-login-password --region ${region} | docker login --username AWS --password-stdin ${ecrRepoUri}
            set -x

            echo "Pushing docker image to ECR Repo"
            docker push ${ecrRepoUri}/${projectName}-${component}:${imageTag}

            echo "✅ Pushed Docker Image to ECR Successfully"

            # Logout and final confirmation
            ecr logout ${ecrRepoUri}"
            echo "✅ Logged out from ECR Successfully"
        """
    } 
}
