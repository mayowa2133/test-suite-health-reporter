job('test-health-cli-reporter') {
  description('Daily Test Health CLI execution for automated suite visibility.')

  parameters {
    stringParam('RESULT_FILE', 'result.json', 'Name/path of the JSON result file in the repository workspace.')
  }

  triggers {
    cron('TZ=America/Los_Angeles\n0 9 * * *')
  }

  wrappers {
    timestamps()
    timeout {
      absolute(10)
    }
  }

  scm {
    git {
      remote {
        // Private repository URL for this project.
        url('https://github.com/mayowa2133/test-suite-health-reporter.git')
      }
      branch('*/main')
    }
  }

  steps {
    shell('''#!/bin/bash
set -euo pipefail

mvn -B -DskipTests clean package
java -jar target/test-health-cli-reporter-1.0.0-all.jar "$RESULT_FILE"
''')
  }
}
