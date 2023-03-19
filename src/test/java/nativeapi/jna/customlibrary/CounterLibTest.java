package nativeapi.jna.customlibrary;

import com.sun.jna.Platform;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

public class CounterLibTest {

  @Before
  public void before() {
    assumeTrue(Platform.isLinux());
  }

  @Test
  public void testCountSuccess() throws Exception {
    assertEquals(1, CounterLib.INSTANCE.incrementAndGet());
    assertEquals(2, CounterLib.INSTANCE.incrementAndGet());
  }

}
