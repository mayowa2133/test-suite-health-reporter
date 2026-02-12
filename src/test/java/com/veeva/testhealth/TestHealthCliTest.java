package com.veeva.testhealth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class TestHealthCliTest {
  @Test
  void run_returnsErrorOnMissingArguments() {
    TestHealthCli cli = new TestHealthCli();
    ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
    ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();

    int exitCode = cli.run(new String[]{}, new PrintStream(outBuffer), new PrintStream(errBuffer));

    assertEquals(1, exitCode);
    assertTrue(errBuffer.toString().contains("Usage"));
  }

  @Test
  void run_printsExpectedSummaryForValidFile() throws IOException {
    Path tempFile = Files.createTempFile("result", ".json");
    Files.writeString(tempFile, """
        [
          {"test_id":"TC-1","test_name":"Login","status":"PASS","duration_ms":100,"timestamp":"2026-01-08T10:00:00Z"},
          {"test_id":"TC-2","test_name":"Upload","status":"FAIL","duration_ms":200,"timestamp":"2026-01-08T10:01:00Z"},
          {"test_id":"TC-3","test_name":"Sync","status":"ERROR","duration_ms":0,"timestamp":"2026-01-08T10:02:00Z"}
        ]
        """);

    TestHealthCli cli = new TestHealthCli();
    ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
    ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();

    int exitCode = cli.run(new String[]{tempFile.toString()}, new PrintStream(outBuffer), new PrintStream(errBuffer));
    String output = outBuffer.toString();

    assertEquals(0, exitCode);
    assertTrue(output.contains("Total Tests Run: 3"));
    assertTrue(output.contains("Pass Percentage: 33.33%"));
    assertTrue(output.contains("Fail Percentage: 66.67%"));
    assertTrue(output.contains("- Upload"));
    assertTrue(output.contains("- Sync"));
  }
}
