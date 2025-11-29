def call(Map config = [:]) {
    def requiredDocker = ["PROJECT_NAME", "COMPONENT", "MY_GIT_LATEST_COMMIT_ID", "DOCKER_HUB_CREDENTIALS_ID", "DOCKER_REPO_URI"]
    requiredDocker.each { key ->
        if (!config[key]) {
            error "❌ IMAGE REGISTRY: Missing required parameter '${key}'"
        }
    }

    def projectName   = config.PROJECT_NAME
    def component     = config.COMPONENT
    def imageTag      = config.MY_GIT_LATEST_COMMIT_ID 
    def credentialsId = config.DOCKER_HUB_CREDENTIALS_ID
    def dockerRepoUri = config.DOCKER_REPO_URI ?: "docker.io"   

    withCredentials([usernamePassword(
        credentialsId: credentialsId, 
        usernameVariable: 'dockerUser', 
        passwordVariable: 'dockerPass'  
    )]) {
            sh """
                echo "🔖 Tagging Docker Image"  #docker.io/dockeruser/expense-backend:5d4ret
                docker tag ${projectName}-${component}:${imageTag} ${dockerUser}/${projectName}-${component}:${imageTag}
                                        
                echo "🔐 Logging into Docker Hub as '${dockerUser}'"
                set +x
                echo "${dockerPass}" | docker login -u "${dockerUser}" --password-stdin
                set -x

                echo "🚀 Pushing Docker Image to Docker Hub"
                docker push ${dockerUser}/${projectName}-${component}:${imageTag}

                echo "✅ Pushed Docker Image to Docker Hub Successfully"

                # Logout and final confirmation
                docker logout
                echo "✅ Logged out from Docker Hub Successfully"
            """    
        }
   
    
}
