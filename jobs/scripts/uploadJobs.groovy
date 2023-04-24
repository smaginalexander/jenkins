imeout(60) {
    node('jobs-uploader') {
        stage("Checkout") {
            checkout scm
        }
        stage("Deploy changes to jenkins") {
            sh "jenkins-jobs --conf ${WORKSPACE}/jobs/conf/jenkins-job-builder.ini update ./jobs"
        }
    }
}