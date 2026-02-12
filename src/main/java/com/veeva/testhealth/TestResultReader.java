package com.veeva.testhealth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class TestResultReader {
  private final ObjectMapper objectMapper;

  public TestResultReader() {
    this.objectMapper = new ObjectMapper();
  }

  public List<TestResult> readResults(Path resultFile) throws IOException {
    return objectMapper.readValue(resultFile.toFile(), new TypeReference<List<TestResult>>() {});
  }
}
