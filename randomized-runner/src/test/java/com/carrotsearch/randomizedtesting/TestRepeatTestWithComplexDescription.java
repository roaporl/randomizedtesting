package com.carrotsearch.randomizedtesting;

import java.util.ArrayList;
import java.util.List;

import org.fest.assertions.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import com.carrotsearch.randomizedtesting.annotations.Listeners;
import com.carrotsearch.randomizedtesting.annotations.Name;
import com.carrotsearch.randomizedtesting.annotations.ParametersFactory;
import com.carrotsearch.randomizedtesting.annotations.Repeat;
import com.carrotsearch.randomizedtesting.annotations.Seed;
import com.carrotsearch.randomizedtesting.annotations.Seeds;
import com.carrotsearch.randomizedtesting.rules.SystemPropertiesRestoreRule;

/** */
public class TestRepeatTestWithComplexDescription extends WithNestedTestClass {
  @Rule
  public SystemPropertiesRestoreRule restoreProperties = new SystemPropertiesRestoreRule(); 

  static ArrayList<String> buf;

  public static class CaptureFailuresListener extends RunListener {
    @Override
    public void testStarted(Description description) throws Exception {
      buf.add(description.getMethodName());
    }
  }

  @Seed("deadbeef")
  @Listeners({CaptureFailuresListener.class})
  public static class Nested extends RandomizedTest {
    public Nested(@Name("value") String value) {
    }

    @Test
    @Seeds({@Seed(), @Seed("deadbeef")})
    @Repeat(iterations = 5, useConstantSeed = false)
    public void testFoo() {}

    @Test
    public void testBar() {}

    @ParametersFactory()
    public static Iterable<Object[]> parameters() {
      List<Object[]> params = new ArrayList<Object[]>();
      params.add($(""));
      params.add($("a/b/c"));
      params.add($("\\"));
      params.add($("$[]{}\\"));
      return params;
    }
  }

  @Test
  public void checkFilteringByName() {
    // Collect all test names first.
    buf = new ArrayList<String>();
    JUnitCore.runClasses(Nested.class);
    
    for (String testName : buf) {
      buf = new ArrayList<String>();
      System.setProperty(SysGlobals.SYSPROP_TESTMETHOD(), testName);

      Result result = JUnitCore.runClasses(Nested.class);
      Assertions.assertThat(result.wasSuccessful()).as(result.getFailures().toString()).isTrue();
      Assertions.assertThat(buf).containsOnly(testName);
    }
  }
}
