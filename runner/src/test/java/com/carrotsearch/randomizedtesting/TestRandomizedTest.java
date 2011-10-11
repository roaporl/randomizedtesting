package com.carrotsearch.randomizedtesting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TestRandomizedTest extends RandomizedTest {
  @Test
  public void testRandomInt() {
    boolean [] array = new boolean [10];
    for (int i = 0; i < 10000; i++) 
      array[randomInt(array.length - 1)] = true;

    for (boolean b: array) 
      assertTrue(b);
  }

  @Test
  public void testRandomIntBetween() {
    boolean [] array = new boolean [10];
    for (int i = 0; i < 10000; i++) 
      array[randomIntBetween(0, array.length - 1)] = true;

    for (boolean b: array) 
      assertTrue(b);
  }

  @Test
  public void testRandomIntBetweenBoundaryCases() {
    for (int i = 0; i < 10000; i++) {
      int j = randomIntBetween(0, Integer.MAX_VALUE);
      assertTrue(j >= 0 && j <= Integer.MAX_VALUE);

      // This must fall in range, but nonetheless
      randomIntBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
  }

  @Test
  public void testRandomFromArray() {
    try {
      randomFrom(new Object [] {});
      fail();
    } catch (IllegalArgumentException e) {
      // expected.
    }
    
    Integer [] ints = new Integer [10];
    for (int i = 0; i < ints.length; i++)
      ints[i] = i;
    
    for (int i = 0; i < 10000; i++) {
      Integer j = randomFrom(ints);
      if (j != null) {
        ints[j] = null;
      }
    }
    
    for (int i = 0; i < ints.length; i++)
      assertTrue(ints[i] == null);
  }

  @Test
  public void testRandomFromList() {
    try {
      randomFrom(Collections.emptyList());
      fail();
    } catch (IllegalArgumentException e) {
      // expected.
    }

    List<Integer> ints = new ArrayList<Integer>();
    for (int i = 0; i < 10; i++)
      ints.add(i);
    
    for (int i = 0; i < 10000; i++) {
      Integer j = randomFrom(ints);
      if (j != null) {
        ints.set(j, null);
      }
    }
    
    for (int i = 0; i < ints.size(); i++)
      assertTrue(ints.get(i) == null);
  }

  @Test
  public void testNewTempDir() {
    for (int i = 0; i < 10; i++) {
      File dir = newTempDir();
      assertNotNull(dir);
      assertTrue(dir.isDirectory());
      assertTrue(dir.canWrite());
      assertTrue(dir.canRead());
      assertTrue(dir.canExecute());
      assertEquals(0, dir.listFiles().length);
    }
  }

  @Test
  public void testNewTempFile() throws IOException {
    for (int i = 0; i < 10; i++) {
      File file = newTempFile();
      assertNotNull(file);
      assertTrue(file.isFile());
      assertTrue(file.canWrite());
      assertTrue(file.canRead());

      new FileOutputStream(file).close();
    }
  }

  @Test
  public void testRandomLocale() {
    assertNotNull(randomLocale());
  }

  @Test
  public void testRandomTimeZone() {
    assertNotNull(randomTimeZone());
  }

  // TODO: port random string tests from Lucene (if there are any).
}