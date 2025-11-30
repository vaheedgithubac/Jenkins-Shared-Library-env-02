/*def call() {
    try { return sh(script: "git rev-parse --short HEAD", returnStdout: true).trim() } 
    catch (e) { return "no-sha" }
}*/

def call() {
    try {
        // Ensure safe directory for git 2.35+ paranoia
        sh "git config --global --add safe.directory '${env.WORKSPACE}'"

        // Check if repo exists
        if (!fileExists("${env.WORKSPACE}/.git")) {
            echo "⚠️ No Git repository found in workspace"
            return "no-sha"
        }

        // Get short commit ID
        def commitId = sh(
            script: "git -C '${env.WORKSPACE}' rev-parse --short HEAD",
            returnStdout: true
        ).trim()

        if (!commitId) {
            echo "⚠️ Git command returned empty"
            return "no-sha"
        }

        return commitId
    } catch (e) {
        echo "⚠️ Error getting latest commit ID: ${e}"
        return "no-sha"
    }
}


