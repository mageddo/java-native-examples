package nativeapi.jna.customlibrary;

import com.sun.jna.Platform;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

public class GoCalculatorTest {

  @Before
  public void before() {
    assumeTrue(Platform.isLinux());
  }

  @Test
  public void sum() throws Exception {
    Assert.assertEquals(3, GoCalculator.INSTANCE.Add(1, 2));
    Assert.assertEquals(5, GoCalculator.INSTANCE.Add(-2, 7));

  }

}
