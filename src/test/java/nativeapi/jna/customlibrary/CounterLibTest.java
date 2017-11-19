package nativeapi.jna.customlibrary;

import org.junit.Assert;
import org.junit.Test;

public class CounterLibTest {

	@Test
	public void testCountSuccess() throws Exception {
		Assert.assertEquals(1, CounterLib.INSTANCE.incrementAndGet());
		Assert.assertEquals(2, CounterLib.INSTANCE.incrementAndGet());
	}

}
