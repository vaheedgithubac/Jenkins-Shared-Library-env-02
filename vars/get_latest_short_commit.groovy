/*def call() {
    try { return sh(script: "git rev-parse --short HEAD", returnStdout: true).trim() } 
    catch (e) { return "no-sha" }
}*/

def call() {
    try {
        // Ensure we run in workspace (where gitCheckout_1 checked out)
        dir(env.WORKSPACE) {
            // Check if .git exists first
            if (!fileExists(".git")) {
                echo "⚠️ No Git repository found in workspace"
                return "no-sha"
            }

            def commitId = sh(
                script: "git rev-parse --short HEAD",
                returnStdout: true
            ).trim()
            
            if (!commitId) {
                echo "⚠️ Git command returned empty"
                return "no-sha"
            }

            return commitId
        }
    } catch (e) {
        echo "⚠️ Error getting latest commit ID: ${e}"
        return "no-sha"
    }
}

