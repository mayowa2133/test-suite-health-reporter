# Test Health CLI & Reporter Implementation Plan

## Objective
Build a Java-based CLI that reads a raw test result JSON file and prints a health summary, plus provide a Jenkins Job DSL definition that schedules and runs the CLI daily.

## Requirements Interpreted
1. Parse JSON records with at least:
   - `test_id`
   - `test_name`
   - `status` (`PASS`, `FAIL`, `ERROR`)
   - `duration_ms` (sample file uses this naming)
   - `timestamp`
2. CLI output must include:
   - Total number of tests run
   - Overall pass/fail percentages
   - Names of tests that failed
3. Jenkins Job DSL must:
   - Trigger every day at 9AM PST
   - Define a string parameter for the results file name
   - Enable timestamps
   - Enforce absolute timeout of 10 minutes
   - Check out this Maven-based project from git
   - Execute the CLI with the provided input file and print output to console
4. Include README with:
   - Build and run instructions
   - One infrastructure improvement idea

## Implementation Steps
1. **Project scaffold**
   - Initialize Maven project structure (`src/main/java`, `src/test/java`).
   - Add `pom.xml` with Java 17, Jackson for JSON parsing, JUnit 5 for tests.
   - Configure jar packaging with dependencies for easy Jenkins execution.

2. **Domain and parsing**
   - Create `TestResult` model mapped from JSON fields.
   - Create parser/service to read JSON arrays from file.
   - Validate status values and handle malformed input with clear CLI errors.

3. **Reporting logic**
   - Implement summary computation:
     - total runs
     - pass count
     - fail count (treat `FAIL` + `ERROR` as non-pass)
     - pass/fail percentages
   - Build deterministic list of failed test names (no duplicates, preserve first-seen order).

4. **CLI entrypoint**
   - Add main class with argument handling:
     - require file path argument
     - validate file existence/readability
   - Print concise human-readable report to stdout.
   - Return non-zero exit code for usage or processing errors.

5. **Automated tests**
   - Unit tests for report computation accuracy.
   - End-to-end style test for parsing sample-like JSON and expected failed names.
   - Edge case test for empty input file array.

6. **Jenkins Job DSL**
   - Create `jenkins/test-health-job.groovy`.
   - Define job configuration for schedule, parameter, wrappers, SCM checkout, and CLI execution.
   - Keep repository URL as clearly marked placeholder for private repo replacement.

7. **Documentation**
   - Create `README.md`:
     - prerequisites
     - build command
     - run command
     - example output
     - Jenkins Job DSL usage
     - one future infrastructure improvement

8. **Verification**
   - Run `mvn test`.
   - Build with `mvn package`.
   - Execute CLI against provided `result.json`.
   - Confirm output satisfies all assignment requirements.

## Execution Checklist
- [x] Gather requirements from PDF and sample JSON
- [x] Create Maven project files
- [x] Implement parser and report logic
- [x] Implement CLI main entrypoint
- [x] Add unit tests
- [x] Add Jenkins Job DSL definition
- [x] Add README documentation
- [x] Run tests and package
- [x] Run CLI with provided sample data
- [x] Final review against submission checklist
