/*def call() {
    try { return sh(script: "git rev-parse --short HEAD", returnStdout: true).trim() } 
    catch (e) { return "no-sha" }
}*/

def call() {
    // Use the GIT_COMMIT environment variable set by Jenkins git step
    if (env.GIT_COMMIT) {
        return env.GIT_COMMIT.take(7)   // Return short SHA (first 7 characters)
    } else {
        echo "⚠️ GIT_COMMIT not found, returning 'no-sha'"
        return "no-sha"
    }
}



