def call(Map config = [:]) {
    def requiredDocker = ["DOCKER_HUB_CREDENTIALS_ID", "DOCKER_REPO_URI"]
    requiredDocker.each { key ->
        if (!config[key]) {
            error "❌ IMAGE REGISTRY: Missing required parameter '${key}'"
        }
    }
    
    def credentialsId = config.DOCKER_HUB_CREDENTIALS_ID
    def dockerRepoUri = config.DOCKER_REPO_URI ?: "docker.io"   

    withCredentials([usernamePassword(
        credentialsId: credentialsId, 
        usernameVariable: 'dockerUser', 
        passwordVariable: 'dockerPass'  
    )]) {
            sh """
                echo "🔖 Tagging Docker Image"  #docker.io/dockeruser/expense-backend:5d4ret
                docker tag ${component}:${imageTag} ${dockerRepoUri}/${dockerUser}/${projectName}-${component}:${imageTag}
                                        
                echo "🔐 Logging into Docker Hub as '${dockerUser}'"
                set +x
                echo "${dockerPass}" | docker login -u "${dockerUser}" --password-stdin
                set -x

                echo "🚀 Pushing Docker Image to Docker Hub"
                docker push ${dockerRepoUri}/${dockerUser}/${projectName}-${component}:${imageTag}

                echo "✅ Pushed Docker Image to Docker Hub Successfully"

                # Logout and final confirmation
                docker logout
                echo "✅ Logged out from Docker Hub Successfully"
            """    
        }
   
    
}
