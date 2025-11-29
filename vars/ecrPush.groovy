def call(Map config = [:]) {

	def required = ["PROJECT_NAME", "COMPONENT", "MY_GIT_LATEST_COMMIT_ID", "REGION", "ECR_REPO_URI", "AWS_CREDENTIALS_ID"]
    required.each { key ->
        if (!config[key]) {
            error "❌ ECR REGISTRY: Missing required parameter '${key}'"
        }
    }
	
    def projectName   = config.PROJECT_NAME
    def component     = config.COMPONENT
    def imageTag      = config.MY_GIT_LATEST_COMMIT_ID 
    def region        = config.REGION ?: "ap-south-1"
    def ecrRepoUri    = config.ECR_REPO_URI
    def credentialsId = config.AWS_CREDENTIALS_ID

    withAWS(credentials: credentialsId, region: "${region}") {  // Plugin: AWS steps
        sh """
            echo "Tagging docker image"
            docker tag ${projectName}-${component}:${imageTag} ${ecrRepoUri}/${projectName}-${component}:${imageTag}

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
