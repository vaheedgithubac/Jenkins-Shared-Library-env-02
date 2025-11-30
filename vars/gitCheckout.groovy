def call(Map config = [:]) {
    echo "MY_GIT_URL: ${env.MY_GIT_URL}"
    echo "MY_GIT_REPO_TYPE: ${env.MY_GIT_REPO_TYPE}"
    
    def MY_GIT_URL       = env.MY_GIT_URL
    def MY_GIT_REPO_TYPE = env.MY_GIT_REPO_TYPE
    
    // Required config parameters
    def requiredEnvParams = [ "MY_GIT_URL", "MY_GIT_REPO_TYPE" ]

    // Validate required parameters
    /*
    requiredEnvParams.each { key ->
        if (!config[key]) {
            error "❌ GIT: Missing required parameter '${key}'"
        }
    }*/
    
    requiredEnvParams.each { key ->
        if (!requiredEnvParams[key]) {
            error "❌ GIT: Missing required parameter '${key}'"
        }
    }
    def my_git_repo_type = config.MY_GIT_REPO_TYPE.trim().toLowerCase().trim()
    def my_git_url       = config.MY_GIT_URL.trim()


    // === Handle credentials === 
    if (my_git_repo_type == "public") {
        echo "⚡ Public repo detected, setting MY_GIT_CREDENTIALS_ID = null"
        env.MY_GIT_CREDENTIALS_ID = null
    } 
    else if (my_git_repo_type == "private") {
    	     if (!env.MY_GIT_CREDENTIALS_ID || env.MY_GIT_CREDENTIALS_ID.trim() == "") {
              error "❌ MY_GIT_CREDENTIALS_ID is required for private repositories."
             }
           // else leave as is
          }
    else { error "❌ MY_GIT_REPO_TYPE must be 'public' or 'private'. Current: '${config.MY_GIT_REPO_TYPE}'" }


    // === Set Default Branch to 'main' === 
    if (!env.MY_GIT_BRANCH || env.MY_GIT_BRANCH.trim() == "") {
        echo "⚡ MY_GIT_BRANCH not defined, setting default branch to 'main'"
        env.MY_GIT_BRANCH = "main"
    }

    // === Log final values === 
    echo "✔ MY_GIT_URL            = ${my_git_url}"
    echo "✔ MY_GIT_REPO_TYPE      = ${my_git_repo_type}"
    echo "✔ MY_GIT_BRANCH         = ${env.MY_GIT_BRANCH}"
    echo "✔ MY_GIT_CREDENTIALS_ID = ${env.MY_GIT_CREDENTIALS_ID}"

    // === Perform Git Checkout === 
    git(
        url: my_git_url,
        branch: env.MY_GIT_BRANCH,
        credentialsId: env.MY_GIT_CREDENTIALS_ID
    )
    
    echo "Checked out Branch:'${env.MY_GIT_BRANCH}' from ${my_git_url} Successfully..."
}
