def call() {
    dir(env.WORKSPACE)
    {
        try { return sh(script: "git rev-parse --short HEAD", returnStdout: true).trim() } 
        catch (e) { return "no-sha" }
    }
}
