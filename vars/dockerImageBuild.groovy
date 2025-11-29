def call(Map config = [:]) {
	def required = ["PROJECT_NAME", "COMPONENT", "MY_GIT_LATEST_COMMIT_ID"]
    required.each { key ->
        if (!config[key]) {
            error "❌ IMAGE REGISTRY: Missing required parameter '${key}'"
        }
    }

    def projectName   = env.PROJECT_NAME
    def component     = env.COMPONENT
    def imageTag      = env.MY_GIT_LATEST_COMMIT_ID

    echo "🔨 Building Docker Image"
    sh "docker build . -t ${projectName}-${component}:${imageTag}"
    echo "✅ Docker Image successfully built"
}
