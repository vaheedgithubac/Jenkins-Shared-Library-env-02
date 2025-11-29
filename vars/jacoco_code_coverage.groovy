def call(Map config = [:]) {

    // Validate required parameters
    /*
    if (!config.jacoco_groupId) { error "Parameter 'jacoco_groupId' is required" }
    else { jacoco_groupId = config.jacoco_groupId }

    if (!config.jacoco_artifactId) { error "Parameter 'jacoco_artifactId' is required" }
    else { jacoco_artifactId = config.jacoco_artifactId }

    if (!config.jacoco_version) { error "Parameter 'jacoco_version' is required" }
    else { jacoco_version = config.jacoco_version }

    if (!config.jacoco_goal) { error "Parameter 'jacoco_goal' is required" }
    else { jacoco_goal = config.jacoco_goal }
    */

    // Validate required parameters
    def required = ["JACOCO_GROUPID", "JACOCO_ARTIFACT_ID", "JACOCO_VERSION", "JACOCO_GOAL"]

    required.each { key ->
        if (!config[key]) {
            error "❌ Jacoco: Missing required parameter '${key}'"
        }
    }

    def jacoco_groupId     = config.JACOCO_GROUPID.trim()
    def jacoco_artifactId  = config.JACOCO_ARTIFACT_ID.trim()
    def jacoco_version     = config.JACOCO_VERSION.trim()
    def jacoco_goal        = config.JACOCO_GOAL.trim()

    echo "Running Jacoco step: ${groupId}:${artifactId}:${version}:${goal}"
    
    try { sh "mvn ${jacoco_groupId}:${jacoco_artifactId}:${jacoco_version}:${jacoco_goal}" }
    catch (Exception ex) { error "❌ Jacoco Maven step failed: ${ex.message}" }

    // sh 'mvn org.jacoco:jacoco-maven-plugin:0.8.7:prepare-agent'

    echo "✅ JACOCO code coverage completed successfully. Report stored at:'${env.WORKSPACE}'"
}
