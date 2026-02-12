package com.veeva.testhealth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class TestResultReaderTest {
  private final TestResultReader reader = new TestResultReader();

  @Test
  void readResults_parsesJsonArrayWithDurationAlias() throws IOException {
    Path tempFile = Files.createTempFile("result", ".json");
    Files.writeString(tempFile, """
        [
          {
            "test_id": "TC-1",
            "test_name": "Login",
            "status": "PASS",
            "duration": 111,
            "timestamp": "2026-01-08T10:00:00Z"
          },
          {
            "test_id": "TC-2",
            "test_name": "Upload",
            "status": "FAIL",
            "duration_ms": 222,
            "timestamp": "2026-01-08T10:01:00Z"
          }
        ]
        """);

    List<TestResult> results = reader.readResults(tempFile);

    assertEquals(2, results.size());
    assertEquals("TC-1", results.get(0).getTestId());
    assertEquals(111, results.get(0).getDurationMs());
    assertEquals(222, results.get(1).getDurationMs());
  }
}
