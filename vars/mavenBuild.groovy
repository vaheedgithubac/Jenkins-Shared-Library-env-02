def call(Map config = [:]) {

    def required = ["MAVEN_SKIP_TESTS"]
    required.each { key ->
        if (!config[key]) {
            error "❌ MAVEN: Missing required parameter '${key}'"
        }
    }

    // Normalize skipTests
    def maven_skip_tests = (config.MAVEN_SKIP_TESTS in [true, 'true']) ? 'true' : 'false'
    def maven_goals = config.MAVEN_GOALS ?: 'clean package'

    echo "Running Maven: ${maven_goals} -DskipTests=${skipTests}"

    try { sh "mvn ${maven_goals} -DskipTests=${skipTests}" } 
    catch (Exception ex) {
        echo "Maven build failed: ${ex.message}"
        error "❌ Maven Build failed"
    }
}
