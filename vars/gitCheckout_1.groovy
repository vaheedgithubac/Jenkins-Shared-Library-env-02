def call(Map config = [:]) {
    echo "MY_GIT_URL: ${env.MY_GIT_URL}"
    echo "MY_GIT_REPO_TYPE: ${env.MY_GIT_REPO_TYPE}"
    
    // Required ENV parameters
    def requiredEnvParams = [ "MY_GIT_URL", "MY_GIT_REPO_TYPE" ]
   
   // Validate ENV parameters
    requiredEnvParams.each { key ->
        if (!env[key] || env[key].trim() == "") {
            error "❌ GIT: Missing required ENV parameter '${key}'"
        }
    }

  
        
    def my_git_repo_type = env.MY_GIT_REPO_TYPE.toLowerCase().trim()
    def my_git_url       = env.MY_GIT_URL.trim()
    
    // ?.trim() prevents NullPointerException. Handles literal "null", empty string, and real null.

    echo "Before Setting:  MY_GIT_BRANCH = ${env.MY_GIT_BRANCH}    MY_GIT_CREDENTIALS_ID = ${env.MY_GIT_CREDENTIALS_ID}   NO_VAR = ${env.NO_VAR}"
    
    echo "After Setting"
    def my_git_branch    = env.MY_GIT_BRANCH ?: 'main'
    def my_git_credentials_id = env.MY_GIT_CREDENTIALS_ID ?: null
    def no_var = env.NO_VAR ?: 'novalue'

    echo "my_git_branch: ${my_git_branch}"
    echo "my_git_credentials_id: ${my_git_credentials_id}"
    echo "no_var: ${no_var}"

    /*
    // === Handle credentials === 
    if (my_git_repo_type == "public") {
        echo "⚡ Public repo detected, setting MY_GIT_CREDENTIALS_ID = null"
        my_git_credentials_id     = null
        env.MY_GIT_CREDENTIALS_ID = null
    } 
    else if (my_git_repo_type == "private") {
    	     if (!my_git_credentials_id || my_git_credentials_id?.trim().toLowerCase() == "null" || my_git_credentials_id?.trim() == "") {
              error "❌ MY_GIT_CREDENTIALS_ID is required for private repositories."
             }
           // else leave as is
          }
    else { error "❌ MY_GIT_REPO_TYPE must be 'public' or 'private'. Current: '${config.MY_GIT_REPO_TYPE}'" }

    // === Set Default Branch to 'main' === 
    if (!my_git_branch || my_git_branch?.trim().toLowerCase() == "null" || my_git_branch?.trim() == "") {
        echo "⚡ MY_GIT_BRANCH not defined, setting default branch to 'main'"
        my_git_branch     = "main"
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
        branch: my_git_branch,
        credentialsId: my_git_credentials_id
    )
    
    echo "Checked out Branch:'${env.MY_GIT_BRANCH}' from ${my_git_url} Successfully..."
    */
}
