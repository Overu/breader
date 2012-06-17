package com.goodow.web.reader.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;

import com.goodow.wave.test.BaseTest;

public class ExampleTest extends BaseTest {

  private static Map<Class<?>, Integer> count = new HashMap<Class<?>, Integer>();

  public static String generateId(final Class<?> clazz) {
    Integer result = count.get(clazz);
    if (result == null) {
      result = 0;
    }
    result += 1;
    count.put(clazz, result);
    return clazz.getSimpleName() + "-" + result;
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }
}
