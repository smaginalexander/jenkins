timeout(60) {
    node('maven') {
        stage('Checkout') {
            checkout scm
        }
        stage('Run tests') {
            def jobs = [:]

            def runnerJobs = "$TEST_TYPE".split(",")

            jobs['ui-tests'] = {
                node('maven') {
                    stage('Ui tests on chrome') {
                        echo "____________________________________________________________________"
                        if ('ui' in runnerJobs) {
                            catchError(buldResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                                build(job: 'ui-tests',
                                        parameters: [
                                                string(name: 'BRANCH', value: BRANCH),
                                                string(name: 'BASE_URL', value: BASE_URL),
                                                string(name: 'BROWSER', value: BROWSER),
                                                string(name: 'BROWSER_VERSION', value: BROWSER_VERSION),
                                                string(name: 'GRID_URL', value: GRID_URL)
                                        ])
                            }
                        } else {
                            echo 'Skipping stage...'
                            Utils.markStageSkippedForConditional('keystone api tests')
                        }
                    }
                }
            }
            parallel jobs
        }
    }
}