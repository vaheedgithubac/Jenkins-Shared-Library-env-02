def call(Map config = [:]) {

    // Validate required parameters
    def sources = config.sources ?: "."
    def required = [
        "sonarqubeAPI",
        "scannerHome",
        "projectName",
        "projectKey"
    ]

    required.each { key ->
        if (!config[key]) {
            error "❌ Sonarqube: Missing required parameter '${key}'"
        }
    }

    def sonarqubeAPI = config.sonarqubeAPI
    def scannerHome  = config.scannerHome
    def projectName  = config.projectName
    def projectKey   = config.projectKey

    withSonarQubeEnv(sonarqubeAPI) {
        sh """
            ${scannerHome}/bin/sonar-scanner \
            -Dsonar.projectName='${projectName}' \
            -Dsonar.projectKey='${projectKey}' -X
        """
    }
}
