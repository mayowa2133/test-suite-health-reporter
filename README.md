# Test Health CLI & Reporter

A Java CLI tool that reads raw test execution results from JSON and prints a concise test health summary.

## Features
- Total tests run
- Pass and fail percentages
- Names of failed tests (`FAIL` and `ERROR` are treated as failures)
- Jenkins Job DSL definition for scheduled daily execution

## Prerequisites
- Java 17+
- Maven 3.8+

## Build
```bash
mvn clean package
```

This creates an executable shaded jar:
`target/test-health-cli-reporter-1.0.0-all.jar`

## Run
```bash
java -jar target/test-health-cli-reporter-1.0.0-all.jar result.json
```

## Example Output
```text
Test Health Report
==================
Input File: result.json
Total Tests Run: 10
Pass Percentage: 60.00%
Fail Percentage: 40.00%
Failed Tests:
- Vault Document Upload
- Permission Sync Service
- CRM Lead Conversion
- PDF Generation Service
```

## Jenkins Job DSL
Job definition is in:
- `jenkins/test-health-job.groovy`

It includes:
- Daily trigger at **9:00 AM America/Los_Angeles** (`TZ=America/Los_Angeles` + cron)
- String parameter `RESULT_FILE`
- Timestamps
- Absolute timeout of 10 minutes
- Git SCM checkout of this Maven project
- Maven build + CLI execution printing to console

Repository URL configured in the job DSL:
- `https://github.com/mayowa2133/test-suite-health-reporter.git`

## Suggested Infrastructure Improvement (If More Time)
Persist each run summary (pass/fail counts and failed test names) into a small datastore (for example, PostgreSQL or S3 + Athena), then visualize historical trends in Grafana. This would make flaky/frequently failing tests obvious over time instead of only showing a single run snapshot.
