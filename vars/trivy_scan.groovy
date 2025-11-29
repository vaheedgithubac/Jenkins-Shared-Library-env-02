def call(Map config = [:]) {

    // --------------------------------
    // 1️⃣ Validate required parameters
    // --------------------------------
    def required = [
        "MODE",
        "TARGET",
        "OUTPUT_REPORT_FORMAT",
        "PROJECT_NAME",
        "COMPONENT"
    ]

    required.each { key ->
        if (!config[key]) {
            error "❌ Trivy: Missing required parameter '${key}'"
        }
    }
    
    // === Read Environment Variables
    def mode                 = config.MODE
    def target               = config.TARGET
    def output_report_format = config.OUTPUT_REPORT_FORMAT
    def project_name         = config.PROJECT_NAME
    def component            = config.COMPONENT
    def git_latest_commit_id = env.MY_GIT_LATEST_COMMIT_ID

    // -----------------------------------
    // 2️⃣ Determine proper file extension
    // -----------------------------------
    def ext = [
        "table": "html",
        "json" : "json",
        "sarif": "sarif",
        "yaml" : "yaml"
    ][output_report_format] ?: output_report_format  // fallback to "?: output_report_format" if unknown
    
    def output_report = ""
    def outDir = "trivy-reports"
    sh "mkdir -p ${outDir}"

    if(mode.toLowerCase() == "fs" ){
        output_report = "${outDir}/${project_name}-${component}-${mode}-${git_latest_commit_id}.${ext}"   // trivy-reports/expense-backend-fs-7drt46y.html
    }
    else if (mode.toLowerCase() == "image"){
        //def safeTarget = target.replaceAll(/[:\/]/, "-")  // replaces ":"" or "/"" with "-"
        output_report = "${outDir}/${project_name}-${component}-${mode}-${git_latest_commit_id}.${ext}"   // trivy-reports/expense-backend-image-7drt46y.html
    }
    else {
        error "❌ Invalid mode: Choose 'fs' or 'image'"
    }

    // -------------------------
    // 3️⃣ Log info
    // -------------------------
    echo "🛡 Running Trivy scan"
    echo "📄 Scan Report: '${output_report}'"
    echo "🎯 Target: '${target}'"

    // ----------------------------------------------------
    // 4️⃣ Run Trivy safely (handle any special characters)
    // ----------------------------------------------------
    sh """
            trivy {mode} \
            --format ${output_report_format} \
            --output ${output_report} \
            --severity MEDIUM,HIGH,CRITICAL \
            {target}   
    """

    echo "✅ Trivy scan completed successfully. Report stored at: '{output_report}'"
}
