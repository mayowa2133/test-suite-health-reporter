package com.veeva.testhealth;

import java.util.Locale;

public enum TestStatus {
  PASS,
  FAIL,
  ERROR;

  public static TestStatus fromRaw(String rawStatus) {
    if (rawStatus == null || rawStatus.isBlank()) {
      throw new IllegalArgumentException("Status is missing for a test result.");
    }

    try {
      return TestStatus.valueOf(rawStatus.trim().toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException("Unsupported status value: " + rawStatus);
    }
  }
}
