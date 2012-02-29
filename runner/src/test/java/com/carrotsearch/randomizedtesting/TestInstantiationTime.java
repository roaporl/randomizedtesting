package com.carrotsearch.randomizedtesting;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Check that {@link BeforeClass} hooks are called before instance initializers.
 */
public class TestInstantiationTime extends RandomizedTest {
  
  private static String constant;

  // instance initializer.
  public String copyOfStatic = constant.toUpperCase();
  
  @BeforeClass
  public static void prepare() {
    constant = "constant";
  }
  
  @Test
  public void testDummy() {
    // empty.
  }
}
