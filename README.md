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

## 30-Second Quick Start
```bash
git clone https://github.com/mayowa2133/test-suite-health-reporter.git
cd test-suite-health-reporter
java -version
mvn -version
mvn clean package
java -jar target/test-health-cli-reporter-1.0.0-all.jar result.json
```

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
I would implement a **Test Health History pipeline** using **S3 + Athena + Grafana**:

1. After each Jenkins run, write a structured summary record (for example: `build_id`, `git_sha`, `timestamp`, `total_tests`, `pass_percentage`, `fail_percentage`, `failed_tests[]`).
2. Upload that record to S3 with date partitions, for example:  
   `s3://test-health-reports/year=2026/month=02/day=12/build=123/summary.json`
3. Query historical results with Athena and build Grafana panels for:
   - Failure rate by test over rolling windows (7/14/30 days)
   - Most frequently failing tests
   - Pass/fail trend over time
4. Add alerts (for example: fail rate > 20% for a test over the last 7 days with at least 5 runs).

Why this choice:
- Very low operational overhead (no database server to manage).
- Cheap for append-only, batch-style reporting.
- Easy auditability because each build writes immutable records.

Tradeoffs:
- Athena is not low-latency for interactive queries (seconds, not milliseconds).
- Schema changes must be handled carefully (versioned JSON/columns).
- If near-real-time dashboards or transactional queries become important, PostgreSQL/TimescaleDB is better, but requires backup/HA/maintenance.
